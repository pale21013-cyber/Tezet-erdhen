package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entities.MlPredictionEntity
import com.example.localization.AppLanguage
import com.example.localization.AppPersona
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedPhaseAdvice
import com.example.localization.getLocalizedPhaseName
import com.example.localization.getPersonaMessage
import com.example.ml.CyclePhase
import com.example.ml.CycleStats
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.LutealAmber
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.OvulationTeal
import com.example.ui.theme.RosePrimary

@Composable
fun MlInsightsCard(
    stats: CycleStats?,
    predictions: List<MlPredictionEntity>,
    language: AppLanguage = AppLanguage.GERMAN,
    persona: AppPersona = AppPersona.LOVING,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val confidencePct = ((stats?.overallConfidence ?: 0.85) * 100).toInt()
    val avgLen = stats?.averageCycleLength?.toInt() ?: 28
    val stdDev = String.format("%.1f", stats?.standardDeviation ?: 1.8)
    val phase = stats?.currentPhase ?: CyclePhase.OVULATORY

    val phaseAdvice = getLocalizedPhaseAdvice(phase, language)
    val personaMessage = getPersonaMessage(persona, phase, language)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ml_insights_card"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Persona AI Greeting Banner Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth().testTag("persona_banner_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = persona.icon, fontSize = 30.sp)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${persona.getLocalizedName(language)} AI • ${getLocalizedPhaseName(phase, language)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = personaMessage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        // ML Engine Status Header
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        color = FollicularPurple.copy(alpha = 0.15f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, FollicularPurple.copy(alpha = 0.3f)),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "ML",
                                tint = FollicularPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.mlTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = strings.mlSubtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // "x % Zuverlässigkeit" in neuer Zeile unterhalb des Modellnamens
                        Surface(
                            color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF064E3B) else Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF059669) else Color(0xFF86EFAC))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF34D399) else Color(0xFF16A34A))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$confidencePct% ${strings.confidenceLabel}",
                                    color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFFD1FAE5) else Color(0xFF14532D),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row with high contrast & clean typography
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricPill(
                        label = strings.avgCycleLabel,
                        value = "$avgLen ${strings.daysUnit}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricPill(
                        label = strings.varianceLabel,
                        value = "±$stdDev ${strings.daysUnit}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricPill(
                        label = strings.nextPeriodLabel,
                        value = stats?.nextPredictedPeriodDate?.substring(5) ?: strings.soonText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Probability Curve Chart
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = RosePrimary.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoGraph,
                                contentDescription = "Chart",
                                tint = RosePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = strings.probCurveTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.probCurveSubtitle,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Canvas Graph
                val displayPredictions = predictions.take(28)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(110.dp)) {
                        if (displayPredictions.size < 2) return@Canvas

                        val w = size.width
                        val h = size.height
                        val stepX = w / (displayPredictions.size - 1)

                        val path = Path()
                        val fillPath = Path()

                        for (i in displayPredictions.indices) {
                            val prob = displayPredictions[i].predictedNextPeriodProbability.toFloat()
                            val x = i * stepX
                            val y = h - (prob * h * 0.88f)

                            if (i == 0) {
                                path.moveTo(x, y)
                                fillPath.moveTo(x, h)
                                fillPath.lineTo(x, y)
                            } else {
                                path.lineTo(x, y)
                            }
                        }

                        fillPath.lineTo(w, h)
                        fillPath.close()

                        // Draw gradient fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(MenstrualRed.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )

                        // Draw stroke
                        drawPath(
                            path = path,
                            color = MenstrualRed,
                            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw peak indicator point
                        val maxIdx = displayPredictions.indices.maxByOrNull {
                            displayPredictions[it].predictedNextPeriodProbability
                        } ?: 0
                        val peakX = maxIdx * stepX
                        val peakY = h - (displayPredictions[maxIdx].predictedNextPeriodProbability.toFloat() * h * 0.88f)

                        drawCircle(
                            color = Color.White,
                            radius = 6.5.dp.toPx(),
                            center = Offset(peakX, peakY)
                        )
                        drawCircle(
                            color = MenstrualRed,
                            radius = 4.5.dp.toPx(),
                            center = Offset(peakX, peakY)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = strings.todayLabel, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = strings.peakOnsetLabel, fontSize = 10.5.sp, color = MenstrualRed, fontWeight = FontWeight.Bold)
                    Text(text = strings.daysFutureLabel, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Phase Health & Wellness Insights
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = OvulationTeal.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = "Tips",
                                tint = OvulationTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = strings.guideTitle,
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                GuideItem(strings.nutritionLabel, phaseAdvice.nutrition, "🥗")
                GuideItem(strings.workoutLabel, phaseAdvice.workout, "🧘")
                GuideItem(strings.energyLabel, phaseAdvice.energy, "✨")
            }
        }

        // Dedicated Personalized Nutrition Card for the Statistics Tab
        PersonalizedNutritionCard(
            currentPhase = phase,
            language = language
        )

        // New Evidence-Based Nutrition & Movement Suggestion Cards
        PhaseSuggestionsSection(
            currentPhase = phase,
            language = language
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalizedNutritionCard(
    currentPhase: CyclePhase,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    var selectedNutritionPhase by remember(currentPhase) { mutableStateOf(currentPhase) }
    val phaseColor = when (selectedNutritionPhase) {
        CyclePhase.MENSTRUAL -> MenstrualRed
        CyclePhase.FOLLICULAR -> FollicularPurple
        CyclePhase.OVULATORY -> OvulationTeal
        CyclePhase.LUTEAL -> LutealAmber
    }

    val cardTitle = when (language) {
        AppLanguage.GERMAN -> "Personalisierte Ernährung"
        AppLanguage.ENGLISH -> "Personalized Nutrition"
        AppLanguage.ALBANIAN -> "Ushqimi i Personalizuar"
    }

    val cardSubtitle = when (language) {
        AppLanguage.GERMAN -> "Gezielte Nährstoffe angepasst an deine Zyklusphase"
        AppLanguage.ENGLISH -> "Targeted nutrients tailored to your cycle phase"
        AppLanguage.ALBANIAN -> "Ushqyesit e synuar sipas fazës sate"
    }

    val nutritionData = when (selectedNutritionPhase) {
        CyclePhase.MENSTRUAL -> Triple(
            if (language == AppLanguage.GERMAN) "🩸 Menstruationsphase: Eisen & Entzündungshemmung" else if (language == AppLanguage.ALBANIAN) "🩸 Faza Menstruale: Hekur & Kundër Inflamacionit" else "🩸 Menstrual Phase: Iron & Anti-Inflammation",
            listOf("🥩 Eisenreich", "🍊 Vitamin C", "🍵 Magnesium-Tee", "🍫 70%+ Kakao"),
            listOf(
                Triple("🥩 Eisenspeicher auffüllen", "Spinat, Linsen, Kichererbsen & Rindfleisch kompensieren den monatlichen Blutverlust.", "🩸"),
                Triple("🍊 Vitamin-C-Synergie", "Paprika, Zitrusfrüchte & Beeren verdoppeln die pflanzliche Eisenaufnahme im Darm.", "⚡"),
                Triple("🍵 Krampflindernder Tee", "Ingwer-, Kamillen- & Himbeerblättertee entspannen die Gebärmutter-Muskulatur.", "🫖")
            )
        )
        CyclePhase.FOLLICULAR -> Triple(
            if (language == AppLanguage.GERMAN) "💜 Follikelphase: Phytoöstrogene & Zellaufbau" else if (language == AppLanguage.ALBANIAN) "💜 Faza Follikulare: Fitoestrogjene & Ndërtim Qelizor" else "💜 Follicular Phase: Phytoestrogens & Cellular Growth",
            listOf("🌱 Leinsamen", "🥬 Fermentierte Nahrung", "🥑 Mageres Eiweiß", "🫐 Antioxidantien"),
            listOf(
                Triple("🌱 Phytoöstrogen-Balance", "Geschrotete Leinsamen & Kürbiskerne unterstützen den steigenden Östrogenspiegel.", "✨"),
                Triple("🥬 Darmgesundheit & Mikrobiom", "Kimchi, Sauerkraut & Kefir fördern den gesunden Abbau von Östrogen.", "🥗"),
                Triple("🥑 Leichtes Eiweiß & Fette", "Geflügel, Eier, Avocados & frische Salate schenken nachhaltige Energie.", "🔋")
            )
        )
        CyclePhase.OVULATORY -> Triple(
            if (language == AppLanguage.GERMAN) "🩵 Eisprungphase: Antioxidantien & Zink" else if (language == AppLanguage.ALBANIAN) "🩵 Faza e Ovulacionit: Antioksidantë & Zink" else "🩵 Ovulatory Phase: Anti-Inflammatory Antioxidants & Zinc",
            listOf("🫐 Beeren-Kraft", "🦪 Zink & B-Komplex", "🥦 Kreuzblütler", "🌊 Hohe Hydratation"),
            listOf(
                Triple("🫐 Antientzündliche Antioxidantien", "Wilde Blaubeeren, Brombeeren & Granatäpfel schützen Eizellen vor oxidativem Stress.", "🫐"),
                Triple("🦪 Zink & B-Vitamine", "Kürbiskerne, Kichererbsen & Lachs fördern die Eizellqualität und Geweberegeneration.", "🧬"),
                Triple("🥦 Faserstoffreiches Gemüse", "Brokkoli, Rosenkohl & Spargel unterstützen den Östrogen-Abbau in der Leber.", "🌿")
            )
        )
        CyclePhase.LUTEAL -> Triple(
            if (language == AppLanguage.GERMAN) "🟠 Lutealphase: Komplexe Kohlenhydrate & B6" else if (language == AppLanguage.ALBANIAN) "🟠 Faza Luteale: Karbohidrate Komplekse & B6" else "🟠 Luteal Phase: Complex Carbs & B6",
            listOf("🥔 Langsame Kohlenhydrate", "🍌 Vitamin B6", "🌻 Tryptophan", "🥥 Natriumarm"),
            listOf(
                Triple("🥔 Stabile Blutzucker-Kurve", "Süßkartoffeln, Haferflocken & Quinoa verhindern Heißhunger & Progesteron-Tiefs.", "🌾"),
                Triple("🍌 Serotonin & B6-Schub", "Bananen, Walnüsse & Sonnenblumenkerne lindern PMS-Symptome & Stimmungsschwankungen.", "🧠"),
                Triple("🥥 Kalium & Blähungs-Schutz", "Kokoswasser & Avocados wirken natürlich entwässernd gegen Progesteron-Einlagerungen.", "💧")
            )
        )
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, phaseColor.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth().testTag("personalized_nutrition_card")
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = phaseColor.copy(alpha = 0.15f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Nutrition",
                            tint = phaseColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cardTitle,
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = cardSubtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Phase Selector Buttons with Banner Marquee Scroll
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val phasesList = listOf(
                    Triple(CyclePhase.MENSTRUAL, "🩸", MenstrualRed),
                    Triple(CyclePhase.FOLLICULAR, "💜", FollicularPurple),
                    Triple(CyclePhase.OVULATORY, "🩵", OvulationTeal),
                    Triple(CyclePhase.LUTEAL, "🟠", LutealAmber)
                )

                for ((p, emoji, col) in phasesList) {
                    val isSelected = p == selectedNutritionPhase
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) col.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) col else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedNutritionPhase = p }
                            .testTag("nutrition_phase_${p.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            val phaseLabel = when (p) {
                                CyclePhase.MENSTRUAL -> if (language == AppLanguage.GERMAN) "Menstruation" else if (language == AppLanguage.ALBANIAN) "Menstruacion" else "Menstrual"
                                CyclePhase.FOLLICULAR -> if (language == AppLanguage.GERMAN) "Follikel" else if (language == AppLanguage.ALBANIAN) "Follikulare" else "Follicular"
                                CyclePhase.OVULATORY -> if (language == AppLanguage.GERMAN) "Eisprung" else if (language == AppLanguage.ALBANIAN) "Ovulacioni" else "Ovulatory"
                                CyclePhase.LUTEAL -> if (language == AppLanguage.GERMAN) "Luteal" else if (language == AppLanguage.ALBANIAN) "Luteale" else "Luteal"
                            }
                            Text(
                                text = phaseLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) col else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                            )
                        }
                    }
                }
            }

            // Phase Title Banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = phaseColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = nutritionData.first,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = phaseColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            // Highlight Tags (Marquee Ticker Banner)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (chip in nutritionData.second) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = phaseColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = chip,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = phaseColor,
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }
                }
            }

            // Dietary Tips Details
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for ((itemTitle, itemDesc, itemEmoji) in nutritionData.third) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = itemEmoji, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = itemTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = itemDesc,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        CoreAnimationTechniquesCard(language = language)
    }
}

@Composable
private fun MetricPill(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun GuideItem(category: String, text: String, emoji: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 17.sp)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = category,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = text,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
