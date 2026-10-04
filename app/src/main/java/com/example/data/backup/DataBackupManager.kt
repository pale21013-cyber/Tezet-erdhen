package com.example.data.backup

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.BuildConfig
import com.example.data.CycleRepository
import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.DailyLogTagEntity
import com.example.data.entities.TagDefinitionEntity
import com.example.ml.CycleStats
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.time.Instant

data class ImportResult(
    val cyclesCount: Int,
    val logsCount: Int,
    val tagsCount: Int,
    val message: String
)

class DataBackupManager(
    private val repository: CycleRepository,
    private val context: Context
) {

    suspend fun exportAllDataToJson(currentStats: CycleStats?): String {
        val root = JSONObject()
        root.put("app_id", BuildConfig.APPLICATION_ID)
        root.put("app_version", BuildConfig.VERSION_NAME)
        root.put("version_code", BuildConfig.VERSION_CODE)
        root.put("export_timestamp", Instant.now().toString())
        root.put("backup_format_version", 1)

        // 1. Cycles
        val cycles = repository.getAllCyclesSync()
        val cyclesArray = JSONArray()
        for (c in cycles) {
            val obj = JSONObject()
            obj.put("cycleId", c.cycleId)
            obj.put("startDate", c.startDate)
            if (c.endDate != null) obj.put("endDate", c.endDate)
            obj.put("periodIntensity", c.periodIntensity)
            cyclesArray.put(obj)
        }
        root.put("cycles", cyclesArray)

        // 2. Daily Logs
        val logs = repository.getAllLogsSync()
        val logsArray = JSONArray()
        for (l in logs) {
            val obj = JSONObject()
            obj.put("logDate", l.logDate)
            if (l.cycleId != null) obj.put("cycleId", l.cycleId)
            obj.put("flowIntensity", l.flowIntensity)
            if (l.flowColor != null) obj.put("flowColor", l.flowColor)
            obj.put("hasClots", l.hasClots)
            obj.put("tabletTaken", l.tabletTaken)
            obj.put("sleepQuality", l.sleepQuality)
            obj.put("activityLevel", l.activityLevel)
            obj.put("notes", l.notes)
            obj.put("isLogged", l.isLogged)
            logsArray.put(obj)
        }
        root.put("daily_logs", logsArray)

        // 3. Log Tags
        val logTags = repository.getAllDailyLogTagsSync()
        val logTagsArray = JSONArray()
        for (lt in logTags) {
            val obj = JSONObject()
            obj.put("logDate", lt.logDate)
            obj.put("tagId", lt.tagId)
            logTagsArray.put(obj)
        }
        root.put("daily_log_tags", logTagsArray)

        // 4. Tag Definitions
        val tags = repository.getAllTagsSync()
        val tagsArray = JSONArray()
        for (t in tags) {
            val obj = JSONObject()
            obj.put("tagId", t.tagId)
            obj.put("tagName", t.tagName)
            obj.put("category", t.category)
            obj.put("emoji", t.emoji)
            obj.put("colorHex", t.colorHex)
            tagsArray.put(obj)
        }
        root.put("tag_definitions", tagsArray)

        // 5. LSTM / ML Stats & State
        val lstmObj = JSONObject()
        if (currentStats != null) {
            lstmObj.put("averageCycleLength", currentStats.averageCycleLength)
            lstmObj.put("averagePeriodLength", currentStats.averagePeriodLength)
            lstmObj.put("standardDeviation", currentStats.standardDeviation)
            lstmObj.put("totalCyclesTracked", currentStats.totalCyclesTracked)
            lstmObj.put("currentCycleDay", currentStats.currentCycleDay)
            lstmObj.put("currentPhase", currentStats.currentPhase.name)
            lstmObj.put("daysUntilNextPeriod", currentStats.daysUntilNextPeriod)
            lstmObj.put("nextPredictedPeriodDate", currentStats.nextPredictedPeriodDate)
            lstmObj.put("fertileWindowStart", currentStats.fertileWindowStart)
            lstmObj.put("fertileWindowEnd", currentStats.fertileWindowEnd)
            lstmObj.put("pregnancyChance", currentStats.pregnancyChance)
            lstmObj.put("overallConfidence", currentStats.overallConfidence)
        }
        root.put("lstm_model_state", lstmObj)

        return root.toString(2)
    }

    suspend fun importDataFromJson(jsonString: String): Result<ImportResult> {
        return try {
            val root = JSONObject(jsonString)

            // Validate structure
            if (!root.has("daily_logs") && !root.has("cycles")) {
                return Result.failure(IllegalArgumentException("Ungültige Backup-Datei: Keine Zyklus- oder Tagesdaten gefunden."))
            }

            val parsedCycles = mutableListOf<CycleEntity>()
            if (root.has("cycles")) {
                val array = root.getJSONArray("cycles")
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    parsedCycles.add(
                        CycleEntity(
                            cycleId = obj.optLong("cycleId", 0L),
                            startDate = obj.getString("startDate"),
                            endDate = if (obj.has("endDate") && !obj.isNull("endDate")) obj.getString("endDate") else null,
                            periodIntensity = obj.optInt("periodIntensity", 2)
                        )
                    )
                }
            }

            val parsedLogs = mutableListOf<DailyLogEntity>()
            if (root.has("daily_logs")) {
                val array = root.getJSONArray("daily_logs")
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    parsedLogs.add(
                        DailyLogEntity(
                            logDate = obj.getString("logDate"),
                            cycleId = if (obj.has("cycleId") && !obj.isNull("cycleId")) obj.getLong("cycleId") else null,
                            flowIntensity = obj.optInt("flowIntensity", 0),
                            flowColor = if (obj.has("flowColor") && !obj.isNull("flowColor")) obj.getString("flowColor") else null,
                            hasClots = obj.optInt("hasClots", 0),
                            tabletTaken = obj.optInt("tabletTaken", 0),
                            sleepQuality = obj.optInt("sleepQuality", 3),
                            activityLevel = obj.optInt("activityLevel", 1),
                            notes = obj.optString("notes", ""),
                            isLogged = obj.optInt("isLogged", 1)
                        )
                    )
                }
            }

            val parsedLogTags = mutableListOf<DailyLogTagEntity>()
            if (root.has("daily_log_tags")) {
                val array = root.getJSONArray("daily_log_tags")
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    parsedLogTags.add(
                        DailyLogTagEntity(
                            logDate = obj.getString("logDate"),
                            tagId = obj.getLong("tagId")
                        )
                    )
                }
            }

            val parsedTags = mutableListOf<TagDefinitionEntity>()
            if (root.has("tag_definitions")) {
                val array = root.getJSONArray("tag_definitions")
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    parsedTags.add(
                        TagDefinitionEntity(
                            tagId = obj.optLong("tagId", 0L),
                            tagName = obj.getString("tagName"),
                            category = obj.getString("category"),
                            emoji = obj.optString("emoji", "✨"),
                            colorHex = obj.optLong("colorHex", 0xFFE11D48)
                        )
                    )
                }
            }

            repository.restoreBackupData(
                cycles = parsedCycles,
                logs = parsedLogs,
                logTags = parsedLogTags,
                tags = parsedTags
            )

            Result.success(
                ImportResult(
                    cyclesCount = parsedCycles.size,
                    logsCount = parsedLogs.size,
                    tagsCount = parsedLogTags.size,
                    message = "Erfolgreich importiert: ${parsedLogs.size} Tage, ${parsedCycles.size} Zyklen!"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun shareBackup(jsonContent: String) {
        try {
            val cacheFile = File(context.cacheDir, "auracycle_backup_${System.currentTimeMillis()}.json")
            FileOutputStream(cacheFile).use { it.write(jsonContent.toByteArray()) }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cacheFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Tezet erdhen Backup & LSTM Daten")
                putExtra(Intent.EXTRA_TEXT, "Tezet erdhen Backup inklusive Tageseinträgen, Zyklen & LSTM-Modelldaten.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooser = Intent.createChooser(shareIntent, "Tezet erdhen Backup teilen / speichern").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
