package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CycleRepository
import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.MlPredictionEntity
import com.example.data.entities.TagDefinitionEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedPhaseName
import com.example.ml.CycleFeaturePipeline
import com.example.ml.CycleStats
import com.example.ml.LocalPredictorEngine
import com.example.security.SecurityManager
import com.example.shortcuts.AppShortcutHelper
import kotlinx.coroutines.Dispatchers
import com.example.ui.theme.ThemeSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import android.app.Activity
import com.example.updater.UpdateManager
import com.example.updater.UpdateStatus

import com.example.data.backup.DataBackupManager
import kotlinx.coroutines.withContext

enum class AppTab {
    TODAY,
    CALENDAR,
    PIXELS,
    INSIGHTS,
    SETTINGS
}

data class CycleUiState(
    val selectedDate: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val currentTab: AppTab = AppTab.TODAY,
    val selectedDateLog: DailyLogEntity? = null,
    val selectedTagIds: Set<Long> = emptySet(),
    val allTags: List<TagDefinitionEntity> = emptyList(),
    val smartDefaults: DailyLogEntity? = null,
    val allCycles: List<CycleEntity> = emptyList(),
    val allLogs: List<DailyLogEntity> = emptyList(),
    val predictions: List<MlPredictionEntity> = emptyList(),
    val cycleStats: CycleStats? = null,
    val isOnboardingCompleted: Boolean = true,
    val saveNotification: String? = null,
    val language: AppLanguage = AppLanguage.GERMAN,
    val themeSetting: ThemeSetting = ThemeSetting.SYSTEM,
    val updateStatus: UpdateStatus = UpdateStatus.Idle,
    val targetRepository: String = "pale21013-cyber/Tezet-erdhen",
    val jumpToTodayTrigger: Long = 0L,
    val isCalendarModalVisible: Boolean = false
)

