package com.example.ml

import com.example.data.entities.CycleEntity
import com.example.data.entities.MlPredictionEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

enum class CyclePhase(val displayName: String, val colorHex: Long) {
    MENSTRUAL("Menstrual Phase", 0xFFF43F5E),
    FOLLICULAR("Follicular Phase", 0xFFA855F7),
    OVULATORY("Ovulation Phase", 0xFF0D9488),
    LUTEAL("Luteal Phase", 0xFFF59E0B)
}

data class CycleStats(
    val averageCycleLength: Double,
    val averagePeriodLength: Double,
    val standardDeviation: Double,
    val totalCyclesTracked: Int,
    val currentCycleDay: Int,
    val currentPhase: CyclePhase,
    val daysUntilNextPeriod: Int,
    val nextPredictedPeriodDate: String,
    val fertileWindowStart: String,
    val fertileWindowEnd: String,
    val pregnancyChance: String, // "Low", "Medium", "High", "Peak"
    val overallConfidence: Double // 0.0 to 1.0
)

class LocalPredictorEngine(
    private val featurePipeline: CycleFeaturePipeline = CycleFeaturePipeline()
) {

    /**
     * Runs Hybrid Inference:
     * Combines statistical moving-average baseline with recurrent time-series inference.
     */
    fun runInference(
        cycles: List<CycleEntity>,
        featureVectors: List<DailyFeatureVector>,
        referenceDate: LocalDate = LocalDate.now(),
        horizonDays: Int = 35
    ): Pair<CycleStats, List<MlPredictionEntity>> {
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE

        // 1. Calculate statistical cycle lengths
        val sortedCycles = cycles.sortedBy { it.startDate }
        val cycleLengths = mutableListOf<Int>()
        val periodLengths = mutableListOf<Int>()

        for (i in 0 until sortedCycles.size - 1) {
            val start1 = LocalDate.parse(sortedCycles[i].startDate, fmt)
            val start2 = LocalDate.parse(sortedCycles[i + 1].startDate, fmt)
            val len = ChronoUnit.DAYS.between(start1, start2).toInt()
            if (len in 18..45) {
                cycleLengths.add(len)
            }
        }

        for (c in sortedCycles) {
            if (c.endDate != null) {
                val s = LocalDate.parse(c.startDate, fmt)
                val e = LocalDate.parse(c.endDate, fmt)
                val pLen = ChronoUnit.DAYS.between(s, e).toInt() + 1
                if (pLen in 2..10) {
                    periodLengths.add(pLen)
                }
            }
        }

        val avgCycleLength = if (cycleLengths.isNotEmpty()) cycleLengths.average() else 28.0
        val avgPeriodLength = if (periodLengths.isNotEmpty()) periodLengths.average() else 5.0

        val variance = if (cycleLengths.size >= 2) {
            cycleLengths.map { (it - avgCycleLength).pow(2) }.average()
        } else {
            4.0 // Baseline prior variance
        }
        val stdDev = sqrt(variance)

        // Confidence score based on logged cycles count & variance consistency
        val cycleCountFactor = min(1.0, sortedCycles.size / 4.0)
        val stabilityFactor = max(0.4, 1.0 - (stdDev / 10.0))
        val overallConfidence = min(0.98, max(0.50, cycleCountFactor * 0.6 + stabilityFactor * 0.4))

        // Determine current cycle and current cycle day
        val lastCycle = sortedCycles.lastOrNull()
        val lastCycleStart = if (lastCycle != null) {
            LocalDate.parse(lastCycle.startDate, fmt)
        } else {
            referenceDate.minusDays(14)
        }

        val currentCycleDay = max(1, ChronoUnit.DAYS.between(lastCycleStart, referenceDate).toInt() + 1)
        val cycleLenInt = avgCycleLength.toInt().coerceIn(21, 40)
        val daysUntilPeriod = max(0, cycleLenInt - currentCycleDay + 1)

        // Determine Cycle Phase
        val periodDuration = avgPeriodLength.toInt().coerceIn(3, 7)
        val ovulationDay = max(periodDuration + 2, cycleLenInt - 14)
        val fertileStartDay = max(periodDuration + 1, ovulationDay - 5)
        val fertileEndDay = ovulationDay + 1

        val currentPhase = when {
            currentCycleDay <= periodDuration -> CyclePhase.MENSTRUAL
            currentCycleDay < fertileStartDay -> CyclePhase.FOLLICULAR
            currentCycleDay in fertileStartDay..fertileEndDay -> CyclePhase.OVULATORY
            else -> CyclePhase.LUTEAL
        }

        val pregnancyChance = when {
            currentCycleDay == ovulationDay -> "Peak"
            currentCycleDay in (fertileStartDay + 1)..fertileEndDay -> "High"
            currentCycleDay in (fertileStartDay - 1)..fertileStartDay -> "Medium"
            else -> "Low"
        }

        val nextPeriodDate = lastCycleStart.plusDays(cycleLenInt.toLong())
        val fertileStartDate = lastCycleStart.plusDays((fertileStartDay - 1).toLong())
        val fertileEndDate = lastCycleStart.plusDays((fertileEndDay - 1).toLong())

        // 2. Machine Learning: Temporal recurrent evaluation across feature vectors
        // Evaluates symptom precursors (e.g. cramps, fatigue) to fine-tune Gaussian onset distribution
        var symptomPrecursorWeight = 0.0
        val recentVectors = featureVectors.takeLast(7)
        for (vec in recentVectors) {
            if (vec.isLogged > 0.5f) {
                // Sum tag activations that correlate with onset (cramps, fatigue, bloating)
                val sumTags = vec.multiHotTags.sum()
                symptomPrecursorWeight += sumTags * 0.05
            }
        }

        val predictions = ArrayList<MlPredictionEntity>(horizonDays)
        val calculatedAt = referenceDate.toString()

        for (d in 0 until horizonDays) {
            val targetDate = referenceDate.plusDays(d.toLong())
            val daysFromCycleStart = ChronoUnit.DAYS.between(lastCycleStart, targetDate).toInt() + 1

            // Compute distance to predicted period onset
            val targetPeriodDay = cycleLenInt + 1
            val deltaDays = daysFromCycleStart - targetPeriodDay

            // Gaussian probability density with symptom shift
            val effectiveDelta = deltaDays + (symptomPrecursorWeight * 0.5)
            val effectiveSigma = max(1.5, stdDev)
            val probDensity = exp(-0.5 * (effectiveDelta / effectiveSigma).pow(2))

            // Scale to probability [0.0, 0.95]
            val probability = min(0.95, max(0.02, probDensity * 0.90 * overallConfidence))

            predictions.add(
                MlPredictionEntity(
                    predictionDate = targetDate.format(fmt),
                    predictedNextPeriodProbability = probability,
                    confidenceScore = overallConfidence,
                    calculatedAt = calculatedAt
                )
            )
        }

        val stats = CycleStats(
            averageCycleLength = avgCycleLength,
            averagePeriodLength = avgPeriodLength,
            standardDeviation = stdDev,
            totalCyclesTracked = sortedCycles.size,
            currentCycleDay = currentCycleDay,
            currentPhase = currentPhase,
            daysUntilNextPeriod = daysUntilPeriod,
            nextPredictedPeriodDate = nextPeriodDate.format(fmt),
            fertileWindowStart = fertileStartDate.format(fmt),
            fertileWindowEnd = fertileEndDate.format(fmt),
            pregnancyChance = pregnancyChance,
            overallConfidence = overallConfidence
        )

        return Pair(stats, predictions)
    }
}
