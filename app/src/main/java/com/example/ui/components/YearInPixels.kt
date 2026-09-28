package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entities.DailyLogEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RoseDark
import com.example.ui.theme.RosePrimary
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

enum class PixelViewMode {
    MATRIX,
    MONTH_CARDS
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun YearInPixels(
    logs: List<DailyLogEntity>,
    selectedYear: Int = LocalDate.now().year,
    onPixelClick: (String) -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    var currentYear by remember { mutableIntStateOf(selectedYear) }
    var viewMode by remember { mutableStateOf(PixelViewMode.MATRIX) }
    var inspectedDate by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val matrixScrollState = rememberScrollState()
    val monthRequesters = remember { (1..12).associateWith { BringIntoViewRequester() } }

    val locale = when (language) {
        AppLanguage.GERMAN -> Locale.GERMAN
        AppLanguage.ALBANIAN -> Locale.forLanguageTag("sq")
        AppLanguage.ENGLISH -> Locale.ENGLISH
    }

    val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    val logMap = remember(logs) { logs.associateBy { it.logDate } }

    // Year-specific calculations
    val yearLogs = remember(logs, currentYear) {
        logs.filter { it.logDate.startsWith("$currentYear") }
    }
    val periodDaysCount = remember(yearLogs) {
        yearLogs.count { it.flowIntensity > 0 }
    }
    val pillDaysCount = remember(yearLogs) {
        yearLogs.count { it.tabletTaken == 1 }
    }
    val loggedDaysCount = remember(yearLogs) {
        yearLogs.count { it.isLogged == 1 }
    }
    val daysInCurrentYear = if (java.time.Year.of(currentYear).isLeap) 366 else 365
    val trackingCoveragePercent = ((loggedDaysCount.toFloat() / daysInCurrentYear) * 100).toInt().coerceIn(0, 100)

    val inspectedLog = inspectedDate?.let { logMap[it] }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("year_in_pixels_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Header & Year Navigation Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Pixels",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = strings.pixelsTitle,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    // Jump & Highlight Today Button
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (inspectedDate == todayStr && currentYear == LocalDate.now().year)
                            RosePrimary else MaterialTheme.colorScheme.primaryContainer,
                        border = BorderStroke(
                            1.dp,
                            if (inspectedDate == todayStr && currentYear == LocalDate.now().year)
                                RosePrimary else MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                        ),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val now = LocalDate.now()
                                currentYear = now.year
                                inspectedDate = todayStr
                                coroutineScope.launch {
                                    if (viewMode == PixelViewMode.MATRIX) {
                                        val targetOffset = ((now.monthValue - 1) * 35).coerceAtLeast(0)
                                        matrixScrollState.animateScrollTo(targetOffset)
                                    } else {
                                        monthRequesters[now.monthValue]?.bringIntoView()
                                    }
                                }
                            }
                            .testTag("jump_to_today_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Today,
                                contentDescription = "Today",
                                tint = if (inspectedDate == todayStr && currentYear == LocalDate.now().year)
                                    Color.White else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = strings.todayLabel,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                color = if (inspectedDate == todayStr && currentYear == LocalDate.now().year)
                                    Color.White else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Upgraded Aesthetic Year Selector Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { currentYear -= 1 },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Previous Year",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = "$currentYear",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { currentYear += 1 },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Next Year",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick KPI Metric Pills (Feminine & Informative)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Period Days Pill
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MenstrualRed.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, MenstrualRed.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = MenstrualRed,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "$periodDaysCount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MenstrualRed
                                )
                            }
                            Text(
                                text = strings.periodDaysLabel,
                                fontSize = 9.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Logged Days / Coverage Pill
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = FollicularPurple.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, FollicularPurple.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = FollicularPurple,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "$loggedDaysCount d",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = FollicularPurple
                                )
                            }
                            Text(
                                text = "$trackingCoveragePercent% Erfasst",
                                fontSize = 9.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Pill Days
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Medication,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "$pillDaysCount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = strings.pillTitle.take(10),
                                fontSize = 9.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // View Mode Switcher: Matrix vs Month Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (viewMode == PixelViewMode.MATRIX) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (viewMode == PixelViewMode.MATRIX) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewMode = PixelViewMode.MATRIX }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = if (viewMode == PixelViewMode.MATRIX) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "12-Monate Matrix",
                                fontSize = 12.sp,
                                fontWeight = if (viewMode == PixelViewMode.MATRIX) FontWeight.Bold else FontWeight.Medium,
                                color = if (viewMode == PixelViewMode.MATRIX) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (viewMode == PixelViewMode.MONTH_CARDS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (viewMode == PixelViewMode.MONTH_CARDS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewMode = PixelViewMode.MONTH_CARDS }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ViewModule,
                                contentDescription = null,
                                tint = if (viewMode == PixelViewMode.MONTH_CARDS) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Monatskarten 🌸",
                                fontSize = 12.sp,
                                fontWeight = if (viewMode == PixelViewMode.MONTH_CARDS) FontWeight.Bold else FontWeight.Medium,
                                color = if (viewMode == PixelViewMode.MONTH_CARDS) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // 2. Inspected Day Drawer (If a pixel is selected)
        AnimatedVisibility(
            visible = inspectedDate != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            inspectedDate?.let { date ->
                val log = inspectedLog
                val hasFlow = (log?.flowIntensity ?: 0) > 0
                val parsedDate = try { LocalDate.parse(date) } catch (e: Exception) { null }
                val formattedDate = parsedDate?.let {
                    val dayName = it.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
                    val monthName = it.month.getDisplayName(TextStyle.FULL, locale)
                    "$dayName, ${it.dayOfMonth}. $monthName $currentYear"
                } ?: date

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth().animateContentSize()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = if (hasFlow) MenstrualRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (hasFlow) Icons.Default.WaterDrop else Icons.Default.Spa,
                                            contentDescription = null,
                                            tint = if (hasFlow) MenstrualRed else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = formattedDate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (hasFlow) "${strings.flowTitle}: ${getFlowIntensityLabel(log?.flowIntensity ?: 0, strings)}" else if (log?.isLogged == 1) strings.pixelsLegendMood else strings.pixelsLegendEmpty,
                                        fontSize = 11.5.sp,
                                        color = if (hasFlow) MenstrualRed else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(
                                onClick = { inspectedDate = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Detailed badges for inspected log
                        if (log != null && log.isLogged == 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (log.tabletTaken == 1) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = "💊 ${strings.pillTitle}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                if (log.sleepQuality in 1..5) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = "🌙 ${log.sleepQuality}★ ${strings.sleepTitle}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                if (log.hasClots == 1) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MenstrualRed.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = strings.clotsChecked,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MenstrualRed,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                if (!log.notes.isNullOrBlank()) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = "📝 \"${log.notes.take(30)}...\"",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Jump to Day button
                        Button(
                            onClick = {
                                onPixelClick(date)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth().testTag("open_inspected_day_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${strings.trackButton} • $formattedDate",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 3. Main Pixels Content (Matrix View or Month Cards View)
        if (viewMode == PixelViewMode.MATRIX) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("pixel_matrix_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header with title and scroll prompt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "12-Monats-Matrix $currentYear",
                                fontFamily = OutfitDisplayFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "← Horizontal wischen für alle Monate →",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "365 Tage",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val monthAbbrs = (1..12).map { monthIdx ->
                        YearMonth.of(currentYear, monthIdx).month.getDisplayName(TextStyle.SHORT, locale).take(3)
                    }

                    // Outer Table: Fixed Sticky Day Column on the left + Horizontally Scrollable 12 Month columns
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // PINNED STICKY DAY COLUMN (1..31) - Stays fixed and always visible!
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // "Tag / Day" header matching month header height
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(width = 30.dp, height = 28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "Tag",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                for (day in 1..31) {
                                    val isMilestone = day % 5 == 0
                                    Surface(
                                        shape = RoundedCornerShape(5.dp),
                                        color = if (isMilestone) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) else Color.Transparent,
                                        modifier = Modifier.size(width = 30.dp, height = 24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$day",
                                                fontSize = 10.5.sp,
                                                fontWeight = if (isMilestone) FontWeight.ExtraBold else FontWeight.Medium,
                                                color = if (isMilestone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // HORIZONTALLY SCROLLABLE 12 MONTH COLUMNS
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .horizontalScroll(matrixScrollState)
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            for (monthIndex in 1..12) {
                                val ym = YearMonth.of(currentYear, monthIndex)
                                val daysInThisMonth = ym.lengthOfMonth()

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    // Month header label pill (32.dp x 28.dp)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (monthIndex == LocalDate.now().monthValue && currentYear == LocalDate.now().year)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant,
                                        border = BorderStroke(
                                            1.dp,
                                            if (monthIndex == LocalDate.now().monthValue && currentYear == LocalDate.now().year)
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                            else
                                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                                        ),
                                        modifier = Modifier.size(width = 32.dp, height = 28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = monthAbbrs[monthIndex - 1],
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (monthIndex == LocalDate.now().monthValue && currentYear == LocalDate.now().year)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    // 31 Day pixels (32.dp x 24.dp)
                                    for (day in 1..31) {
                                        if (day <= daysInThisMonth) {
                                            val dateStr = String.format("%04d-%02d-%02d", currentYear, monthIndex, day)
                                            val log = logMap[dateStr]
                                            val flow = log?.flowIntensity ?: 0
                                            val isLogged = (log?.isLogged ?: 0) == 1
                                            val isToday = dateStr == todayStr
                                            val isInspected = dateStr == inspectedDate

                                            val pixelColor = getPixelColor(flow, isLogged)

                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = pixelColor,
                                                border = if (isInspected) {
                                                    BorderStroke(2.dp, RosePrimary)
                                                } else if (isToday) {
                                                    BorderStroke(2.dp, Color(0xFFF59E0B))
                                                } else {
                                                    BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                                                },
                                                shadowElevation = if (isInspected) 3.dp else 0.dp,
                                                modifier = Modifier
                                                    .size(width = 32.dp, height = 24.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .clickable {
                                                        inspectedDate = dateStr
                                                    }
                                                    .testTag("pixel_$dateStr")
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    if (isToday) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(5.dp)
                                                                .clip(CircleShape)
                                                                .background(Color(0xFFF59E0B))
                                                        )
                                                    } else if (flow > 0) {
                                                        Icon(
                                                            imageVector = Icons.Default.WaterDrop,
                                                            contentDescription = null,
                                                            tint = Color.White.copy(alpha = 0.9f),
                                                            modifier = Modifier.size(11.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            // Blank placeholder for months with < 31 days
                                            Box(
                                                modifier = Modifier
                                                    .size(width = 32.dp, height = 24.dp)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                                                        shape = RoundedCornerShape(6.dp)
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Rich Gradient Legend
                    PixelLegendSection(strings = strings)
                }
            }
        } else {
            // Month-Cards View (12 Aesthetic mini monthly cards)
            Column(
                modifier = Modifier.fillMaxWidth().testTag("pixel_month_cards_view"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (monthIndex in 1..12) {
                    val ym = YearMonth.of(currentYear, monthIndex)
                    val daysInMonth = ym.lengthOfMonth()
                    val monthName = ym.month.getDisplayName(TextStyle.FULL, locale)
                    val monthPeriodDays = (1..daysInMonth).count { day ->
                        val dateStr = String.format("%04d-%02d-%02d", currentYear, monthIndex, day)
                        (logMap[dateStr]?.flowIntensity ?: 0) > 0
                    }
                    val requester = monthRequesters[monthIndex]
                    val isCurrentCalendarMonth = currentYear == LocalDate.now().year && monthIndex == LocalDate.now().monthValue

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrentCalendarMonth && inspectedDate == todayStr)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.22f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(18.dp),
                        border = if (isCurrentCalendarMonth && inspectedDate == todayStr)
                            BorderStroke(1.5.dp, RosePrimary.copy(alpha = 0.65f))
                        else
                            BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (requester != null) Modifier.bringIntoViewRequester(requester) else Modifier)
                            .testTag("pixel_month_card_$monthIndex")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = monthName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "$currentYear",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (monthPeriodDays > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MenstrualRed.copy(alpha = 0.12f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.WaterDrop,
                                                contentDescription = null,
                                                tint = MenstrualRed,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "$monthPeriodDays ${strings.periodDaysLabel}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MenstrualRed
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val monthWeekDays = when (language) {
                                AppLanguage.GERMAN -> listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So")
                                AppLanguage.ALBANIAN -> listOf("Hën", "Mar", "Mër", "Enj", "Pre", "Sht", "Die")
                                AppLanguage.ENGLISH -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            }

                            // Weekday Header (7 columns starting with Monday)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                monthWeekDays.forEach { dayName ->
                                    Text(
                                        text = dayName,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // 7-column calendar grid for this month (Monday as Day 1)
                            val firstDayOfWeek = ym.atDay(1).dayOfWeek.value // 1 (Mon) .. 7 (Sun)
                            val leadingEmptyDays = firstDayOfWeek - 1 // 0 for Monday, 6 for Sunday
                            val totalSlots = leadingEmptyDays + daysInMonth
                            val numRows = (totalSlots + 6) / 7

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                for (rowIndex in 0 until numRows) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        for (colIndex in 0..6) {
                                            val slotIndex = rowIndex * 7 + colIndex
                                            val day = slotIndex - leadingEmptyDays + 1

                                            if (day in 1..daysInMonth) {
                                                val dateStr = String.format("%04d-%02d-%02d", currentYear, monthIndex, day)
                                                val log = logMap[dateStr]
                                                val flow = log?.flowIntensity ?: 0
                                                val isLogged = (log?.isLogged ?: 0) == 1
                                                val isToday = dateStr == todayStr
                                                val isInspected = dateStr == inspectedDate

                                                val pixelColor = getPixelColor(flow, isLogged)

                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = pixelColor,
                                                    border = if (isInspected) {
                                                        BorderStroke(2.dp, RosePrimary)
                                                    } else if (isToday) {
                                                        BorderStroke(1.5.dp, Color(0xFFF59E0B))
                                                    } else {
                                                        BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                                    },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .clickable { inspectedDate = dateStr }
                                                        .testTag("pixel_day_${dateStr}")
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text(
                                                            text = "$day",
                                                            fontSize = 11.sp,
                                                            fontWeight = if (flow > 0 || isToday || isInspected) FontWeight.Bold else FontWeight.Medium,
                                                            color = if (flow > 0) Color.White else MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                }
                                            } else {
                                                Spacer(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PixelLegendSection(strings: com.example.localization.StringsBundle) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Legende & Farb-Intensität",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            LegendItem(
                color = RoseDark,
                label = "${strings.flowHeavy} / ${strings.flowMedium}"
            )
            LegendItem(
                color = Color(0xFFFB7185),
                label = strings.flowLight
            )
            LegendItem(
                color = Color(0xFFFDA4AF),
                label = strings.flowSpotting
            )
            LegendItem(
                color = FollicularPurple,
                label = strings.pixelsLegendMood
            )
            LegendItem(
                color = Color(0xFFF59E0B),
                label = "Heute (Gold)",
                isRing = true
            )
            LegendItem(
                color = MaterialTheme.colorScheme.surfaceVariant,
                label = strings.pixelsLegendEmpty
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    isRing: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isRing) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.5.dp, color, RoundedCornerShape(3.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun getPixelColor(flowIntensity: Int, isLogged: Boolean): Color {
    return when (flowIntensity) {
        3 -> RoseDark                  // Heavy: Deep Crimson
        2 -> RosePrimary               // Medium: Vibrant Rose
        1 -> Color(0xFFFB7185)         // Light: Soft Coral Rose
        4 -> Color(0xFFFDA4AF)         // Spotting: Soft Blush
        else -> if (isLogged) {
            FollicularPurple.copy(alpha = 0.8f) // Mood/Wellness Logged
        } else {
            MaterialTheme.colorScheme.surfaceVariant // Empty tile
        }
    }
}

private fun getFlowIntensityLabel(
    flowIntensity: Int,
    strings: com.example.localization.StringsBundle
): String {
    return when (flowIntensity) {
        1 -> strings.flowLight
        2 -> strings.flowMedium
        3 -> strings.flowHeavy
        4 -> strings.flowSpotting
        else -> strings.flowNone
    }
}
