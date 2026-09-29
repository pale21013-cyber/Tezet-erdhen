package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.util.rememberHapticFeedbackManager
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

enum class DayFertilityChance {
    PEAK, // Ovulation day (highest probability: baby can be made!)
    HIGH, // Fertile window (baby can be made!)
    LOW   // Low chance (sex won't lead to a baby)
}

fun calculateFertilityForDate(
    date: LocalDate,
    cycles: List<CycleEntity>
): DayFertilityChance {
    val fmt = DateTimeFormatter.ISO_LOCAL_DATE
    val cycleStarts = cycles.mapNotNull {
        try { LocalDate.parse(it.startDate, fmt) } catch (e: Exception) { null }
    }.sorted()
    val starts = if (cycleStarts.isNotEmpty()) cycleStarts else listOf(LocalDate.now().minusDays(14))
    val avgLen = if (starts.size >= 2) {
        val intervals = (0 until starts.size - 1).map { i ->
            java.time.temporal.ChronoUnit.DAYS.between(starts[i], starts[i + 1]).toInt()
        }.filter { it in 18..45 }
        if (intervals.isNotEmpty()) intervals.average().toInt().coerceIn(21, 38) else 28
    } else {
        28
    }

    val anchor = starts.last()
    val allStarts = mutableSetOf<LocalDate>()
    allStarts.addAll(starts)

    var next = anchor
    while (next.isBefore(date.plusMonths(2))) {
        allStarts.add(next)
        next = next.plusDays(avgLen.toLong())
    }
    var prev = starts.first()
    while (prev.isAfter(date.minusMonths(2))) {
        allStarts.add(prev)
        prev = prev.minusDays(avgLen.toLong())
    }

    for (start in allStarts) {
        val ovulation = start.plusDays((avgLen - 14).toLong())
        val fertileStart = ovulation.minusDays(5)
        val fertileEnd = ovulation.plusDays(1)

        if (date == ovulation) return DayFertilityChance.PEAK
        if (!date.isBefore(fertileStart) && !date.isAfter(fertileEnd)) return DayFertilityChance.HIGH
    }
    return DayFertilityChance.LOW
}

