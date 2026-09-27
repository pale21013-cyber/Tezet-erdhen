package com.example.ml

import com.example.data.entities.CycleEntity
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.DailyLogTagEntity
import com.example.data.entities.TagDefinitionEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

/**
 * Feature Engineering Pipeline constructing fixed-size daily input vectors (X_t)
 * across a 90-180 day sliding window for local ML cycle inference.
 *
 * Missing days are NOT interpolated; they strictly receive 0s and is_logged = 0.
 */
data class DailyFeatureVector(
    val date: String,
    val isPeriodStart: Float,
    val isPeriodEnd: Float,
    val intensity: Float,
    val tabletTaken: Float,
    val sleepQuality: Float,
    val activityLevel: Float,
    val multiHotTags: FloatArray,
    val isLogged: Float // 1.0 if logged, 0.0 if missing
) {
    fun toFlatArray(): FloatArray {
        val fixed = floatArrayOf(
            isPeriodStart,
            isPeriodEnd,
            intensity,
            tabletTaken,
            sleepQuality,
            activityLevel,
            isLogged
        )
        return fixed + multiHotTags
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DailyFeatureVector
        return date == other.date &&
                isPeriodStart == other.isPeriodStart &&
                isPeriodEnd == other.isPeriodEnd &&
                intensity == other.intensity &&
                tabletTaken == other.tabletTaken &&
                sleepQuality == other.sleepQuality &&
                activityLevel == other.activityLevel &&
                isLogged == other.isLogged &&
                multiHotTags.contentEquals(other.multiHotTags)
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + isPeriodStart.hashCode()
        result = 31 * result + isPeriodEnd.hashCode()
        result = 31 * result + intensity.hashCode()
        result = 31 * result + tabletTaken.hashCode()
        result = 31 * result + sleepQuality.hashCode()
        result = 31 * result + activityLevel.hashCode()
        result = 31 * result + isLogged.hashCode()
        result = 31 * result + multiHotTags.contentHashCode()
        return result
    }
}

class CycleFeaturePipeline {

    fun constructSlidingWindowVectors(
        cycles: List<CycleEntity>,
        logs: List<DailyLogEntity>,
        allTags: List<TagDefinitionEntity>,
        logTags: List<DailyLogTagEntity>,
        windowDays: Int = 90,
        referenceDate: LocalDate = LocalDate.now()
    ): List<DailyFeatureVector> {
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE
        val startDate = referenceDate.minusDays((windowDays - 1).toLong())

        val logMap = logs.associateBy { it.logDate }
        val cycleStarts = cycles.map { it.startDate }.toSet()
        val cycleEnds = cycles.mapNotNull { it.endDate }.toSet()

        // Map date to set of active tag IDs
        val tagMap = logTags.groupBy({ it.logDate }, { it.tagId })
        val tagIndexMap = allTags.mapIndexed { index, tag -> tag.tagId to index }.toMap()
        val tagSize = allTags.size

        val vectors = ArrayList<DailyFeatureVector>(windowDays)

        var curr = startDate
        while (!curr.isAfter(referenceDate)) {
            val dateStr = curr.format(fmt)
            val log = logMap[dateStr]

            if (log == null || log.isLogged == 0) {
                // Crucial requirement: Missing days must not be interpolated; they receive 0s plus is_logged = 0
                vectors.add(
                    DailyFeatureVector(
                        date = dateStr,
                        isPeriodStart = if (cycleStarts.contains(dateStr)) 1f else 0f,
                        isPeriodEnd = if (cycleEnds.contains(dateStr)) 1f else 0f,
                        intensity = 0f,
                        tabletTaken = 0f,
                        sleepQuality = 0f,
                        activityLevel = 0f,
                        multiHotTags = FloatArray(tagSize),
                        isLogged = 0f
                    )
                )
            } else {
                val activeTagIds = tagMap[dateStr] ?: emptyList()
                val multiHot = FloatArray(tagSize)
                for (tagId in activeTagIds) {
                    val idx = tagIndexMap[tagId]
                    if (idx != null && idx in multiHot.indices) {
                        multiHot[idx] = 1.0f
                    }
                }

                val intensityNorm = min(1.0f, max(0.0f, log.flowIntensity / 3.0f))
                val sleepNorm = min(1.0f, max(0.0f, log.sleepQuality / 5.0f))
                val activityNorm = min(1.0f, max(0.0f, log.activityLevel / 2.0f))

                vectors.add(
                    DailyFeatureVector(
                        date = dateStr,
                        isPeriodStart = if (cycleStarts.contains(dateStr)) 1f else 0f,
                        isPeriodEnd = if (cycleEnds.contains(dateStr)) 1f else 0f,
                        intensity = intensityNorm,
                        tabletTaken = if (log.tabletTaken == 1) 1f else 0f,
                        sleepQuality = sleepNorm,
                        activityLevel = activityNorm,
                        multiHotTags = multiHot,
                        isLogged = 1.0f
                    )
                )
            }

            curr = curr.plusDays(1)
        }

        return vectors
    }
}
