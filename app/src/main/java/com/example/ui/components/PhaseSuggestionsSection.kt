package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.getScientificPhaseGuide
import com.example.ml.CyclePhase
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.LutealAmber
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.OvulationTeal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhaseSuggestionsSection(
    currentPhase: CyclePhase,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    var selectedTabPhase by remember(currentPhase) { mutableStateOf(currentPhase) }
    val guide = getScientificPhaseGuide(selectedTabPhase, language)
    val phaseColor = Color(guide.colorHex)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("phase_suggestions_section"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header with Phase Switcher Chips
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
                        color = phaseColor.copy(alpha = 0.15f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = "Science",
                                tint = phaseColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.GERMAN) "Evidenzbasierte Empfehlungen" else if (language == AppLanguage.ALBANIAN) "Rekomandime Shkencore" else "Evidence-Based Guide",
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.GERMAN) "Gezielte Ernährungs- & Bewegungsvorschläge je Phase" else if (language == AppLanguage.ALBANIAN) "Këshilla për ushqimin dhe aktivitetin sipas fazës" else "Targeted nutrition & movement per cycle phase",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Phase Switcher Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val phasesList = listOf(
                        Triple(CyclePhase.MENSTRUAL, "🩸", MenstrualRed),
                        Triple(CyclePhase.FOLLICULAR, "💜", FollicularPurple),
                        Triple(CyclePhase.OVULATORY, "🩵", OvulationTeal),
                        Triple(CyclePhase.LUTEAL, "🟠", LutealAmber)
                    )

                    for ((p, emoji, col) in phasesList) {
                        val isSelected = p == selectedTabPhase
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) col.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) col else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedTabPhase = p }
                                .testTag("phase_tab_${p.name.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = p.name.take(3),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    color = if (isSelected) col else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Card 1: Nutrition Suggestions
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("nutrition_suggestion_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Card Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = phaseColor.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = "Nutrition",
                                tint = phaseColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = guide.nutritionTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${guide.emoji} ${guide.phaseTitle} (${guide.dayRangeText})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = phaseColor
                        )
                    }
                }

                // Highlight Chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (chip in guide.nutritionHighlights) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = phaseColor.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "✨ $chip",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = phaseColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Scientific Detailed Items
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    for ((title, detail) in guide.nutritionDetails) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(phaseColor)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = detail,
                                    fontSize = 12.5.sp,
                                    lineHeight = 17.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Metabolism Fact Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Fact",
                            tint = phaseColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = guide.metabolismFact,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Card 2: Activity Suggestions
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("activity_suggestion_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = phaseColor.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Activity",
                                tint = phaseColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = guide.activityTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "⚡ ${guide.activityLevelLabel}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = phaseColor
                        )
                    }
                }

                // Activity Highlights
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (act in guide.activityHighlights) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "🏃 $act",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Activity Details
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    for ((cat, desc) in guide.activityDetails) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = phaseColor,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = desc,
                                    fontSize = 12.5.sp,
                                    lineHeight = 17.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Card 3: Hormones & Physiology Card (Beautified & Normalized)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("hormone_physiology_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = phaseColor.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Hormones",
                                tint = phaseColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = guide.physiologyTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${guide.emoji} ${guide.phaseTitle} • Hormonelle Steuerung",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = phaseColor
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Section 1: Hormonstatus
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                color = phaseColor.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.GERMAN) "🧪 Hormonstatus & Physiologie" else if (language == AppLanguage.ALBANIAN) "🧪 Statusi Hormonal & Fiziologjia" else "🧪 Hormonal Status & Physiology",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = phaseColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = guide.hormonesAndPhysiology,
                                fontSize = 12.5.sp,
                                lineHeight = 17.5.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Divider line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        )

                        // Section 2: Stimmung & Psyche
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                color = phaseColor.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.GERMAN) "🧠 Stimmung, Psyche & Energie" else if (language == AppLanguage.ALBANIAN) "🧠 Humori, Psyche & Energjia" else "🧠 Mood, Psyche & Energy",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = phaseColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = guide.moodAndEnergy,
                                fontSize = 12.5.sp,
                                lineHeight = 17.5.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
