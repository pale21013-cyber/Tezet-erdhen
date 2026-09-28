package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import android.app.Activity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.ui.components.CalendarDayDetailModal
import com.example.ui.components.CycleWheel
import com.example.ui.components.DailyLogForm
import com.example.ui.components.MlInsightsCard
import com.example.ui.components.OnboardingScreen
import com.example.ui.components.SegmentedCalendar
import com.example.ui.components.SettingsSheet
import com.example.ui.components.YearInPixels
import com.example.ui.components.calculateFertilityForDate
import com.example.ui.theme.RosePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraApp(
    viewModel: CycleViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val strings = getAppStrings(uiState.language)
    val context = LocalContext.current

    val currentMonthName = remember(uiState.language) {
        val currentMonthVal = java.time.LocalDate.now().monthValue
        when (uiState.language) {
            AppLanguage.GERMAN -> when (currentMonthVal) {
                1 -> "Januar"; 2 -> "Februar"; 3 -> "März"; 4 -> "April"; 5 -> "Mai"; 6 -> "Juni"
                7 -> "Juli"; 8 -> "August"; 9 -> "September"; 10 -> "Oktober"; 11 -> "November"; else -> "Dezember"
            }
            AppLanguage.ALBANIAN -> when (currentMonthVal) {
                1 -> "Janar"; 2 -> "Shkurt"; 3 -> "Mars"; 4 -> "Prill"; 5 -> "Maj"; 6 -> "Qershor"
                7 -> "Korrik"; 8 -> "Gusht"; 9 -> "Shtator"; 10 -> "Tetor"; 11 -> "Nëntor"; else -> "Dhjetor"
            }
            AppLanguage.ENGLISH -> java.time.LocalDate.now().month.name.lowercase().replaceFirstChar { it.uppercase() }
        }
    }

    LaunchedEffect(uiState.saveNotification) {
        uiState.saveNotification?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissNotification()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (!uiState.isOnboardingCompleted) {
            // Initial Full-Screen Onboarding Flow
            OnboardingScreen(
                currentLanguage = uiState.language,
                onLanguageChange = { viewModel.setLanguage(it) },
                currentThemeSetting = uiState.themeSetting,
                onThemeSettingChange = { viewModel.setThemeSetting(it) },
                allTags = uiState.allTags,
                onCompleteOnboarding = { goal, lastPeriodDate, cycleLen, periodDur, isRegular, selectedTags ->
                    viewModel.completeOnboarding(
                        goal = goal,
                        lastPeriodDate = lastPeriodDate,
                        cycleLength = cycleLen,
                        periodDuration = periodDur,
                        isRegular = isRegular,
                        selectedTagIds = selectedTags
                    )
                }
            )
        } else {
            // Main App Scaffold
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isExpanded = maxWidth >= 840.dp

            if (isExpanded) {
                // Expanded Screen: Tablet / Desktop Canonical Layout (Navigation Rail + Dual Pane)
                Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("nav_rail")
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4E6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Aura",
                                tint = RosePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        NavigationRailItem(
                            selected = uiState.currentTab == AppTab.TODAY,
                            onClick = { viewModel.selectTab(AppTab.TODAY) },
                            icon = { Icon(Icons.Default.Spa, contentDescription = strings.tabToday) },
                            label = { Text(strings.tabToday) }
                        )
                        NavigationRailItem(
                            selected = uiState.currentTab == AppTab.CALENDAR,
                            onClick = { viewModel.selectTab(AppTab.CALENDAR) },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = strings.tabCalendar) },
                            label = { Text(strings.tabCalendar) }
                        )
                        NavigationRailItem(
                            selected = uiState.currentTab == AppTab.PIXELS,
                            onClick = { viewModel.selectTab(AppTab.PIXELS) },
                            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = strings.tabPixels) },
                            label = { Text(strings.tabPixels) }
                        )
                        NavigationRailItem(
                            selected = uiState.currentTab == AppTab.INSIGHTS,
                            onClick = { viewModel.selectTab(AppTab.INSIGHTS) },
                            icon = { Icon(Icons.Default.SelfImprovement, contentDescription = strings.tabInsights) },
                            label = { Text(strings.tabInsights) }
                        )
                        NavigationRailItem(
                            selected = uiState.currentTab == AppTab.SETTINGS,
                            onClick = { viewModel.selectTab(AppTab.SETTINGS) },
                            icon = { Icon(Icons.Default.Settings, contentDescription = strings.tabSettings) },
                            label = { Text(strings.tabSettings) }
                        )
                    }

                    // Dual Pane Content
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Left Pane: Combined Cycle Wheel & Vertical Thumb Form
                        Column(
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxHeight()
                                .imePadding()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            CycleWheel(
                                stats = uiState.cycleStats,
                                onTrackClick = { viewModel.selectTab(AppTab.TODAY) },
                                language = uiState.language
                            )

                            DailyLogForm(
                                selectedDate = uiState.selectedDate,
                                currentLog = uiState.selectedDateLog,
                                selectedTagIds = uiState.selectedTagIds,
                                allTags = uiState.allTags,
                                smartDefaults = uiState.smartDefaults,
                                onApplySmartDefaults = { viewModel.applySmartDefaults() },
                                onFlowIntensityChange = { intensity, color, clots ->
                                    viewModel.updateFlowIntensity(intensity, color, clots)
                                },
                                onTabletToggle = { viewModel.toggleTablet(it) },
                                onSleepQualityChange = { viewModel.updateSleepQuality(it) },
                                onActivityLevelChange = { viewModel.updateActivityLevel(it) },
                                onNotesChange = { viewModel.updateNotes(it) },
                                onTagToggle = { viewModel.toggleTag(it) },
                                onSaveLog = { viewModel.saveCurrentLog() },
                                language = uiState.language
                            )
                        }

                        // Right Pane: Segmented Calendar, Year in Pixels, ML Insights, or Security
                        Column(
                            modifier = Modifier
                                .weight(1.2f)
                                .fillMaxHeight()
                                .imePadding()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            when (uiState.currentTab) {
                                AppTab.TODAY -> {
                                    SegmentedCalendar(
                                        selectedDate = uiState.selectedDate,
                                        cycles = uiState.allCycles,
                                        logs = uiState.allLogs,
                                        predictions = uiState.predictions,
                                        onDateSelect = { viewModel.selectDate(it) },
                                        language = uiState.language,
                                        jumpToTodayTrigger = uiState.jumpToTodayTrigger
                                    )
                                    MlInsightsCard(
                                        stats = uiState.cycleStats,
                                        predictions = uiState.predictions,
                                        language = uiState.language
                                    )
                                }
                                 AppTab.CALENDAR -> {
                                    SegmentedCalendar(
                                        selectedDate = uiState.selectedDate,
                                        cycles = uiState.allCycles,
                                        logs = uiState.allLogs,
                                        predictions = uiState.predictions,
                                        onDateSelect = { viewModel.openCalendarModal(it) },
                                        language = uiState.language,
                                        jumpToTodayTrigger = uiState.jumpToTodayTrigger
                                    )
                                }
                                AppTab.PIXELS -> {
                                    YearInPixels(
                                        logs = uiState.allLogs,
                                        onPixelClick = { 
                                            viewModel.openCalendarModal(it)
                                            viewModel.selectTab(AppTab.CALENDAR)
                                        },
                                        language = uiState.language,
                                        jumpToTodayTrigger = uiState.jumpToTodayTrigger
                                    )
                                }
                                AppTab.INSIGHTS -> {
                                    MlInsightsCard(
                                        stats = uiState.cycleStats,
                                        predictions = uiState.predictions,
                                        language = uiState.language
                                    )
                                }
                                AppTab.SETTINGS -> {
                                    SettingsSheet(
                                        currentLanguage = uiState.language,
                                        onLanguageChange = { viewModel.setLanguage(it) },
                                        currentThemeSetting = uiState.themeSetting,
                                        onThemeSettingChange = { viewModel.setThemeSetting(it) },
                                        onFastMoodLog = { viewModel.logFastMood(it) },
                                        onResetDemoData = { viewModel.resetDemoData() },
                                        onReplayOnboarding = { viewModel.replayOnboarding() },
                                        onExportBackup = { viewModel.shareBackup() },
                                        onImportBackup = { viewModel.importData(it) },
                                        targetRepository = uiState.targetRepository,
                                        onTargetRepositoryChange = { viewModel.setUpdateTargetRepo(it) },
                                        updateStatus = uiState.updateStatus,
                                        onCheckForUpdates = { viewModel.checkForUpdates() },
                                        onDownloadAndInstallUpdate = { viewModel.downloadAndInstallUpdate(context as? Activity) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Compact (Phones e.g. Samsung Galaxy S21 Ultra) & Medium (Foldables)
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = "🌸", fontSize = 17.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = strings.appName,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 19.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            letterSpacing = (-0.3).sp
                                        )
                                    }
                                }
                            },
                            actions = {
                                if (uiState.currentTab == AppTab.CALENDAR || uiState.currentTab == AppTab.PIXELS) {
                                    // Today-Button showing current month
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(16.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, RosePrimary.copy(alpha = 0.4f)),
                                        shadowElevation = 2.dp,
                                        modifier = Modifier
                                            .padding(end = 14.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable {
                                                viewModel.jumpToToday()
                                            }
                                            .testTag("top_bar_today_button")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CalendarToday,
                                                contentDescription = "Today",
                                                tint = RosePrimary,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "${strings.tabToday} • $currentMonthName",
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(32.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                                tonalElevation = 8.dp,
                                shadowElevation = 12.dp,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.2.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("floating_bottom_bar")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FloatingBottomNavItem(
                                        selected = uiState.currentTab == AppTab.TODAY,
                                        onClick = { viewModel.selectTab(AppTab.TODAY) },
                                        icon = Icons.Default.Spa,
                                        label = strings.tabToday
                                    )
                                    FloatingBottomNavItem(
                                        selected = uiState.currentTab == AppTab.CALENDAR,
                                        onClick = { viewModel.selectTab(AppTab.CALENDAR) },
                                        icon = Icons.Default.CalendarMonth,
                                        label = strings.tabCalendar
                                    )
                                    FloatingBottomNavItem(
                                        selected = uiState.currentTab == AppTab.PIXELS,
                                        onClick = { viewModel.selectTab(AppTab.PIXELS) },
                                        icon = Icons.Default.AutoAwesome,
                                        label = strings.tabPixels
                                    )
                                    FloatingBottomNavItem(
                                        selected = uiState.currentTab == AppTab.INSIGHTS,
                                        onClick = { viewModel.selectTab(AppTab.INSIGHTS) },
                                        icon = Icons.Default.SelfImprovement,
                                        label = strings.tabInsights
                                    )
                                    FloatingBottomNavItem(
                                        selected = uiState.currentTab == AppTab.SETTINGS,
                                        onClick = { viewModel.selectTab(AppTab.SETTINGS) },
                                        icon = Icons.Default.Settings,
                                        label = strings.tabSettings
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .imePadding()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedContent(
                            targetState = uiState.currentTab,
                            transitionSpec = {
                                fadeIn() + slideInHorizontally { width -> if (targetState.ordinal > initialState.ordinal) width / 4 else -width / 4 } togetherWith
                                        fadeOut() + slideOutHorizontally { width -> if (targetState.ordinal > initialState.ordinal) -width / 4 else width / 4 }
                            },
                            label = "tab_transition"
                        ) { targetTab ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                when (targetTab) {
                                    AppTab.TODAY -> {
                                        // Combined Dashboard: Circular Cycle Wheel + Vertical Thumb Form with Smart Defaults
                                        CycleWheel(
                                            stats = uiState.cycleStats,
                                            onTrackClick = { /* focus or scroll form */ },
                                            language = uiState.language,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )

                                        DailyLogForm(
                                            selectedDate = uiState.selectedDate,
                                            currentLog = uiState.selectedDateLog,
                                            selectedTagIds = uiState.selectedTagIds,
                                            allTags = uiState.allTags,
                                            smartDefaults = uiState.smartDefaults,
                                            onApplySmartDefaults = { viewModel.applySmartDefaults() },
                                            onFlowIntensityChange = { intensity, color, clots ->
                                                viewModel.updateFlowIntensity(intensity, color, clots)
                                            },
                                            onTabletToggle = { viewModel.toggleTablet(it) },
                                            onSleepQualityChange = { viewModel.updateSleepQuality(it) },
                                            onActivityLevelChange = { viewModel.updateActivityLevel(it) },
                                            onNotesChange = { viewModel.updateNotes(it) },
                                            onTagToggle = { viewModel.toggleTag(it) },
                                            onSaveLog = { viewModel.saveCurrentLog() },
                                            language = uiState.language
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                    AppTab.CALENDAR -> {
                                        SegmentedCalendar(
                                            selectedDate = uiState.selectedDate,
                                            cycles = uiState.allCycles,
                                            logs = uiState.allLogs,
                                            predictions = uiState.predictions,
                                            onDateSelect = { viewModel.openCalendarModal(it) },
                                            language = uiState.language,
                                            jumpToTodayTrigger = uiState.jumpToTodayTrigger,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        // Also show the log form for the selected date!
                                        DailyLogForm(
                                            selectedDate = uiState.selectedDate,
                                            currentLog = uiState.selectedDateLog,
                                            selectedTagIds = uiState.selectedTagIds,
                                            allTags = uiState.allTags,
                                            smartDefaults = uiState.smartDefaults,
                                            onApplySmartDefaults = { viewModel.applySmartDefaults() },
                                            onFlowIntensityChange = { intensity, color, clots ->
                                                viewModel.updateFlowIntensity(intensity, color, clots)
                                            },
                                            onTabletToggle = { viewModel.toggleTablet(it) },
                                            onSleepQualityChange = { viewModel.updateSleepQuality(it) },
                                            onActivityLevelChange = { viewModel.updateActivityLevel(it) },
                                            onNotesChange = { viewModel.updateNotes(it) },
                                            onTagToggle = { viewModel.toggleTag(it) },
                                            onSaveLog = { viewModel.saveCurrentLog() },
                                            language = uiState.language
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                    AppTab.PIXELS -> {
                                        YearInPixels(
                                            logs = uiState.allLogs,
                                            onPixelClick = {
                                                viewModel.openCalendarModal(it)
                                                viewModel.selectTab(AppTab.CALENDAR)
                                            },
                                            language = uiState.language,
                                            jumpToTodayTrigger = uiState.jumpToTodayTrigger,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                    AppTab.INSIGHTS -> {
                                        MlInsightsCard(
                                            stats = uiState.cycleStats,
                                            predictions = uiState.predictions,
                                            language = uiState.language,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                    AppTab.SETTINGS -> {
                                        SettingsSheet(
                                            currentLanguage = uiState.language,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            currentThemeSetting = uiState.themeSetting,
                                            onThemeSettingChange = { viewModel.setThemeSetting(it) },
                                            onFastMoodLog = { viewModel.logFastMood(it) },
                                            onResetDemoData = { viewModel.resetDemoData() },
                                            onReplayOnboarding = { viewModel.replayOnboarding() },
                                            onExportBackup = { viewModel.shareBackup() },
                                            onImportBackup = { viewModel.importData(it) },
                                            targetRepository = uiState.targetRepository,
                                            onTargetRepositoryChange = { viewModel.setUpdateTargetRepo(it) },
                                            updateStatus = uiState.updateStatus,
                                            onCheckForUpdates = { viewModel.checkForUpdates() },
                                            onDownloadAndInstallUpdate = { viewModel.downloadAndInstallUpdate(context as? Activity) },
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

        // Calendar Day Inspector & Edit Modal
        if (uiState.isCalendarModalVisible) {
            val selectedDateFertility = remember(uiState.selectedDate, uiState.allCycles) {
                val parsed = try {
                    java.time.LocalDate.parse(uiState.selectedDate, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (e: Exception) {
                    java.time.LocalDate.now()
                }
                calculateFertilityForDate(parsed, uiState.allCycles)
            }

            CalendarDayDetailModal(
                selectedDate = uiState.selectedDate,
                currentLog = uiState.selectedDateLog,
                selectedTagIds = uiState.selectedTagIds,
                allTags = uiState.allTags,
                fertilityChance = selectedDateFertility,
                onFlowIntensityChange = { intensity, color, clots ->
                    viewModel.updateFlowIntensity(intensity, color, clots)
                },
                onTabletToggle = { viewModel.toggleTablet(it) },
                onSleepQualityChange = { viewModel.updateSleepQuality(it) },
                onActivityLevelChange = { viewModel.updateActivityLevel(it) },
                onNotesChange = { viewModel.updateNotes(it) },
                onTagToggle = { viewModel.toggleTag(it) },
                onSaveLog = { viewModel.saveCurrentLog() },
                onDismiss = { viewModel.closeCalendarModal() },
                language = uiState.language
            )
        }
    }
}

@Composable
private fun FloatingBottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) RosePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (selected) RosePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
