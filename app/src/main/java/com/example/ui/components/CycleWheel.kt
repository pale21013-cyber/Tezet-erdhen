package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedChance
import com.example.localization.getLocalizedPhaseName
import com.example.ml.CyclePhase
import com.example.ml.CycleStats
import com.example.ui.theme.HealthCardPink
import com.example.ui.theme.HealthCreamPill
import com.example.ui.theme.HealthPastelBlue
import com.example.ui.theme.HealthPastelGreen
import com.example.ui.theme.HealthPastelLilac
import com.example.ui.theme.HealthPastelPink
import com.example.ui.theme.HealthPastelYellow
import com.example.ui.theme.HealthPitchBlack
import com.example.ui.theme.OutfitDisplayFamily
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Health & Cycle Score Hero Wheel matching the reference UI/UX:
 * - Thick pastel pill curved arc segments
 * - Floating circular badge icons (DNA, Lungs, Heart, Brain, etc.)
 * - Large bold central score display (e.g. 8.8) with "your health score ⓘ"
 * - Black pill "Plan check-up" action row with circular buttons
 * - "Aura assistant" pastel pink card with "Let's discuss"
 * - "Health systems" breakdown list with progress bars
 */
@Composable
fun CycleWheel(
    stats: CycleStats?,
    onTrackClick: () -> Unit = {},
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val currentDay = stats?.currentCycleDay ?: 14
    val totalDays = (stats?.averageCycleLength?.toInt() ?: 28).coerceAtLeast(20)
    val daysUntil = stats?.daysUntilNextPeriod ?: 6
    val phase = stats?.currentPhase ?: CyclePhase.OVULATORY
    val pregnancyChance = stats?.pregnancyChance ?: "High"

    val localizedPhase = getLocalizedPhaseName(phase, language)
    val localizedChance = getLocalizedChance(pregnancyChance, language)

    var selectedPhaseForModal by remember { mutableStateOf<CyclePhase?>(null) }
    var showHealthScoreDetails by remember { mutableStateOf(false) }

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(currentDay, totalDays) {
        val target = (currentDay.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)
        animatedProgress.animateTo(
            targetValue = target,
            animationSpec = tween(durationMillis = 800)
        )
    }

    // Modal popup when clicking a phase
    selectedPhaseForModal?.let { targetPhase ->
        CyclePhaseDetailModal(
            phase = targetPhase,
            language = language,
            onDismiss = { selectedPhaseForModal = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("cycle_wheel_container"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // --- 1. HERO SEGMENTED DONUT WHEEL WITH BADGE ICONS ---
        Box(
            modifier = Modifier
                .size(310.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Canvas: Thick Rounded Pastel Pill Arcs with Tap Gestures on Circle Parts
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
                    .pointerInput(totalDays, currentDay) {
                        detectTapGestures { offset ->
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val dx = offset.x - centerX
                            val dy = offset.y - centerY
                            val dist = sqrt((dx * dx + dy * dy).toDouble()).toFloat()

                            val strokeWidthPx = 26.dp.toPx()
                            val radiusPx = (minOf(size.width, size.height) - strokeWidthPx) / 2f
                            val innerBound = radiusPx - strokeWidthPx * 1.5f
                            val outerBound = radiusPx + strokeWidthPx * 1.8f

                            if (dist in innerBound..outerBound) {
                                val angleRad = atan2(dy.toDouble(), dx.toDouble())
                                val angleDeg = Math.toDegrees(angleRad).toFloat()
                                val tappedPhase = when {
                                    angleDeg in -180f..-90f -> CyclePhase.FOLLICULAR  // Top-Left (Yellow arc)
                                    angleDeg in -90f..0f -> CyclePhase.OVULATORY     // Top-Right (Sage Green arc)
                                    angleDeg in 0f..90f -> CyclePhase.LUTEAL         // Bottom-Right (Rose Pink arc)
                                    else -> CyclePhase.MENSTRUAL                     // Bottom-Left (Sky Blue arc)
                                }
                                selectedPhaseForModal = tappedPhase
                            } else if (dist < innerBound) {
                                selectedPhaseForModal = phase
                            }
                        }
                    }
            ) {
                val strokeWidth = 26.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val arcSize = Size(diameter, diameter)
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                // 4 Distinct Thick Pastel Pill Arcs with Gaps
                // Segment 1 (Top / Follicular): Pastel Yellow
                drawArc(
                    color = HealthPastelYellow,
                    startAngle = -175f,
                    sweepAngle = 72f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Segment 2 (Right / Ovulatory): Pastel Sage Green
                drawArc(
                    color = HealthPastelGreen,
                    startAngle = -85f,
                    sweepAngle = 70f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Segment 3 (Bottom-Right / Luteal): Pastel Rose Pink
                drawArc(
                    color = HealthPastelPink,
                    startAngle = 5f,
                    sweepAngle = 75f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Segment 4 (Bottom-Left / Menstrual): Pastel Sky Blue
                drawArc(
                    color = HealthPastelBlue,
                    startAngle = 100f,
                    sweepAngle = 65f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Floating Pastel Badge Icons along the ring (Clickable to open corresponding phase modal)
            // 1. Top Shield (Pastel Yellow) -> Follicular
            BadgeIcon(
                emoji = "🛡️",
                bgColor = HealthPastelYellow,
                angleDeg = -135.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.FOLLICULAR }
            )
            // 2. Top-Left Stethoscope / Ovary (Pastel Pink) -> Ovulatory
            BadgeIcon(
                emoji = "🩺",
                bgColor = HealthPastelPink,
                angleDeg = -162.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.OVULATORY }
            )
            // 3. Top-Right Bone (Pastel Lavender) -> Follicular
            BadgeIcon(
                emoji = "🦴",
                bgColor = HealthPastelLilac,
                angleDeg = -105.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.FOLLICULAR }
            )
            // 4. Right Stomach / Nutrition (Pastel Green) -> Ovulatory
            BadgeIcon(
                emoji = "🥗",
                bgColor = HealthPastelGreen,
                angleDeg = -50.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.OVULATORY }
            )
            // 5. Bottom-Right Heart (Pastel Pink) -> Luteal
            BadgeIcon(
                emoji = "🩷",
                bgColor = HealthPastelPink,
                angleDeg = 45.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.LUTEAL }
            )
            // 6. Bottom Brain (Pastel Blue) -> Menstrual
            BadgeIcon(
                emoji = "🧠",
                bgColor = HealthPastelBlue,
                angleDeg = 92.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.MENSTRUAL }
            )
            // 7. Left Lungs (Pastel Green) -> Menstrual
            BadgeIcon(
                emoji = "🫁",
                bgColor = HealthPastelGreen,
                angleDeg = 145.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.MENSTRUAL }
            )
            // 8. Mid-Left DNA (Pastel Gold) -> Follicular
            BadgeIcon(
                emoji = "🧬",
                bgColor = HealthPastelYellow,
                angleDeg = 178.0,
                radiusDp = 132.dp,
                onClick = { selectedPhaseForModal = CyclePhase.FOLLICULAR }
            )

            // Center Content: Huge bold "8.8" & "your health score ⓘ"
            Column(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { selectedPhaseForModal = phase }
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "8.8",
                    fontFamily = OutfitDisplayFamily,
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthPitchBlack,
                    lineHeight = 60.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedPhaseForModal = phase }
                ) {
                    Text(
                        text = when (language) {
                            AppLanguage.GERMAN -> "dein Gesundheitsscore"
                            AppLanguage.ALBANIAN -> "rezultati yt shëndetësor"
                            AppLanguage.ENGLISH -> "your health score"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Phase & Day Sub-label (Clickable to open current phase info)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = HealthPastelPink.copy(alpha = 0.25f),
                    border = BorderStroke(1.dp, HealthPastelPink.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedPhaseForModal = phase }
                        .testTag("center_phase_pill")
                ) {
                    Text(
                        text = "${strings.cycleDayPrefix} $currentDay • $localizedPhase",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = HealthPitchBlack,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }



        // --- INTERACTIVE PHASE LEGEND CHIPS ROW (Click any phase to open its info modal) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val phasesList = listOf(
                Pair(CyclePhase.MENSTRUAL, HealthPastelBlue),
                Pair(CyclePhase.FOLLICULAR, HealthPastelYellow),
                Pair(CyclePhase.OVULATORY, HealthPastelGreen),
                Pair(CyclePhase.LUTEAL, HealthPastelPink)
            )

            for ((p, col) in phasesList) {
                val pName = when (p) {
                    CyclePhase.MENSTRUAL -> if (language == AppLanguage.GERMAN) "Periode" else if (language == AppLanguage.ALBANIAN) "Menstruacione" else "Period"
                    CyclePhase.FOLLICULAR -> if (language == AppLanguage.GERMAN) "Follikel" else if (language == AppLanguage.ALBANIAN) "Follikulare" else "Follicular"
                    CyclePhase.OVULATORY -> if (language == AppLanguage.GERMAN) "Eisprung" else if (language == AppLanguage.ALBANIAN) "Ovulacioni" else "Ovulation"
                    CyclePhase.LUTEAL -> if (language == AppLanguage.GERMAN) "Luteal" else if (language == AppLanguage.ALBANIAN) "Luteale" else "Luteal"
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = col.copy(alpha = 0.28f),
                    border = BorderStroke(1.2.dp, col),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable { selectedPhaseForModal = p }
                        .testTag("phase_legend_chip_${p.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(col)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = pName,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = HealthPitchBlack
                        )
                    }
                }
            }
        }

        // --- 3. "AURA ASSISTANT" PASTEL PINK INSIGHT CARD ---
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = HealthCardPink,
            border = BorderStroke(1.dp, Color(0xFFF9A8D4).copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("aura_assistant_card")
        ) {
            Box(modifier = Modifier.padding(18.dp)) {
                // Background Soft Heart Blob
                Text(
                    text = "🩷",
                    fontSize = 72.sp,
                    color = Color(0xFFF472B6).copy(alpha = 0.22f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 4.dp, bottom = 2.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Aura assistant",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF831843)
                    )

                    Text(
                        text = when (language) {
                            AppLanguage.GERMAN -> "Taigo, dein Gesundheitsscore ist letzte Woche um 28% gesunken"
                            AppLanguage.ALBANIAN -> "Taigo, rezultati yt shëndetësor ra me 28% javën e kaluar"
                            AppLanguage.ENGLISH -> "Taigo, your health score goes down 28% last week"
                        },
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = HealthPitchBlack,
                        lineHeight = 19.sp,
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Black Pill Action Button: "Let's discuss"
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = HealthPitchBlack,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onTrackClick() }
                    ) {
                        Text(
                            text = when (language) {
                                AppLanguage.GERMAN -> "Besprechen"
                                AppLanguage.ALBANIAN -> "Bisedo"
                                AppLanguage.ENGLISH -> "Let's discuss"
                            },
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // --- 4. "HEALTH SYSTEMS" SECTION BREAKDOWN ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("health_systems_section"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: "Health systems" + Black Pill "All ⌄"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (language) {
                        AppLanguage.GERMAN -> "Gesundheitssysteme"
                        AppLanguage.ALBANIAN -> "Sistemet shëndetësore"
                        AppLanguage.ENGLISH -> "Health systems"
                    },
                    fontFamily = OutfitDisplayFamily,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthPitchBlack
                )

                // Black Pill "All ⌄"
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HealthPitchBlack,
                    modifier = Modifier.clip(RoundedCornerShape(50))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (language) {
                                AppLanguage.GERMAN -> "Alle"
                                AppLanguage.ALBANIAN -> "Të gjitha"
                                AppLanguage.ENGLISH -> "All"
                            },
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // System Row 1: Endocrine System (🧬) -> Follicular Phase
            HealthSystemProgressRow(
                emoji = "🧬",
                badgeColor = HealthPastelYellow,
                title = when (language) {
                    AppLanguage.GERMAN -> "Hormonsystem"
                    AppLanguage.ALBANIAN -> "Sistemi endokrin"
                    AppLanguage.ENGLISH -> "Endocrine system"
                },
                score = "8.3",
                progress = 0.83f,
                barColor = HealthPastelYellow,
                onClick = { selectedPhaseForModal = CyclePhase.FOLLICULAR }
            )

            // System Row 2: Respiratory & Vitality (🫁) -> Ovulatory Phase
            HealthSystemProgressRow(
                emoji = "🫁",
                badgeColor = HealthPastelGreen,
                title = when (language) {
                    AppLanguage.GERMAN -> "Atemwege & Vitalität"
                    AppLanguage.ALBANIAN -> "Frymëmarrja & Vitaliteti"
                    AppLanguage.ENGLISH -> "Respiratory & Vitality"
                },
                score = "9.1",
                progress = 0.91f,
                barColor = HealthPastelGreen,
                onClick = { selectedPhaseForModal = CyclePhase.OVULATORY }
            )

            // System Row 3: Cardiovascular & Flow (🩷) -> Luteal Phase
            HealthSystemProgressRow(
                emoji = "🩷",
                badgeColor = HealthPastelPink,
                title = when (language) {
                    AppLanguage.GERMAN -> "Herz-Kreislauf & Zyklus"
                    AppLanguage.ALBANIAN -> "Kardiaku & Qarkullimi"
                    AppLanguage.ENGLISH -> "Cardiovascular system"
                },
                score = "8.6",
                progress = 0.86f,
                barColor = HealthPastelPink,
                onClick = { selectedPhaseForModal = CyclePhase.LUTEAL }
            )

            // System Row 4: Nervous & Sleep (🧠) -> Menstrual Phase
            HealthSystemProgressRow(
                emoji = "🧠",
                badgeColor = HealthPastelBlue,
                title = when (language) {
                    AppLanguage.GERMAN -> "Nervensystem & Schlaf"
                    AppLanguage.ALBANIAN -> "Sistemi nervor & Gjumi"
                    AppLanguage.ENGLISH -> "Nervous & Sleep system"
                },
                score = "8.8",
                progress = 0.88f,
                barColor = HealthPastelBlue,
                onClick = { selectedPhaseForModal = CyclePhase.MENSTRUAL }
            )
        }
    }
}

/**
 * Small circular icon badge floating on the perimeter of the donut ring
 */
@Composable
private fun BadgeIcon(
    emoji: String,
    bgColor: Color,
    angleDeg: Double,
    radiusDp: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit = {}
) {
    val angleRad = Math.toRadians(angleDeg)
    val offsetX = (radiusDp.value * cos(angleRad)).dp
    val offsetY = (radiusDp.value * sin(angleRad)).dp

    Box(
        modifier = Modifier
            .size(36.dp)
            .offset(x = offsetX, y = offsetY),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = bgColor.copy(alpha = 0.45f),
            border = BorderStroke(1.2.dp, bgColor),
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .clickable(onClick = onClick)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 15.sp)
            }
        }
    }
}



/**
 * Health system progress row with circular icon, title, rounded progress bar and score
 */
@Composable
private fun HealthSystemProgressRow(
    emoji: String,
    badgeColor: Color,
    title: String,
    score: String,
    progress: Float,
    barColor: Color,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Emoji Badge
            Surface(
                shape = CircleShape,
                color = badgeColor.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, badgeColor),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title & Progress Bar
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthPitchBlack
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Rounded Progress Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFEFECE5))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .background(barColor)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Score: e.g. "8.3 of 10"
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = score,
                    fontFamily = OutfitDisplayFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = HealthPitchBlack
                )
                Text(
                    text = " of 10",
                    fontSize = 10.5.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }
    }
}
