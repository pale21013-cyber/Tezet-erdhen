package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.entities.CycleEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedPhaseName
import com.example.localization.getLocalizedTagName
import com.example.ml.CycleFeaturePipeline
import com.example.ml.CyclePhase
import com.example.ml.LocalPredictorEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aura Cycle", appName)
    }

    @Test
    fun `verify german and albanian localization strings`() {
        val deStrings = getAppStrings(AppLanguage.GERMAN)
        assertEquals("Heute", deStrings.tabToday)
        assertEquals("Sicherheit", deStrings.tabSecurity)
        assertEquals("Menstruationsfluss", deStrings.flowTitle)
        assertEquals("Menstruationsphase", getLocalizedPhaseName(CyclePhase.MENSTRUAL, AppLanguage.GERMAN))
        assertEquals("Glücklich", getLocalizedTagName("Happy", AppLanguage.GERMAN))

        val sqStrings = getAppStrings(AppLanguage.ALBANIAN)
        assertEquals("Sot", sqStrings.tabToday)
        assertEquals("Siguria", sqStrings.tabSecurity)
        assertEquals("Rrjedha Menstruale", sqStrings.flowTitle)
        assertEquals("Faza Menstruale", getLocalizedPhaseName(CyclePhase.MENSTRUAL, AppLanguage.ALBANIAN))
        assertEquals("E lumtur", getLocalizedTagName("Happy", AppLanguage.ALBANIAN))

        // Verify Theme and Fast Actions localization
        assertEquals("Erscheinungsbild & Farbschema", deStrings.themeSectionTitle)
        assertEquals("Hell", deStrings.themeLight)
        assertEquals("Dunkel", deStrings.themeDark)
        assertEquals("Dukja & Tema e Ngjyrave", sqStrings.themeSectionTitle)
        assertEquals("E çelët", sqStrings.themeLight)
        assertEquals("E errët", sqStrings.themeDark)
    }

    @Test
    fun `ml predictor runs hybrid cycle inference`() {
        val engine = LocalPredictorEngine()
        val pipeline = CycleFeaturePipeline()

        val sampleCycles = listOf(
            CycleEntity(cycleId = 1, startDate = "2026-06-01", endDate = "2026-06-05", periodIntensity = 2),
            CycleEntity(cycleId = 2, startDate = "2026-06-29", endDate = "2026-07-03", periodIntensity = 2),
            CycleEntity(cycleId = 3, startDate = "2026-07-27", endDate = "2026-07-31", periodIntensity = 2),
            CycleEntity(cycleId = 4, startDate = "2026-08-24", endDate = null, periodIntensity = 2)
        )

        val vectors = pipeline.constructSlidingWindowVectors(
            cycles = sampleCycles,
            logs = emptyList(),
            allTags = emptyList(),
            logTags = emptyList(),
            windowDays = 30,
            referenceDate = LocalDate.parse("2026-09-07")
        )

        val (stats, predictions) = engine.runInference(
            cycles = sampleCycles,
            featureVectors = vectors,
            referenceDate = LocalDate.parse("2026-09-07")
        )

        assertNotNull(stats)
        assertTrue(stats.averageCycleLength in 26.0..30.0)
        assertTrue(predictions.isNotEmpty())
        assertTrue(stats.overallConfidence > 0.5)
    }
}
