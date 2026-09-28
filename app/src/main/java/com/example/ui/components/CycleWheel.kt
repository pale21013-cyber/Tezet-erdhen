package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.ui.theme.RosePrimary
import kotlin.math.cos
import kotlin.math.sin

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

    Box(
        modifier = modifier
            .size(315.dp)
            .testTag("cycle_wheel_container"),
        contentAlignment = Alignment.Center
    ) {
        // Outer Circular Arc Track
        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
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

        // Center card with Phase & Days Countdown
        Surface(
            modifier = Modifier
                .size(236.dp)
                .clip(CircleShape)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)), CircleShape)
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
                    border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f))
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
}
