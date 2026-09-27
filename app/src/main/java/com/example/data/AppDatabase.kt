package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.CycleDao
import com.example.data.dao.DailyLogDao
import com.example.data.dao.MlPredictionDao
import com.example.data.dao.TagDao
import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.DailyLogTagEntity
import com.example.data.entities.MlPredictionEntity
import com.example.data.entities.TagDefinitionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Database(
    entities = [
        CycleEntity::class,
        DailyLogEntity::class,
        TagDefinitionEntity::class,
        DailyLogTagEntity::class,
        MlPredictionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cycleDao(): CycleDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun tagDao(): TagDao
    abstract fun mlPredictionDao(): MlPredictionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aura_cycle_secure.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedInitialData(database)
                }
            }
        }
    }
}

suspend fun seedInitialData(database: AppDatabase) {
    val tagDao = database.tagDao()
    val cycleDao = database.cycleDao()
    val logDao = database.dailyLogDao()

    // 1. Tag Definitions: Multi-tagging emotional nuances & physical symptoms
    val defaultTags = listOf(
        // Emotional Nuances (for multi-tagging complex emotional states)
        TagDefinitionEntity(tagName = "Happy", category = "emotion", emoji = "😊", colorHex = 0xFFFF6584),
        TagDefinitionEntity(tagName = "Calm", category = "emotion", emoji = "🌿", colorHex = 0xFF14B8A6),
        TagDefinitionEntity(tagName = "Sensitive", category = "emotion", emoji = "🥺", colorHex = 0xFFA855F7),
        TagDefinitionEntity(tagName = "Sad", category = "emotion", emoji = "😔", colorHex = 0xFF6366F1),
        TagDefinitionEntity(tagName = "Anxious", category = "emotion", emoji = "⚡", colorHex = 0xFFF59E0B),
        TagDefinitionEntity(tagName = "Irritable", category = "emotion", emoji = "😤", colorHex = 0xFFEF4444),
        TagDefinitionEntity(tagName = "Energetic", category = "emotion", emoji = "✨", colorHex = 0xFFEC4899),
        TagDefinitionEntity(tagName = "Exhausted", category = "emotion", emoji = "😴", colorHex = 0xFF8B5CF6),
        TagDefinitionEntity(tagName = "Moody", category = "emotion", emoji = "🎭", colorHex = 0xFFF43F5E),
        TagDefinitionEntity(tagName = "Confident", category = "emotion", emoji = "👑", colorHex = 0xFFEAB308),
        TagDefinitionEntity(tagName = "Focused", category = "emotion", emoji = "🎯", colorHex = 0xFF06B6D4),
        TagDefinitionEntity(tagName = "Vulnerable", category = "emotion", emoji = "🕊️", colorHex = 0xFFD946EF),

        // Physical Symptoms
        TagDefinitionEntity(tagName = "All Good", category = "physical_symptom", emoji = "👍", colorHex = 0xFF10B981),
        TagDefinitionEntity(tagName = "Cramps", category = "physical_symptom", emoji = "⚡", colorHex = 0xFFE11D48),
        TagDefinitionEntity(tagName = "Headache", category = "physical_symptom", emoji = "🤕", colorHex = 0xFF8B5CF6),
        TagDefinitionEntity(tagName = "Acne", category = "physical_symptom", emoji = "✨", colorHex = 0xFFF97316),
        TagDefinitionEntity(tagName = "Bloating", category = "physical_symptom", emoji = "🎈", colorHex = 0xFF6366F1),
        TagDefinitionEntity(tagName = "Tender Breasts", category = "physical_symptom", emoji = "🌸", colorHex = 0xFFEC4899),
        TagDefinitionEntity(tagName = "Fatigue", category = "physical_symptom", emoji = "💤", colorHex = 0xFF64748B),
        TagDefinitionEntity(tagName = "Back Pain", category = "physical_symptom", emoji = "🦴", colorHex = 0xFFD97706),
        TagDefinitionEntity(tagName = "Cravings", category = "physical_symptom", emoji = "🍫", colorHex = 0xFFB45309),
        TagDefinitionEntity(tagName = "Nausea", category = "physical_symptom", emoji = "🤢", colorHex = 0xFF059669)
    )
    tagDao.insertTags(defaultTags)

    // 2. Realistic Historical Cycles (past 3 cycles + current active cycle)
    val today = LocalDate.now()
    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    // Cycle 3 months ago (28 days)
    val c1Start = today.minusDays(84)
    val c1End = today.minusDays(57)
    val c1Id = cycleDao.insertCycle(
        CycleEntity(startDate = c1Start.format(fmt), endDate = c1End.format(fmt), periodIntensity = 2)
    )

    // Cycle 2 months ago (28 days)
    val c2Start = today.minusDays(56)
    val c2End = today.minusDays(29)
    val c2Id = cycleDao.insertCycle(
        CycleEntity(startDate = c2Start.format(fmt), endDate = c2End.format(fmt), periodIntensity = 3)
    )

    // Current Cycle (started 14 days ago - Ovulation phase right now!)
    val c3Start = today.minusDays(14)
    val c3Id = cycleDao.insertCycle(
        CycleEntity(startDate = c3Start.format(fmt), endDate = null, periodIntensity = 2)
    )

    // 3. Seed some representative logs for previous days to support smart defaults and year-in-pixels
    val tagsList = tagDao.getAllTagsSync()
    val happyTag = tagsList.find { it.tagName == "Happy" }?.tagId ?: 1L
    val calmTag = tagsList.find { it.tagName == "Calm" }?.tagId ?: 2L
    val crampsTag = tagsList.find { it.tagName == "Cramps" }?.tagId ?: 14L

    // Seed historical period flow logs for c1, c2, c3
    for (i in 0..4) {
        val d1 = c1Start.plusDays(i.toLong()).format(fmt)
        logDao.insertOrUpdate(
            DailyLogEntity(
                logDate = d1,
                cycleId = c1Id,
                flowIntensity = if (i == 1 || i == 2) 3 else 2,
                flowColor = "Big Red",
                tabletTaken = 1,
                sleepQuality = 3,
                activityLevel = 0,
                isLogged = 1
            )
        )

        val d2 = c2Start.plusDays(i.toLong()).format(fmt)
        logDao.insertOrUpdate(
            DailyLogEntity(
                logDate = d2,
                cycleId = c2Id,
                flowIntensity = if (i == 1) 3 else 2,
                flowColor = "Big Red",
                tabletTaken = 1,
                sleepQuality = 4,
                activityLevel = 1,
                isLogged = 1
            )
        )

        val d3 = c3Start.plusDays(i.toLong()).format(fmt)
        logDao.insertOrUpdate(
            DailyLogEntity(
                logDate = d3,
                cycleId = c3Id,
                flowIntensity = if (i == 1 || i == 2) 3 else 1,
                flowColor = if (i == 0) "Pink" else "Big Red",
                tabletTaken = 1,
                sleepQuality = 3,
                activityLevel = 0,
                isLogged = 1
            )
        )
    }

    // Seed yesterday's log to test Smart Defaults!
    val yesterday = today.minusDays(1).format(fmt)
    logDao.insertOrUpdate(
        DailyLogEntity(
            logDate = yesterday,
            cycleId = c3Id,
            flowIntensity = 0,
            tabletTaken = 1,
            sleepQuality = 4,
            activityLevel = 2,
            notes = "Felt vibrant and energized. Great morning walk.",
            isLogged = 1
        )
    )
    tagDao.insertLogTag(DailyLogTagEntity(yesterday, happyTag))
    tagDao.insertLogTag(DailyLogTagEntity(yesterday, calmTag))
}
