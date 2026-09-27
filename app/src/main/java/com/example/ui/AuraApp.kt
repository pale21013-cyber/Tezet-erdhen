package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import com.example.localization.getAppStrings
import com.example.ui.components.BiometricLockOverlay
import com.example.ui.components.CycleWheel
import com.example.ui.components.DailyLogForm
import com.example.ui.components.MlInsightsCard
import com.example.ui.components.SegmentedCalendar
import com.example.ui.components.SettingsSheet
import com.example.ui.components.YearInPixels
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

    LaunchedEffect(uiState.saveNotification) {
        uiState.saveNotification?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissNotification()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
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
                                        language = uiState.language
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
                                        onDateSelect = { viewModel.selectDate(it) },
                                        language = uiState.language
                                    )
                                }
                                AppTab.PIXELS -> {
                                    YearInPixels(
                                        logs = uiState.allLogs,
                                        onPixelClick = { viewModel.selectDate(it) },
                                        language = uiState.language
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
                                        isBiometricEnabled = uiState.isBiometricEnabled,
                                        onToggleBiometric = { viewModel.setBiometricEnabled(it) },
                                        onLockApp = { viewModel.lockApp() },
                                        onFastMoodLog = { viewModel.logFastMood(it) },
                                        onResetDemoData = { viewModel.resetDemoData() },
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
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                                    modifier = Modifier.padding(end = 14.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF16A34A))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = strings.topBarStatus,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D)
                                        )
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp,
                            shadowElevation = 8.dp,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("bottom_nav")
                            ) {
                                NavigationBarItem(
                                    selected = uiState.currentTab == AppTab.TODAY,
                                    onClick = { viewModel.selectTab(AppTab.TODAY) },
                                    icon = { Icon(Icons.Default.Spa, contentDescription = strings.tabToday, modifier = Modifier.size(22.dp)) },
                                    label = { Text(strings.tabToday, fontWeight = if (uiState.currentTab == AppTab.TODAY) FontWeight.Bold else FontWeight.Medium) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RosePrimary,
                                        selectedTextColor = RosePrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                NavigationBarItem(
                                    selected = uiState.currentTab == AppTab.CALENDAR,
                                    onClick = { viewModel.selectTab(AppTab.CALENDAR) },
                                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = strings.tabCalendar, modifier = Modifier.size(22.dp)) },
                                    label = { Text(strings.tabCalendar, fontWeight = if (uiState.currentTab == AppTab.CALENDAR) FontWeight.Bold else FontWeight.Medium) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RosePrimary,
                                        selectedTextColor = RosePrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                NavigationBarItem(
                                    selected = uiState.currentTab == AppTab.PIXELS,
                                    onClick = { viewModel.selectTab(AppTab.PIXELS) },
                                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = strings.tabPixels, modifier = Modifier.size(22.dp)) },
                                    label = { Text(strings.tabPixels, fontWeight = if (uiState.currentTab == AppTab.PIXELS) FontWeight.Bold else FontWeight.Medium) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RosePrimary,
                                        selectedTextColor = RosePrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                NavigationBarItem(
                                    selected = uiState.currentTab == AppTab.INSIGHTS,
                                    onClick = { viewModel.selectTab(AppTab.INSIGHTS) },
                                    icon = { Icon(Icons.Default.SelfImprovement, contentDescription = strings.tabInsights, modifier = Modifier.size(22.dp)) },
                                    label = { Text(strings.tabInsights, fontWeight = if (uiState.currentTab == AppTab.INSIGHTS) FontWeight.Bold else FontWeight.Medium) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RosePrimary,
                                        selectedTextColor = RosePrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                NavigationBarItem(
                                    selected = uiState.currentTab == AppTab.SETTINGS,
                                    onClick = { viewModel.selectTab(AppTab.SETTINGS) },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = strings.tabSettings, modifier = Modifier.size(22.dp)) },
                                    label = { Text(strings.tabSettings, fontWeight = if (uiState.currentTab == AppTab.SETTINGS) FontWeight.Bold else FontWeight.Medium) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RosePrimary,
                                        selectedTextColor = RosePrimary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when (uiState.currentTab) {
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
                                    onDateSelect = { viewModel.selectDate(it) },
                                    language = uiState.language,
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
                                        viewModel.selectDate(it)
                                        viewModel.selectTab(AppTab.CALENDAR)
                                    },
                                    language = uiState.language,
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
                                    isBiometricEnabled = uiState.isBiometricEnabled,
                                    onToggleBiometric = { viewModel.setBiometricEnabled(it) },
                                    onLockApp = { viewModel.lockApp() },
                                    onFastMoodLog = { viewModel.logFastMood(it) },
                                    onResetDemoData = { viewModel.resetDemoData() },
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

        // Biometric Lock Full-Screen Gate Overlay
        AnimatedVisibility(
            visible = uiState.isLocked,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BiometricLockOverlay(
                errorMessage = uiState.pinError,
                onBiometricUnlock = { viewModel.unlockWithBiometric() },
                onPinSubmit = { pin -> viewModel.verifyPin(pin) },
                language = uiState.language
            )
        }
    }
}