@Composable
fun SegmentedCalendar(
    selectedDate: String,
    cycles: List<CycleEntity>,
    logs: List<DailyLogEntity>,
    predictions: List<MlPredictionEntity>,
    onDateSelect: (String) -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    jumpToTodayTrigger: Long = 0L,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val hapticManager = rememberHapticFeedbackManager()
    val fmt = DateTimeFormatter.ISO_LOCAL_DATE
    val selectedLocalDate = try {
        LocalDate.parse(selectedDate, fmt)
    } catch (e: Exception) {
        LocalDate.now()
    }

    var currentYearMonth by remember { mutableStateOf(YearMonth.from(selectedLocalDate)) }

    androidx.compose.runtime.LaunchedEffect(jumpToTodayTrigger) {
        if (jumpToTodayTrigger > 0L) {
            currentYearMonth = YearMonth.now()
        }
    }

    androidx.compose.runtime.LaunchedEffect(selectedDate) {
        currentYearMonth = YearMonth.from(selectedLocalDate)
    }

    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfWeek = currentYearMonth.atDay(1).dayOfWeek.value % 7 // Sunday = 0, Monday = 1 ...

    // Build fast lookup sets
    val logMap = logs.associateBy { it.logDate }
    val predictionMap = predictions.associateBy { it.predictionDate }

    // Biological cycle anchors and fertility window calculation
    val cycleStarts = remember(cycles) {
        val parsed = cycles.mapNotNull {
            try { LocalDate.parse(it.startDate, fmt) } catch (e: Exception) { null }
        }.sorted()
        if (parsed.isNotEmpty()) parsed else listOf(LocalDate.now().minusDays(14))
    }

    val avgCycleLen = remember(cycleStarts) {
        if (cycleStarts.size >= 2) {
            val intervals = (0 until cycleStarts.size - 1).map { i ->
                java.time.temporal.ChronoUnit.DAYS.between(cycleStarts[i], cycleStarts[i + 1]).toInt()
            }.filter { it in 18..45 }
            if (intervals.isNotEmpty()) intervals.average().toInt().coerceIn(21, 38) else 28
        } else {
            28
        }
    }

    // Fertility Map for every date in view:
    // PEAK = Ovulation day (highest probability: baby can be made!)
    // HIGH = Fertile window (baby can be made!)
    // LOW = Low conception chance (sex won't lead to a baby)
    val fertilityMap = remember(currentYearMonth, cycleStarts, avgCycleLen) {
        val map = mutableMapOf<LocalDate, DayFertilityChance>()
        val firstVisible = currentYearMonth.atDay(1).minusDays(15)
        val lastVisible = currentYearMonth.atEndOfMonth().plusDays(15)

        val anchor = cycleStarts.last()
        val allProjectedStarts = mutableSetOf<LocalDate>()
        allProjectedStarts.addAll(cycleStarts)

        // Project forward
        var next = anchor
        while (next.isBefore(lastVisible.plusMonths(2))) {
            allProjectedStarts.add(next)
            next = next.plusDays(avgCycleLen.toLong())
        }
        // Project backward
        var prev = cycleStarts.first()
        while (prev.isAfter(firstVisible.minusMonths(2))) {
            allProjectedStarts.add(prev)
            prev = prev.minusDays(avgCycleLen.toLong())
        }

        for (start in allProjectedStarts) {
            val ovulation = start.plusDays((avgCycleLen - 14).toLong())
            val fertileStart = ovulation.minusDays(5)
            val fertileEnd = ovulation.plusDays(1)

            var d = fertileStart
            while (!d.isAfter(fertileEnd)) {
                if (d == ovulation) {
                    map[d] = DayFertilityChance.PEAK
                } else {
                    map[d] = DayFertilityChance.HIGH
                }
                d = d.plusDays(1)
            }
        }
        map
    }

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

                                val fertility = fertilityMap[cellDate] ?: DayFertilityChance.LOW
                                val isFertile = fertility == DayFertilityChance.PEAK || fertility == DayFertilityChance.HIGH

                                val cellBgColor = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    hasPeriodFlow -> MenstrualRed.copy(alpha = 0.22f)
                                    fertility == DayFertilityChance.PEAK -> Color(0xFFCCFBF1).copy(alpha = 0.6f)
                                    fertility == DayFertilityChance.HIGH -> Color(0xFFF0FDFA).copy(alpha = 0.6f)
                                    isHighProbFuture -> MenstrualRed.copy(alpha = 0.12f)
                                    isToday -> MaterialTheme.colorScheme.primaryContainer
                                    else -> Color.Transparent
                                }

                                val cellBorderColor = when {
                                    isSelected -> Color.Transparent
                                    isToday -> MaterialTheme.colorScheme.primary
                                    fertility == DayFertilityChance.PEAK -> Color(0xFF0D9488)
                                    fertility == DayFertilityChance.HIGH -> Color(0xFF14B8A6).copy(alpha = 0.5f)
                                    isHighProbFuture -> MenstrualRed.copy(alpha = 0.5f)
                                    hasPeriodFlow -> MenstrualRed.copy(alpha = 0.4f)
                                    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                                }

                                val textColor = when {
                                    isSelected -> MaterialTheme.colorScheme.onPrimary
                                    hasPeriodFlow -> MenstrualRed
                                    fertility == DayFertilityChance.PEAK -> Color(0xFF0F766E)
                                    isToday -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(cellBgColor)
                                        .border(
                                            width = if (isSelected) 0.dp else if (isToday || fertility == DayFertilityChance.PEAK) 1.5.dp else 0.8.dp,
                                            color = cellBorderColor,
                                            shape = RoundedCornerShape(13.dp)
                                        )
                                        .clickable {
                                            hapticManager.performClick()
                                            onDateSelect(dateStr)
                                        }
                                        .testTag("cal_cell_$dateStr"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "$dayNum",
                                            fontSize = 12.5.sp,
                                            fontWeight = if (isSelected || isToday || hasPeriodFlow || isFertile) FontWeight.ExtraBold else FontWeight.Medium,
                                            color = textColor
                                        )

                                        // Status dots & baby icon
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (fertility == DayFertilityChance.PEAK) {
                                                Text(
                                                    text = "👶",
                                                    fontSize = 9.sp,
                                                    lineHeight = 10.sp
                                                )
                                            } else if (fertility == DayFertilityChance.HIGH) {
                                                Text(
                                                    text = "👶",
                                                    fontSize = 8.5.sp,
                                                    lineHeight = 10.sp
                                                )
                                            }
                                            if (hasPeriodFlow) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else MenstrualRed)
                                                )
                                            }
                                            if (log != null && log.isLogged == 1 && !hasPeriodFlow) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else FollicularPurple)
                                                )
                                            }
                                            if (isHighProbFuture && !hasPeriodFlow && !isFertile) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
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

            // Scrollable Calendar Legend with description words
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = MenstrualRed, label = strings.legendPeriod)
                LegendItem(color = MenstrualRed.copy(alpha = 0.4f), label = strings.legendPredicted)
                LegendItem(color = Color(0xFF0D9488), label = strings.legendFertile, emoji = "👶")
                LegendItem(color = Color(0xFF14B8A6), label = strings.legendOvulation, emoji = "👶✨")
                LegendItem(color = Color(0xFF64748B), label = strings.legendLowChance, emoji = "🛡️")
                LegendItem(color = FollicularPurple, label = strings.legendLogged)
                LegendItem(color = RosePrimary, label = strings.legendSelected)
            }

            // Fertility & Conception banner for the selected day
            val selectedFertility = fertilityMap[selectedLocalDate] ?: DayFertilityChance.LOW
            val fertilityBgColor = when (selectedFertility) {
                DayFertilityChance.PEAK -> Color(0xFFCCFBF1)
                DayFertilityChance.HIGH -> Color(0xFFE0F2FE)
                DayFertilityChance.LOW -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
            val fertilityBorderColor = when (selectedFertility) {
                DayFertilityChance.PEAK -> Color(0xFF0D9488)
                DayFertilityChance.HIGH -> Color(0xFF0284C7)
                DayFertilityChance.LOW -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            }
            val fertilityTitle = when (selectedFertility) {
                DayFertilityChance.PEAK -> strings.babyChancePeakTitle
                DayFertilityChance.HIGH -> strings.babyChanceHighTitle
                DayFertilityChance.LOW -> strings.babyChanceLowTitle
            }
            val fertilityDesc = when (selectedFertility) {
                DayFertilityChance.PEAK -> strings.babyChancePeakDesc
                DayFertilityChance.HIGH -> strings.babyChanceHighDesc
                DayFertilityChance.LOW -> strings.babyChanceLowDesc
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = fertilityBgColor,
                border = BorderStroke(1.dp, fertilityBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = when (selectedFertility) {
                                    DayFertilityChance.PEAK -> "👶✨"
                                    DayFertilityChance.HIGH -> "👶"
                                    DayFertilityChance.LOW -> "🛡️"
                                },
                                fontSize = 17.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fertilityTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = fertilityDesc,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, emoji: String? = null) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
        ) {
            if (emoji != null) {
                Text(text = emoji, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
            } else {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
