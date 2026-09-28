package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.LutealAmber
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.OvulationTeal
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(currentDay, totalDays) {
        val target = (currentDay.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)
        animatedProgress.animateTo(
            targetValue = target,
            animationSpec = tween(durationMillis = 900)
        )
    }

    val trackBgColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val phaseColor = Color(phase.colorHex)

    val infiniteTransition = rememberInfiniteTransition(label = "aura_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Modal popup when clicking a phase
    selectedPhaseForModal?.let { targetPhase ->
        CyclePhaseDetailModal(
            phase = targetPhase,
            language = language,
            onDismiss = { selectedPhaseForModal = null }
        )
    }

    Column(
        modifier = modifier.testTag("cycle_wheel_container"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(315.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer Circular Arc Track with Tap Detector on color arcs
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .pointerInput(totalDays) {
                        detectTapGestures { offset ->
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val dx = offset.x - centerX
                            val dy = offset.y - centerY
                            val dist = sqrt((dx * dx + dy * dy).toDouble()).toFloat()

                            val strokeWidthPx = 16.dp.toPx()
                            val radiusPx = (Math.min(size.width, size.height) - strokeWidthPx) / 2f
                            val innerBound = radiusPx - strokeWidthPx * 1.5f
                            val outerBound = radiusPx + strokeWidthPx * 1.5f

                            if (dist in innerBound..outerBound) {
                                val angleRad = atan2(dy.toDouble(), dx.toDouble())
                                var angleDeg = Math.toDegrees(angleRad).toFloat()
                                var normAngle = angleDeg - (-90f)
                                if (normAngle < 0f) normAngle += 360f

                                val menstrualAngle = (5f / totalDays) * 360f
                                val follicularAngle = (8f / totalDays) * 360f
                                val ovulationAngle = (3f / totalDays) * 360f

                                val tappedPhase = when {
                                    normAngle < menstrualAngle -> CyclePhase.MENSTRUAL
                                    normAngle < (menstrualAngle + follicularAngle) -> CyclePhase.FOLLICULAR
                                    normAngle < (menstrualAngle + follicularAngle + ovulationAngle) -> CyclePhase.OVULATORY
                                    else -> CyclePhase.LUTEAL
                                }
                                selectedPhaseForModal = tappedPhase
                            } else if (dist < innerBound) {
                                selectedPhaseForModal = phase
                            }
                        }
                    }
            ) {
                val strokeWidth = 16.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val arcSize = Size(diameter, diameter)
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                // Background subtle track
                drawArc(
                    color = trackBgColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 4 Segmented Phase Arcs
                val menstrualAngle = (5f / totalDays) * 360f
                val follicularAngle = (8f / totalDays) * 360f
                val ovulationAngle = (3f / totalDays) * 360f
                val lutealAngle = 360f - (menstrualAngle + follicularAngle + ovulationAngle)

                // 1. Menstrual phase arc (Coral Red)
                drawArc(
                    color = MenstrualRed,
                    startAngle = -90f,
                    sweepAngle = menstrualAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 2. Follicular phase arc (Purple)
                drawArc(
                    color = FollicularPurple.copy(alpha = 0.9f),
                    startAngle = -90f + menstrualAngle,
                    sweepAngle = follicularAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 3. Ovulation phase arc (Teal)
                drawArc(
                    color = OvulationTeal,
                    startAngle = -90f + menstrualAngle + follicularAngle,
                    sweepAngle = ovulationAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 4. Luteal phase arc (Amber)
                drawArc(
                    color = LutealAmber.copy(alpha = 0.9f),
                    startAngle = -90f + menstrualAngle + follicularAngle + ovulationAngle,
                    sweepAngle = lutealAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Current day indicator bubble on the track
                val angleRad = Math.toRadians((-90.0 + (animatedProgress.value * 360.0)))
                val radius = diameter / 2
                val centerOffset = Offset(size.width / 2, size.height / 2)
                val indicatorX = centerOffset.x + (radius * cos(angleRad)).toFloat()
                val indicatorY = centerOffset.y + (radius * sin(angleRad)).toFloat()

                // Outer glow ring
                drawCircle(
                    color = surfaceColor,
                    radius = 16.dp.toPx(),
                    center = Offset(indicatorX, indicatorY)
                )
                // Primary fill dot
                drawCircle(
                    color = primaryColor,
                    radius = 11.dp.toPx(),
                    center = Offset(indicatorX, indicatorY)
                )
                // Center mini white dot
                drawCircle(
                    color = Color.White,
                    radius = 3.5.dp.toPx(),
                    center = Offset(indicatorX, indicatorY)
                )
            }

            // Subtle glowing aura halo ring
            Box(
                modifier = Modifier
                    .size(244.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                        alpha = pulseAlpha
                    }
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                phaseColor.copy(alpha = 0.5f),
                                phaseColor.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Center card with Phase & Days Countdown (Clickable to inspect current phase)
            Surface(
                modifier = Modifier
                    .size(236.dp)
                    .clip(CircleShape)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)), CircleShape)
                    .clickable { selectedPhaseForModal = phase }
                    .testTag("cycle_wheel_center"),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Phase badge with high contrast
                    Surface(
                        color = phaseColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
                        modifier = Modifier.clickable { selectedPhaseForModal = phase }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(phaseColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${strings.cycleDayPrefix} $currentDay • $localizedPhase",
                                color = phaseColor,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Days countdown (Apple Health / Clue luxury style)
                    Text(
                        text = "$daysUntil",
                        fontFamily = OutfitDisplayFamily,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 48.sp
                    )

                    Text(
                        text = strings.daysUntilNextPeriod,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Pregnancy chance indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (pregnancyChance == "Peak" || pregnancyChance == "High") OvulationTeal else MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${strings.chancePrefix}: $localizedChance",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Color Legend Chips Row (Click any color to inspect its meaning)
        Row(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val phasesList = listOf(
                Pair(CyclePhase.MENSTRUAL, MenstrualRed),
                Pair(CyclePhase.FOLLICULAR, FollicularPurple),
                Pair(CyclePhase.OVULATORY, OvulationTeal),
                Pair(CyclePhase.LUTEAL, LutealAmber)
            )

            for ((p, col) in phasesList) {
                val pName = when (p) {
                    CyclePhase.MENSTRUAL -> if (language == AppLanguage.GERMAN) "Periode" else if (language == AppLanguage.ALBANIAN) "Menstruacione" else "Period"
                    CyclePhase.FOLLICULAR -> if (language == AppLanguage.GERMAN) "Follikel" else if (language == AppLanguage.ALBANIAN) "Follikulare" else "Follicular"
                    CyclePhase.OVULATORY -> if (language == AppLanguage.GERMAN) "Eisprung" else if (language == AppLanguage.ALBANIAN) "Ovulacioni" else "Ovulation"
                    CyclePhase.LUTEAL -> if (language == AppLanguage.GERMAN) "Luteal" else if (language == AppLanguage.ALBANIAN) "Luteale" else "Luteal"
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = col.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, col.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedPhaseForModal = p }
                        .testTag("phase_legend_chip_${p.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(col)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = pName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = col
                        )
                    }
                }
            }
        }
    }
}
