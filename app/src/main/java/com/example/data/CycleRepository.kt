package com.example.data

import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.DailyLogTagEntity
import com.example.data.entities.MlPredictionEntity
import com.example.data.entities.TagDefinitionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CycleRepository(private val database: AppDatabase) {
    private val cycleDao = database.cycleDao()
    private val dailyLogDao = database.dailyLogDao()
    private val tagDao = database.tagDao()
    private val mlPredictionDao = database.mlPredictionDao()

    val allCycles: Flow<List<CycleEntity>> = cycleDao.getAllCycles()
    val activeCycle: Flow<CycleEntity?> = cycleDao.getActiveCycle()
    val allLogs: Flow<List<DailyLogEntity>> = dailyLogDao.getAllLogs()
    val allTags: Flow<List<TagDefinitionEntity>> = tagDao.getAllTags()
    val upcomingPredictions: Flow<List<MlPredictionEntity>> =
        mlPredictionDao.getUpcomingPredictions(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))

    fun getLogForDate(date: String): Flow<DailyLogEntity?> = dailyLogDao.getLogForDate(date)
    suspend fun getLogForDateSync(date: String): DailyLogEntity? = dailyLogDao.getLogForDateSync(date)

    fun getTagsForDate(date: String): Flow<List<TagDefinitionEntity>> = tagDao.getTagsForDate(date)
    suspend fun getTagsForDateSync(date: String): List<TagDefinitionEntity> = tagDao.getTagsForDateSync(date)

    // Smart Defaults query: Real-time query of historical data from the previous logged day
    suspend fun getSmartDefaults(forDate: String): DailyLogEntity? {
        return dailyLogDao.getPreviousLoggedDay(forDate)
    }

    suspend fun saveDailyLog(
        log: DailyLogEntity,
        selectedTagIds: List<Long>
    ) {
        dailyLogDao.insertOrUpdate(log)
        tagDao.clearTagsForDate(log.logDate)
        for (tagId in selectedTagIds) {
            tagDao.insertLogTag(DailyLogTagEntity(log.logDate, tagId))
        }
    }

    suspend fun insertCycle(cycle: CycleEntity): Long = cycleDao.insertCycle(cycle)
    suspend fun updateCycle(cycle: CycleEntity) = cycleDao.updateCycle(cycle)

    suspend fun getAllCyclesSync(): List<CycleEntity> = cycleDao.getAllCyclesSync()
    suspend fun getAllLogsSync(): List<DailyLogEntity> = dailyLogDao.getAllLogsSync()
    suspend fun getAllTagsSync(): List<TagDefinitionEntity> = tagDao.getAllTagsSync()
    suspend fun getAllDailyLogTagsSync(): List<DailyLogTagEntity> = tagDao.getAllDailyLogTagsSync()

    suspend fun savePredictions(predictions: List<MlPredictionEntity>) {
        mlPredictionDao.clearAll()
        mlPredictionDao.insertPredictions(predictions)
    }

    suspend fun resetDatabase() {
        mlPredictionDao.clearAll()
        dailyLogDao.clearAll()
        cycleDao.clearAll()
        seedInitialData(database)
    }

    suspend fun restoreBackupData(
        cycles: List<CycleEntity>,
        logs: List<DailyLogEntity>,
        logTags: List<DailyLogTagEntity>,
        tags: List<TagDefinitionEntity>
    ) {
        if (tags.isNotEmpty()) {
            tagDao.insertTags(tags)
        }
        if (cycles.isNotEmpty()) {
            cycleDao.insertCycles(cycles)
        }
        if (logs.isNotEmpty()) {
            dailyLogDao.insertLogs(logs)
        }
        if (logTags.isNotEmpty()) {
            tagDao.insertLogTags(logTags)
        }
    }
}
