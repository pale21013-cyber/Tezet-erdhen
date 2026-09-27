package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.entities.DailyLogEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.RosePrimary
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun YearInPixels(
    logs: List<DailyLogEntity>,
    selectedYear: Int = LocalDate.now().year,
    onPixelClick: (String) -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val monthLetters = listOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
    val logMap = logs.associateBy { it.logDate }
    var inspectInfo by remember { mutableStateOf<String?>(null) }

    // Count period days in year
    val periodDaysCount = logs.count {
        it.flowIntensity > 0 && it.logDate.startsWith("$selectedYear")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("year_in_pixels_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = strings.pixelsTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$selectedYear ${strings.pixelsSubtitle}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = MenstrualRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$periodDaysCount ${strings.periodDaysLabel}",
                        color = MenstrualRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (inspectInfo != null) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = inspectInfo ?: "",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Matrix: Scrollable horizontally if screen is compact
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Center
            ) {
                // Day Numbers column (1..31)
                Column(
                    modifier = Modifier.padding(end = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Empty space for header alignment
                    Text(text = " ", fontSize = 11.sp, modifier = Modifier.height(18.dp))

                    for (day in 1..31) {
                        Text(
                            text = "$day",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .size(width = 16.dp, height = 12.dp)
                        )
                    }
                }

                // 12 Month Columns
                for (monthIndex in 1..12) {
                    val ym = YearMonth.of(selectedYear, monthIndex)
                    val daysInThisMonth = ym.lengthOfMonth()

                    Column(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // Month header letter
                        Text(
                            text = monthLetters[monthIndex - 1],
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.height(18.dp)
                        )

                        // 31 Day pixels for this month
                        for (day in 1..31) {
                            if (day <= daysInThisMonth) {
                                val dateStr = String.format("%04d-%02d-%02d", selectedYear, monthIndex, day)
                                val log = logMap[dateStr]
                                val hasFlow = (log?.flowIntensity ?: 0) > 0
                                val isLogged = (log?.isLogged ?: 0) == 1

                                val emptyPixelColor = MaterialTheme.colorScheme.outlineVariant
                                val pixelColor = when {
                                    hasFlow -> MenstrualRed
                                    isLogged -> FollicularPurple.copy(alpha = 0.5f)
                                    else -> emptyPixelColor
                                }

                                Box(
                                    modifier = Modifier
                                        .size(14.dp, 12.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(pixelColor)
                                        .clickable {
                                            inspectInfo = "$dateStr: ${if (hasFlow) "${strings.pixelsLegendFlow} (${log?.flowIntensity})" else if (isLogged) strings.pixelsLegendMood else strings.pixelsLegendEmpty}"
                                            onPixelClick(dateStr)
                                        }
                                        .testTag("pixel_$dateStr")
                                )
                            } else {
                                Box(modifier = Modifier.size(14.dp, 12.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(MenstrualRed))
                    Text(text = " ${strings.pixelsLegendFlow}", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(FollicularPurple.copy(alpha = 0.5f)))
                    Text(text = " ${strings.pixelsLegendMood}", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(MaterialTheme.colorScheme.outlineVariant))
                    Text(text = " ${strings.pixelsLegendEmpty}", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