class CycleViewModel(
    private val repository: CycleRepository,
    private val securityManager: SecurityManager,
    private val shortcutHelper: AppShortcutHelper,
    private val updateManager: UpdateManager,
    private val backupManager: DataBackupManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CycleUiState())
    val uiState: StateFlow<CycleUiState> = _uiState.asStateFlow()

    private val predictorEngine = LocalPredictorEngine()
    private val featurePipeline = CycleFeaturePipeline()

    init {
        // Observe updater status
        viewModelScope.launch {
            updateManager.updateStatus.collectLatest { status ->
                _uiState.update { it.copy(updateStatus = status) }
            }
        }
        // Observe onboarding state
        viewModelScope.launch {
            securityManager.isOnboardingCompleted.collectLatest { completed ->
                _uiState.update { it.copy(isOnboardingCompleted = completed) }
            }
        }

        // Observe language state
        viewModelScope.launch {
            securityManager.appLanguage.collectLatest { lang ->
                _uiState.update { it.copy(language = lang) }
            }
        }

        // Observe theme setting
        viewModelScope.launch {
            securityManager.themeSetting.collectLatest { theme ->
                _uiState.update { it.copy(themeSetting = theme) }
            }
        }

        // Observe tags
        viewModelScope.launch {
            repository.allTags.collectLatest { tags ->
                _uiState.update { it.copy(allTags = tags) }
            }
        }

        // Observe cycles and logs, and run ML pipeline
        viewModelScope.launch {
            repository.allCycles.collectLatest { cycles ->
                _uiState.update { it.copy(allCycles = cycles) }
                runMlPipeline()
            }
        }

        viewModelScope.launch {
            repository.allLogs.collectLatest { logs ->
                _uiState.update { it.copy(allLogs = logs) }
                loadSelectedDateData(_uiState.value.selectedDate)
                runMlPipeline()
            }
        }

        // Load today's log and smart defaults
        loadSelectedDateData(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    fun setLanguage(language: AppLanguage) {
        securityManager.setLanguage(language)
    }

    fun setThemeSetting(themeSetting: ThemeSetting) {
        securityManager.setThemeSetting(themeSetting)
    }

    fun logFastMood(moodName: String) {
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch(Dispatchers.IO) {
            val existing = repository.getLogForDateSync(todayStr)
            val currentTags = repository.getTagsForDateSync(todayStr).map { it.tagId }.toMutableSet()
            val allTags = repository.getAllTagsSync()

            val targetTag = allTags.find { it.tagName.equals(moodName, ignoreCase = true) }
            if (targetTag != null) {
                currentTags.add(targetTag.tagId)
            }

            val updatedLog = (existing ?: DailyLogEntity(logDate = todayStr)).copy(isLogged = 1)
            repository.saveDailyLog(updatedLog, currentTags.toList())

            shortcutHelper.recordActionUsage("quick_mood")
            shortcutHelper.recordActionUsage("mood_${moodName.lowercase()}")
            runMlPipeline()

            val strings = getAppStrings(_uiState.value.language)
            val localizedMood = com.example.localization.getLocalizedTagName(moodName, _uiState.value.language)
            val msg = String.format(strings.fastModeLoggedMsg, localizedMood)

            _uiState.update {
                it.copy(
                    saveNotification = msg,
                    selectedDate = todayStr
                )
            }
            loadSelectedDateData(todayStr)
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectDate(date: String) {
        _uiState.update { it.copy(selectedDate = date) }
        loadSelectedDateData(date)
    }

    fun openCalendarModal(date: String) {
        selectDate(date)
        _uiState.update { it.copy(isCalendarModalVisible = true) }
    }

    fun closeCalendarModal() {
        _uiState.update { it.copy(isCalendarModalVisible = false) }
    }

    fun jumpToToday() {
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        _uiState.update {
            it.copy(
                selectedDate = todayStr,
                jumpToTodayTrigger = System.currentTimeMillis()
            )
        }
        loadSelectedDateData(todayStr)
    }

    private fun loadSelectedDateData(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val log = repository.getLogForDateSync(date)
            val tags = repository.getTagsForDateSync(date)
            val defaults = repository.getSmartDefaults(date)

            _uiState.update {
                it.copy(
                    selectedDateLog = log,
                    selectedTagIds = tags.map { t -> t.tagId }.toSet(),
                    smartDefaults = defaults
                )
            }
        }
    }

    fun applySmartDefaults() {
        val defaults = _uiState.value.smartDefaults ?: return
        val current = _uiState.value.selectedDateLog
        val updated = (current ?: DailyLogEntity(logDate = _uiState.value.selectedDate)).copy(
            sleepQuality = defaults.sleepQuality,
            activityLevel = defaults.activityLevel,
            tabletTaken = defaults.tabletTaken
        )
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun updateFlowIntensity(intensity: Int, color: String? = null, hasClots: Int = 0) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val activeCycle = _uiState.value.allCycles.firstOrNull { it.endDate == null }
        val updated = current.copy(
            flowIntensity = intensity,
            flowColor = color ?: current.flowColor ?: "Big Red",
            hasClots = hasClots,
            cycleId = activeCycle?.cycleId ?: current.cycleId,
            isLogged = 1
        )
        _uiState.update { it.copy(selectedDateLog = updated) }
        shortcutHelper.recordActionUsage("quick_flow")
    }

    fun toggleTablet(taken: Boolean) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val updated = current.copy(tabletTaken = if (taken) 1 else 0, isLogged = 1)
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun updateSleepQuality(quality: Int) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val updated = current.copy(sleepQuality = quality.coerceIn(1, 5), isLogged = 1)
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun updateActivityLevel(level: Int) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val updated = current.copy(activityLevel = level.coerceIn(0, 2), isLogged = 1)
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun updateNotes(notes: String) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val updated = current.copy(notes = notes, isLogged = 1)
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun setWaterMl(ml: Int) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val updated = current.copy(waterMl = ml.coerceAtLeast(0), isLogged = 1)
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun updateJournalEntry(entry: String, prompt: String) {
        val current = _uiState.value.selectedDateLog ?: DailyLogEntity(logDate = _uiState.value.selectedDate)
        val updated = current.copy(journalEntry = entry, journalPrompt = prompt, isLogged = 1)
        _uiState.update { it.copy(selectedDateLog = updated) }
    }

    fun toggleTag(tagId: Long) {
        val currentTags = _uiState.value.selectedTagIds.toMutableSet()
        if (currentTags.contains(tagId)) {
            currentTags.remove(tagId)
        } else {
            currentTags.add(tagId)
        }
        _uiState.update { it.copy(selectedTagIds = currentTags) }

        // Find tag category for shortcut learning
        val tag = _uiState.value.allTags.find { it.tagId == tagId }
        if (tag?.category == "emotion") {
            shortcutHelper.recordActionUsage("quick_mood")
        } else {
            shortcutHelper.recordActionUsage("quick_symptoms")
        }
    }

    fun saveCurrentLog() {
        val date = _uiState.value.selectedDate
        val log = (_uiState.value.selectedDateLog ?: DailyLogEntity(logDate = date)).copy(isLogged = 1)
        val tags = _uiState.value.selectedTagIds.toList()

        viewModelScope.launch(Dispatchers.IO) {
            repository.saveDailyLog(log, tags)
            runMlPipeline()
            val strings = getAppStrings(_uiState.value.language)
            _uiState.update { it.copy(saveNotification = strings.saveSuccessMsg) }
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(saveNotification = null) }
    }

    fun exportData(onReady: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val json = backupManager.exportAllDataToJson(_uiState.value.cycleStats)
            withContext(Dispatchers.Main) {
                onReady(json)
            }
        }
    }

    fun shareBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            val json = backupManager.exportAllDataToJson(_uiState.value.cycleStats)
            backupManager.shareBackup(json)
        }
    }

    fun importData(jsonContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = backupManager.importDataFromJson(jsonContent)
            if (result.isSuccess) {
                runMlPipeline()
                val count = result.getOrNull()?.logsCount ?: 0
                _uiState.update { it.copy(saveNotification = "✓ $count Tage & LSTM-Modelldaten erfolgreich importiert!") }
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "Import fehlgeschlagen"
                _uiState.update { it.copy(saveNotification = "Fehler: $err") }
            }
        }
    }

    fun resetDemoData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetDatabase()
            runMlPipeline()
        }
    }

    fun runMlPipeline() {
        viewModelScope.launch(Dispatchers.Default) {
            val cycles = repository.getAllCyclesSync()
            val logs = repository.getAllLogsSync()
            val tags = repository.getAllTagsSync()
            val logTags = repository.getAllDailyLogTagsSync()

            val vectors = featurePipeline.constructSlidingWindowVectors(
                cycles = cycles,
                logs = logs,
                allTags = tags,
                logTags = logTags,
                windowDays = 90
            )

            val (stats, predictions) = predictorEngine.runInference(
                cycles = cycles,
                featureVectors = vectors
            )

            repository.savePredictions(predictions)

            _uiState.update {
                it.copy(
                    cycleStats = stats,
                    predictions = predictions
                )
            }

            val lang = _uiState.value.language
            val strings = getAppStrings(lang)
            val localizedPhase = getLocalizedPhaseName(stats.currentPhase, lang)
            shortcutHelper.updateDynamicShortcuts("${strings.cycleDayPrefix} ${stats.currentCycleDay} • $localizedPhase")
        }
    }

    fun checkForUpdates(repo: String? = null) {
        viewModelScope.launch {
            updateManager.checkForUpdates(repo ?: updateManager.targetRepository)
        }
    }

    fun setUpdateTargetRepo(repo: String) {
        updateManager.targetRepository = repo
        _uiState.update { it.copy(targetRepository = repo) }
    }

    fun downloadAndInstallUpdate(activity: Activity? = null) {
        val currentStatus = _uiState.value.updateStatus
        val downloadUrl = if (currentStatus is UpdateStatus.UpdateAvailable) {
            currentStatus.info.downloadUrl
        } else {
            "https://github.com/${updateManager.targetRepository}/releases/latest/download/AuraCycle-latest.apk"
        }
        viewModelScope.launch {
            updateManager.downloadAndInstallApk(downloadUrl, activity)
        }
    }

    fun completeOnboarding(
        goal: String,
        lastPeriodDate: String,
        cycleLength: Int,
        periodDuration: Int,
        isRegular: Boolean,
        selectedTagIds: Set<Long>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // If the user specified a last period date, record initial cycle and period logs
                val parsedDate = try {
                    LocalDate.parse(lastPeriodDate, DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (e: Exception) {
                    LocalDate.now()
                }

                // Add cycle baseline
                repository.insertCycle(
                    CycleEntity(
                        startDate = parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                        endDate = null,
                        periodIntensity = 2
                    )
                )

                // Log period days for the duration
                for (dayOffset in 0 until periodDuration) {
                    val logDate = parsedDate.plusDays(dayOffset.toLong())
                    if (!logDate.isAfter(LocalDate.now())) {
                        val dateStr = logDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        repository.saveDailyLog(
                            DailyLogEntity(
                                logDate = dateStr,
                                flowIntensity = if (dayOffset == 0 || dayOffset == 1) 3 else 2,
                                flowColor = "Big Red",
                                sleepQuality = 4,
                                activityLevel = 1,
                                isLogged = 1,
                                notes = "Logged during Onboarding ($goal)"
                            ),
                            selectedTagIds = selectedTagIds.toList()
                        )
                    }
                }
            } catch (e: Exception) {
                // Safe fallback
            }

            securityManager.setOnboardingCompleted(true)
            runMlPipeline()
        }
    }

    fun replayOnboarding() {
        securityManager.setOnboardingCompleted(false)
    }

    fun resetUpdateStatus() {
        updateManager.resetStatus()
    }
}

class CycleViewModelFactory(
    private val repository: CycleRepository,
    private val securityManager: SecurityManager,
    private val shortcutHelper: AppShortcutHelper,
    private val updateManager: UpdateManager,
    private val backupManager: DataBackupManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CycleViewModel(repository, securityManager, shortcutHelper, updateManager, backupManager) as T
    }
}
