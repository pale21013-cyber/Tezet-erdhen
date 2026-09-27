package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.TagDefinitionEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedTagName
import com.example.ui.theme.FlowBigRed
import com.example.ui.theme.FlowBrown
import com.example.ui.theme.FlowDarkRed
import com.example.ui.theme.FlowOrange
import com.example.ui.theme.FlowPink
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.RosePrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyLogForm(
    selectedDate: String,
    currentLog: DailyLogEntity?,
    selectedTagIds: Set<Long>,
    allTags: List<TagDefinitionEntity>,
    smartDefaults: DailyLogEntity?,
    onApplySmartDefaults: () -> Unit,
    onFlowIntensityChange: (Int, String?, Int) -> Unit,
    onTabletToggle: (Boolean) -> Unit,
    onSleepQualityChange: (Int) -> Unit,
    onActivityLevelChange: (Int) -> Unit,
    onNotesChange: (String) -> Unit,
    onTagToggle: (Long) -> Unit,
    onSaveLog: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val focusManager = LocalFocusManager.current
    val notesFocusRequester = remember { FocusRequester() }

    val flow = currentLog?.flowIntensity ?: 0
    val flowColor = currentLog?.flowColor ?: "Big Red"
    val hasClots = currentLog?.hasClots ?: 0
    val tabletTaken = (currentLog?.tabletTaken ?: 0) == 1
    val sleepQuality = currentLog?.sleepQuality ?: 3
    val activityLevel = currentLog?.activityLevel ?: 1
    val notes = currentLog?.notes ?: ""

    val emotionTags = allTags.filter { it.category == "emotion" }
    val symptomTags = allTags.filter { it.category == "physical_symptom" }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_log_form"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Smart Defaults Banner
        if (smartDefaults != null && currentLog == null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("smart_defaults_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Smart Defaults",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.smartDefaultsTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        val actDesc = if (smartDefaults.activityLevel == 0) strings.actRest else strings.actModerate
                        val pillDesc = if (smartDefaults.tabletTaken == 1) strings.yes else strings.no
                        Text(
                            text = "${strings.smartDefaultsSleep}: ${smartDefaults.sleepQuality}★ • ${strings.smartDefaultsAct}: $actDesc • ${strings.smartDefaultsPill}: $pillDesc",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = onApplySmartDefaults,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("apply_defaults_button")
                    ) {
                        Text(strings.applyBtn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 1. Menstruation Flow Section
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Flow",
                        tint = FlowBigRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.flowTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Flow Intensity Cards: None, Light, Medium, Heavy, Spotting
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val flowOptions = listOf(
                        Pair(0, strings.flowNone),
                        Pair(1, strings.flowLight),
                        Pair(2, strings.flowMedium),
                        Pair(3, strings.flowHeavy),
                        Pair(4, strings.flowSpotting)
                    )

                    for ((optFlow, label) in flowOptions) {
                        val isSelected = flow == optFlow
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) FlowBigRed else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    onFlowIntensityChange(optFlow, flowColor, hasClots)
                                }
                                .testTag("flow_option_$optFlow"),
                            color = if (isSelected) FlowBigRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = when (optFlow) {
                                        0 -> "⚪"
                                        1 -> "💧"
                                        2 -> "🩸"
                                        3 -> "🩸🩸"
                                        else -> "✨"
                                    },
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) FlowBigRed else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // If flow is active, show blood color & clots picker
                AnimatedVisibility(visible = flow > 0) {
                    Column(modifier = Modifier.padding(top = 14.dp)) {
                        Text(
                            text = strings.flowColorTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val colors = listOf(
                                Pair("Pink", FlowPink),
                                Pair("Big Red", FlowBigRed),
                                Pair("Dark Red", FlowDarkRed),
                                Pair("Orange", FlowOrange),
                                Pair("Brown", FlowBrown)
                            )

                            for ((colorName, col) in colors) {
                                val isSelected = flowColor == colorName
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(col)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) Color.Black else Color.White,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            onFlowIntensityChange(flow, colorName, hasClots)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Clots Toggle
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (hasClots == 1) FlowBigRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        onFlowIntensityChange(flow, flowColor, if (hasClots == 1) 0 else 1)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (hasClots == 1) strings.clotsChecked else strings.clotsAdd,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasClots == 1) FlowBigRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Multi-Tagging: Emotional Nuances (core requested feature)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strings.emotionsTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = strings.emotionsSubtitle,
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (tag in emotionTags) {
                        val isSelected = selectedTagIds.contains(tag.tagId)
                        val chipColor = Color(tag.colorHex)
                        val translatedName = getLocalizedTagName(tag.tagName, language)

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) chipColor.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) chipColor else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onTagToggle(tag.tagId) }
                                .testTag("tag_emotion_${tag.tagName}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = tag.emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = translatedName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) chipColor else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = chipColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Physical Symptoms Multi-Tagging
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strings.symptomsTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = strings.symptomsSubtitle,
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (tag in symptomTags) {
                        val isSelected = selectedTagIds.contains(tag.tagId)
                        val chipColor = Color(tag.colorHex)
                        val translatedName = getLocalizedTagName(tag.tagName, language)

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) chipColor.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) chipColor else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onTagToggle(tag.tagId) }
                                .testTag("tag_symptom_${tag.tagName}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = tag.emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = translatedName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) chipColor else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = chipColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Lifestyle & Medication (Tablet, Sleep, Activity)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Tablet taken toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = "Medication",
                        tint = FollicularPurple,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = strings.pillTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = strings.pillSubtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = tabletTaken,
                        onCheckedChange = onTabletToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = FollicularPurple
                        ),
                        modifier = Modifier.testTag("pill_switch")
                    )
                }

                // Sleep Quality
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = "Sleep",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = strings.sleepTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "$sleepQuality / 5 ${strings.sleepStars}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (s in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star $s",
                                tint = if (s <= sleepQuality) Color(0xFFF59E0B) else Color(0xFFE2D6D6),
                                modifier = Modifier
                                    .size(26.dp)
                                    .clickable { onSleepQualityChange(s) }
                            )
                        }
                    }
                }

                // Activity Level
                Column {
                    Text(text = strings.actTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val activityOptions = listOf(
                            Triple(0, strings.actRest, Icons.Default.Hotel),
                            Triple(1, strings.actModerate, Icons.AutoMirrored.Filled.DirectionsWalk),
                            Triple(2, strings.actIntensive, Icons.AutoMirrored.Filled.DirectionsRun)
                        )

                        for ((actLevel, actName, actIcon) in activityOptions) {
                            val isSelected = activityLevel == actLevel
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onActivityLevelChange(actLevel) },
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = actIcon,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = actName,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // Notes Field with fast Keyboard navigation
                OutlinedTextField(
                    value = notes,
                    onValueChange = onNotesChange,
                    label = { Text(strings.notesTitle) },
                    placeholder = { Text(strings.notesPlaceholder) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(notesFocusRequester)
                        .testTag("notes_input"),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onSaveLog()
                    })
                )
            }
        }

        // Save Button (Thumb Reach)
        Button(
            onClick = {
                focusManager.clearFocus()
                onSaveLog()
            },
            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_log_button")
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Save",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = strings.saveLogBtn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
