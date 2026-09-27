package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.DailyLogTagEntity
import com.example.data.entities.MlPredictionEntity
import com.example.data.entities.TagDefinitionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycles ORDER BY start_date DESC")
    fun getAllCycles(): Flow<List<CycleEntity>>

    @Query("SELECT * FROM cycles ORDER BY start_date DESC")
    suspend fun getAllCyclesSync(): List<CycleEntity>

    @Query("SELECT * FROM cycles WHERE end_date IS NULL ORDER BY start_date DESC LIMIT 1")
    fun getActiveCycle(): Flow<CycleEntity?>

    @Query("SELECT * FROM cycles WHERE end_date IS NULL ORDER BY start_date DESC LIMIT 1")
    suspend fun getActiveCycleSync(): CycleEntity?

    @Query("SELECT * FROM cycles ORDER BY start_date DESC LIMIT :limit")
    suspend fun getRecentCycles(limit: Int): List<CycleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycle(cycle: CycleEntity): Long

    @Update
    suspend fun updateCycle(cycle: CycleEntity)

    @Delete
    suspend fun deleteCycle(cycle: CycleEntity)

    @Query("DELETE FROM cycles")
    suspend fun clearAll()
}

@Dao
interface DailyLogDao {
    @Query("SELECT * FROM daily_logs ORDER BY log_date DESC")
    fun getAllLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs ORDER BY log_date DESC")
    suspend fun getAllLogsSync(): List<DailyLogEntity>

    @Query("SELECT * FROM daily_logs WHERE log_date = :date")
    fun getLogForDate(date: String): Flow<DailyLogEntity?>

    @Query("SELECT * FROM daily_logs WHERE log_date = :date")
    suspend fun getLogForDateSync(date: String): DailyLogEntity?

    @Query("SELECT * FROM daily_logs WHERE log_date < :date AND is_logged = 1 ORDER BY log_date DESC LIMIT 1")
    suspend fun getPreviousLoggedDay(date: String): DailyLogEntity?

    @Query("SELECT * FROM daily_logs WHERE log_date BETWEEN :startDate AND :endDate ORDER BY log_date ASC")
    fun getLogsInRange(startDate: String, endDate: String): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE log_date BETWEEN :startDate AND :endDate ORDER BY log_date ASC")
    suspend fun getLogsInRangeSync(startDate: String, endDate: String): List<DailyLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(log: DailyLogEntity)

    @Delete
    suspend fun deleteLog(log: DailyLogEntity)

    @Query("DELETE FROM daily_logs")
    suspend fun clearAll()
}

@Dao
interface TagDao {
    @Query("SELECT * FROM tag_definitions ORDER BY category, tag_name ASC")
    fun getAllTags(): Flow<List<TagDefinitionEntity>>

    @Query("SELECT * FROM tag_definitions ORDER BY category, tag_name ASC")
    suspend fun getAllTagsSync(): List<TagDefinitionEntity>

    @Query("SELECT * FROM tag_definitions WHERE category = :category ORDER BY tag_name ASC")
    fun getTagsByCategory(category: String): Flow<List<TagDefinitionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagDefinitionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<TagDefinitionEntity>)

    @Query("""
        SELECT td.* FROM tag_definitions td
        INNER JOIN daily_log_tags dlt ON td.tag_id = dlt.tag_id
        WHERE dlt.log_date = :date
        ORDER BY td.category, td.tag_name
    """)
    fun getTagsForDate(date: String): Flow<List<TagDefinitionEntity>>

    @Query("""
        SELECT td.* FROM tag_definitions td
        INNER JOIN daily_log_tags dlt ON td.tag_id = dlt.tag_id
        WHERE dlt.log_date = :date
        ORDER BY td.category, td.tag_name
    """)
    suspend fun getTagsForDateSync(date: String): List<TagDefinitionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogTag(dailyLogTag: DailyLogTagEntity)

    @Query("DELETE FROM daily_log_tags WHERE log_date = :date AND tag_id = :tagId")
    suspend fun removeTagFromDate(date: String, tagId: Long)

    @Query("DELETE FROM daily_log_tags WHERE log_date = :date")
    suspend fun clearTagsForDate(date: String)

    @Query("SELECT * FROM daily_log_tags")
    suspend fun getAllDailyLogTagsSync(): List<DailyLogTagEntity>
}

@Dao
interface MlPredictionDao {
    @Query("SELECT * FROM ml_predictions ORDER BY prediction_date ASC")
    fun getAllPredictions(): Flow<List<MlPredictionEntity>>

    @Query("SELECT * FROM ml_predictions ORDER BY prediction_date ASC")
    suspend fun getAllPredictionsSync(): List<MlPredictionEntity>

    @Query("SELECT * FROM ml_predictions WHERE prediction_date >= :fromDate ORDER BY prediction_date ASC LIMIT :limit")
    fun getUpcomingPredictions(fromDate: String, limit: Int = 30): Flow<List<MlPredictionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPredictions(predictions: List<MlPredictionEntity>)

    @Query("DELETE FROM ml_predictions")
    suspend fun clearAll()
}
