package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cycles")
data class CycleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cycle_id")
    val cycleId: Long = 0,
    @ColumnInfo(name = "start_date")
    val startDate: String, // ISO 8601: YYYY-MM-DD
    @ColumnInfo(name = "end_date")
    val endDate: String? = null, // NULL if ongoing
    @ColumnInfo(name = "period_intensity")
    val periodIntensity: Int = 2 // 1 = light, 2 = medium, 3 = heavy
)

@Entity(
    tableName = "daily_logs",
    foreignKeys = [
        ForeignKey(
            entity = CycleEntity::class,
            parentColumns = ["cycle_id"],
            childColumns = ["cycle_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["cycle_id"])]
)
data class DailyLogEntity(
    @PrimaryKey
    @ColumnInfo(name = "log_date")
    val logDate: String, // ISO 8601: YYYY-MM-DD
    @ColumnInfo(name = "cycle_id")
    val cycleId: Long? = null,
    @ColumnInfo(name = "flow_intensity")
    val flowIntensity: Int = 0, // 0 = none, 1 = light, 2 = medium, 3 = heavy, 4 = spotting
    @ColumnInfo(name = "flow_color")
    val flowColor: String? = null, // "Pink", "Big Red", "Dark Red", "Orange", "Brown"
    @ColumnInfo(name = "has_clots")
    val hasClots: Int = 0, // 0 = no, 1 = yes
    @ColumnInfo(name = "tablet_taken")
    val tabletTaken: Int = 0, // 0 = no, 1 = yes
    @ColumnInfo(name = "sleep_quality")
    val sleepQuality: Int = 3, // 1-5 scale
    @ColumnInfo(name = "activity_level")
    val activityLevel: Int = 1, // 0 = inactive, 1 = moderate, 2 = intensive
    @ColumnInfo(name = "water_ml")
    val waterMl: Int = 0,
    @ColumnInfo(name = "journal_entry")
    val journalEntry: String = "",
    @ColumnInfo(name = "journal_prompt")
    val journalPrompt: String = "",
    @ColumnInfo(name = "notes")
    val notes: String = "",
    @ColumnInfo(name = "is_logged")
    val isLogged: Int = 1 // 1 = logged by user, 0 = missing/skipped indicator
)

@Entity(
    tableName = "tag_definitions",
    indices = [Index(value = ["tag_name"], unique = true)]
)
data class TagDefinitionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    val tagId: Long = 0,
    @ColumnInfo(name = "tag_name")
    val tagName: String,
    @ColumnInfo(name = "category")
    val category: String, // 'emotion' or 'physical_symptom'
    @ColumnInfo(name = "emoji")
    val emoji: String = "✨",
    @ColumnInfo(name = "color_hex")
    val colorHex: Long = 0xFFE11D48
)

@Entity(
    tableName = "daily_log_tags",
    primaryKeys = ["log_date", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = DailyLogEntity::class,
            parentColumns = ["log_date"],
            childColumns = ["log_date"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagDefinitionEntity::class,
            parentColumns = ["tag_id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["log_date"]),
        Index(value = ["tag_id"])
    ]
)
data class DailyLogTagEntity(
    @ColumnInfo(name = "log_date")
    val logDate: String,
    @ColumnInfo(name = "tag_id")
    val tagId: Long
)

@Entity(tableName = "ml_predictions")
data class MlPredictionEntity(
    @PrimaryKey
    @ColumnInfo(name = "prediction_date")
    val predictionDate: String, // ISO 8601: YYYY-MM-DD
    @ColumnInfo(name = "predicted_next_period_probability")
    val predictedNextPeriodProbability: Double, // 0.0 to 1.0
    @ColumnInfo(name = "confidence_score")
    val confidenceScore: Double, // 0.0 to 1.0
    @ColumnInfo(name = "calculated_at")
    val calculatedAt: String // ISO 8601 Timestamp
)
