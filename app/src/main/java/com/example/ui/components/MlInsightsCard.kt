package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entities.MlPredictionEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedPhaseAdvice
import com.example.ml.CyclePhase
import com.example.ml.CycleStats
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.OvulationTeal
import com.example.ui.theme.RosePrimary

@Composable
fun MlInsightsCard(
    stats: CycleStats?,
    predictions: List<MlPredictionEntity>,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val confidencePct = ((stats?.overallConfidence ?: 0.85) * 100).toInt()
    val avgLen = stats?.averageCycleLength?.toInt() ?: 28
    val stdDev = String.format("%.1f", stats?.standardDeviation ?: 1.8)
    val phase = stats?.currentPhase ?: CyclePhase.OVULATORY

    val phaseAdvice = getLocalizedPhaseAdvice(phase, language)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ml_insights_card"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ML Engine Status Header
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        color = FollicularPurple.copy(alpha = 0.15f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, FollicularPurple.copy(alpha = 0.3f)),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "ML",
                                tint = FollicularPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.mlTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = strings.mlSubtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // "x % Zuverlässigkeit" in neuer Zeile unterhalb des Modellnamens
                        Surface(
                            color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF064E3B) else Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF059669) else Color(0xFF86EFAC))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF34D399) else Color(0xFF16A34A))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$confidencePct% ${strings.confidenceLabel}",
                                    color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFFD1FAE5) else Color(0xFF14532D),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row with high contrast & clean typography
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricPill(
                        label = strings.avgCycleLabel,
                        value = "$avgLen ${strings.daysUnit}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricPill(
                        label = strings.varianceLabel,
                        value = "±$stdDev ${strings.daysUnit}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricPill(
                        label = strings.nextPeriodLabel,
                        value = stats?.nextPredictedPeriodDate?.substring(5) ?: strings.soonText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Probability Curve Chart
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = RosePrimary.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoGraph,
                                contentDescription = "Chart",
                                tint = RosePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = strings.probCurveTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.probCurveSubtitle,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Canvas Graph
                val displayPredictions = predictions.take(28)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(110.dp)) {
                        if (displayPredictions.size < 2) return@Canvas

                        val w = size.width
                        val h = size.height
                        val stepX = w / (displayPredictions.size - 1)

                        val path = Path()
                        val fillPath = Path()

                        for (i in displayPredictions.indices) {
                            val prob = displayPredictions[i].predictedNextPeriodProbability.toFloat()
                            val x = i * stepX
                            val y = h - (prob * h * 0.88f)

                            if (i == 0) {
                                path.moveTo(x, y)
                                fillPath.moveTo(x, h)
                                fillPath.lineTo(x, y)
                            } else {
                                path.lineTo(x, y)
                            }
                        }

                        fillPath.lineTo(w, h)
                        fillPath.close()

                        // Draw gradient fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(MenstrualRed.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )

                        // Draw stroke
                        drawPath(
                            path = path,
                            color = MenstrualRed,
                            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw peak indicator point
                        val maxIdx = displayPredictions.indices.maxByOrNull {
                            displayPredictions[it].predictedNextPeriodProbability
                        } ?: 0
                        val peakX = maxIdx * stepX
                        val peakY = h - (displayPredictions[maxIdx].predictedNextPeriodProbability.toFloat() * h * 0.88f)

                        drawCircle(
                            color = Color.White,
                            radius = 6.5.dp.toPx(),
                            center = Offset(peakX, peakY)
                        )
                        drawCircle(
                            color = MenstrualRed,
                            radius = 4.5.dp.toPx(),
                            center = Offset(peakX, peakY)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = strings.todayLabel, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = strings.peakOnsetLabel, fontSize = 10.5.sp, color = MenstrualRed, fontWeight = FontWeight.Bold)
                    Text(text = strings.daysFutureLabel, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Phase Health & Wellness Insights
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = OvulationTeal.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = "Tips",
                                tint = OvulationTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = strings.guideTitle,
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                GuideItem(strings.nutritionLabel, phaseAdvice.nutrition, "🥗")
                GuideItem(strings.workoutLabel, phaseAdvice.workout, "🧘")
                GuideItem(strings.energyLabel, phaseAdvice.energy, "✨")
            }
        }

        // New Evidence-Based Nutrition & Movement Suggestion Cards
        PhaseSuggestionsSection(
            currentPhase = phase,
            language = language
        )
    }
}

@Composable
private fun MetricPill(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun GuideItem(category: String, text: String, emoji: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 17.sp)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = category,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = text,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
