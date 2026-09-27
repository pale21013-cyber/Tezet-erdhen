package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.MlPredictionEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RosePrimary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun SegmentedCalendar(
    selectedDate: String,
    cycles: List<CycleEntity>,
    logs: List<DailyLogEntity>,
    predictions: List<MlPredictionEntity>,
    onDateSelect: (String) -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val fmt = DateTimeFormatter.ISO_LOCAL_DATE
    val selectedLocalDate = try {
        LocalDate.parse(selectedDate, fmt)
    } catch (e: Exception) {
        LocalDate.now()
    }

    var currentYearMonth by remember { mutableStateOf(YearMonth.from(selectedLocalDate)) }

    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfWeek = currentYearMonth.atDay(1).dayOfWeek.value % 7 // Sunday = 0, Monday = 1 ...

    // Build fast lookup sets
    val logMap = logs.associateBy { it.logDate }
    val predictionMap = predictions.associateBy { it.predictionDate }

    // Month name translation
    val monthName = when (language) {
        AppLanguage.GERMAN -> when (currentYearMonth.monthValue) {
            1 -> "Januar"; 2 -> "Februar"; 3 -> "März"; 4 -> "April"; 5 -> "Mai"; 6 -> "Juni"
            7 -> "Juli"; 8 -> "August"; 9 -> "September"; 10 -> "Oktober"; 11 -> "November"; else -> "Dezember"
        }
        AppLanguage.ALBANIAN -> when (currentYearMonth.monthValue) {
            1 -> "Janar"; 2 -> "Shkurt"; 3 -> "Mars"; 4 -> "Prill"; 5 -> "Maj"; 6 -> "Qershor"
            7 -> "Korrik"; 8 -> "Gusht"; 9 -> "Shtator"; 10 -> "Tetor"; 11 -> "Nëntor"; else -> "Dhjetor"
        }
        AppLanguage.ENGLISH -> currentYearMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    // Weekday abbreviations
    val weekDays = when (language) {
        AppLanguage.GERMAN -> listOf("S", "M", "D", "M", "D", "F", "S")
        AppLanguage.ALBANIAN -> listOf("D", "H", "M", "M", "E", "P", "SH")
        AppLanguage.ENGLISH -> listOf("S", "M", "T", "W", "T", "F", "S")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("segmented_calendar")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Month Header with Prev / Next Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$monthName ${currentYearMonth.year}",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.2).sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(
                            onClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Month",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(
                            onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Month",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Weekday labels with crisp contrast
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (wd in weekDays) {
                    Text(
                        text = wd,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Days Grid
            val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                for (row in 0 until (totalCells / 7)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (col in 0..6) {
                            val cellIndex = row * 7 + col
                            val dayNum = cellIndex - firstDayOfWeek + 1

                            if (dayNum in 1..daysInMonth) {
                                val cellDate = currentYearMonth.atDay(dayNum)
                                val dateStr = cellDate.format(fmt)
                                val isSelected = dateStr == selectedDate
                                val isToday = cellDate == LocalDate.now()

                                val log = logMap[dateStr]
                                val hasPeriodFlow = (log?.flowIntensity ?: 0) > 0
                                val prediction = predictionMap[dateStr]
                                val isHighProbFuture = (prediction?.predictedNextPeriodProbability ?: 0.0) > 0.40

                                val cellBgColor = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    hasPeriodFlow -> MenstrualRed.copy(alpha = 0.22f)
                                    isHighProbFuture -> MenstrualRed.copy(alpha = 0.12f)
                                    isToday -> MaterialTheme.colorScheme.primaryContainer
                                    else -> Color.Transparent
                                }

                                val textColor = when {
                                    isSelected -> MaterialTheme.colorScheme.onPrimary
                                    hasPeriodFlow -> MenstrualRed
                                    isToday -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(cellBgColor)
                                        .border(
                                            width = if (isSelected) {
                                                0.dp
                                            } else if (isToday) {
                                                1.5.dp
                                            } else if (isHighProbFuture) {
                                                1.dp
                                            } else {
                                                0.5.dp
                                            },
                                            color = when {
                                                isSelected -> Color.Transparent
                                                isToday -> MaterialTheme.colorScheme.primary
                                                isHighProbFuture -> MenstrualRed.copy(alpha = 0.5f)
                                                else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                                            },
                                            shape = RoundedCornerShape(13.dp)
                                        )
                                        .clickable { onDateSelect(dateStr) }
                                        .testTag("cal_cell_$dateStr"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "$dayNum",
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected || isToday || hasPeriodFlow) FontWeight.ExtraBold else FontWeight.Medium,
                                            color = textColor
                                        )

                                        // Status dots (Period / Tag indicator)
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (hasPeriodFlow) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.5.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else MenstrualRed)
                                                )
                                            }
                                            if (log != null && log.isLogged == 1 && !hasPeriodFlow) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.5.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else FollicularPurple)
                                                )
                                            }
                                            if (isHighProbFuture && !hasPeriodFlow) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.5.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else Color(0xFFF43F5E))
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Legend with high contrast labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = MenstrualRed, label = strings.legendPeriod)
                LegendItem(color = MenstrualRed.copy(alpha = 0.4f), label = strings.legendPredicted)
                LegendItem(color = FollicularPurple, label = strings.legendLogged)
                LegendItem(color = RosePrimary, label = strings.legendSelected)
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
