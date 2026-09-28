package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.TagDefinitionEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedTagName
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RosePrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CalendarDayDetailModal(
    selectedDate: String,
    currentLog: DailyLogEntity?,
    selectedTagIds: Set<Long>,
    allTags: List<TagDefinitionEntity>,
    fertilityChance: DayFertilityChance,
    onFlowIntensityChange: (Int, String?, Int) -> Unit,
    onTabletToggle: (Boolean) -> Unit,
    onSleepQualityChange: (Int) -> Unit,
    onActivityLevelChange: (Int) -> Unit,
    onNotesChange: (String) -> Unit,
    onTagToggle: (Long) -> Unit,
    onSaveLog: () -> Unit,
    onDismiss: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val strings = getAppStrings(language)

    val parsedDate = remember(selectedDate) {
        try {
            LocalDate.parse(selectedDate, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            LocalDate.now()
        }
    }

    val formattedDate = remember(parsedDate, language) {
        val locale = when (language) {
            AppLanguage.GERMAN -> Locale.GERMAN
            AppLanguage.ALBANIAN -> Locale("sq", "AL")
            AppLanguage.ENGLISH -> Locale.ENGLISH
        }
        val pattern = when (language) {
            AppLanguage.GERMAN -> "EEEE, d. MMMM yyyy"
            AppLanguage.ALBANIAN -> "EEEE, d MMMM yyyy"
            AppLanguage.ENGLISH -> "EEEE, MMMM d, yyyy"
        }
        try {
            DateTimeFormatter.ofPattern(pattern, locale).format(parsedDate)
        } catch (e: Exception) {
            parsedDate.toString()
        }
    }

    val isLogged = currentLog != null && currentLog.isLogged == 1

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier.testTag("calendar_day_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Date, Fertility Badge and Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = formattedDate,
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Fertility / Baby Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = when (fertilityChance) {
                            DayFertilityChance.PEAK -> Color(0xFFCCFBF1)
                            DayFertilityChance.HIGH -> Color(0xFFE0F2FE)
                            DayFertilityChance.LOW -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        border = BorderStroke(
                            1.dp,
                            when (fertilityChance) {
                                DayFertilityChance.PEAK -> Color(0xFF0D9488)
                                DayFertilityChance.HIGH -> Color(0xFF0284C7)
                                DayFertilityChance.LOW -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            }
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = when (fertilityChance) {
                                    DayFertilityChance.PEAK -> "👶✨"
                                    DayFertilityChance.HIGH -> "👶"
                                    DayFertilityChance.LOW -> "🛡️"
                                },
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = when (fertilityChance) {
                                    DayFertilityChance.PEAK -> strings.babyChancePeakTitle
                                    DayFertilityChance.HIGH -> strings.babyChanceHighTitle
                                    DayFertilityChance.LOW -> strings.babyChanceLowTitle
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("day_modal_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = strings.dayModalCloseBtn,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // SECTION 1: WHAT IS CURRENTLY SAVED FOR THIS DAY
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = RosePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.dayModalSavedSection,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isLogged) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = strings.dayModalNoEntries,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        val activeTags = allTags.filter { selectedTagIds.contains(it.tagId) }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Flow status
                            val flowIntensity = currentLog?.flowIntensity ?: 0
                            val flowLabel = when (flowIntensity) {
                                1 -> strings.flowLight
                                2 -> strings.flowMedium
                                3 -> strings.flowHeavy
                                4 -> strings.flowSpotting
                                else -> strings.flowNone
                            }
                            SavedDetailRow(
                                icon = "🩸",
                                label = strings.dayModalFlowLabel,
                                value = if (flowIntensity > 0) "$flowLabel (${currentLog?.flowColor ?: "Big Red"}${if ((currentLog?.hasClots ?: 0) == 1) " • Gerinnsel" else ""})" else strings.flowNone,
                                highlightColor = if (flowIntensity > 0) MenstrualRed else null
                            )

                            // Pill status
                            val tabletTaken = (currentLog?.tabletTaken ?: 0) == 1
                            SavedDetailRow(
                                icon = "💊",
                                label = strings.dayModalPillLabel,
                                value = if (tabletTaken) strings.dayModalPillTaken else strings.dayModalPillNotTaken,
                                highlightColor = if (tabletTaken) Color(0xFF0D9488) else null
                            )

                            // Sleep
                            val sleep = currentLog?.sleepQuality ?: 3
                            SavedDetailRow(
                                icon = "😴",
                                label = strings.dayModalSleepLabel,
                                value = "★".repeat(sleep) + "☆".repeat(5 - sleep) + " ($sleep/5)"
                            )

                            // Activity
                            val act = currentLog?.activityLevel ?: 1
                            val actText = when (act) {
                                0 -> strings.actRest
                                1 -> strings.actModerate
                                2 -> strings.actIntensive
                                else -> strings.actModerate
                            }
                            SavedDetailRow(
                                icon = "🏃",
                                label = strings.dayModalActivityLabel,
                                value = actText
                            )

                            // Tags / Symptoms
                            if (activeTags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = strings.dayModalTagsLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (tag in activeTags) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (tag.category == "emotion") FollicularPurple.copy(alpha = 0.2f) else RosePrimary.copy(alpha = 0.15f),
                                            border = BorderStroke(
                                                1.dp,
                                                if (tag.category == "emotion") FollicularPurple.copy(alpha = 0.4f) else RosePrimary.copy(alpha = 0.4f)
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(text = tag.emoji, fontSize = 11.sp)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = getLocalizedTagName(tag.tagName, language),
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Notes
                            val notes = currentLog?.notes
                            if (!notes.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${strings.dayModalNotesLabel}:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = notes,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 2: EDIT OR ADD MORE INFOS FOR THIS DAY
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = RosePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.dayModalEditSection,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    DailyLogForm(
                        selectedDate = selectedDate,
                        currentLog = currentLog,
                        selectedTagIds = selectedTagIds,
                        allTags = allTags,
                        smartDefaults = null,
                        onApplySmartDefaults = {},
                        onFlowIntensityChange = onFlowIntensityChange,
                        onTabletToggle = onTabletToggle,
                        onSleepQualityChange = onSleepQualityChange,
                        onActivityLevelChange = onActivityLevelChange,
                        onNotesChange = onNotesChange,
                        onTagToggle = onTagToggle,
                        onSaveLog = {
                            onSaveLog()
                            onDismiss()
                        },
                        language = language
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedDetailRow(
    icon: String,
    label: String,
    value: String,
    highlightColor: Color? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = highlightColor ?: MaterialTheme.colorScheme.onSurface
        )
    }
}
