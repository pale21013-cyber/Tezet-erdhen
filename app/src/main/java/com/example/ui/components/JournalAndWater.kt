package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ml.CyclePhase
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.LutealAmber
import com.example.ui.theme.MenstrualRed
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.OvulationTeal
import com.example.ui.theme.RosePrimary
import com.example.ui.util.rememberHapticFeedbackManager

/**
 * Returns personalized daily water target in ml synced with hormonal cycle phase.
 */
fun getWaterGoalForPhase(phase: CyclePhase): Int {
    return when (phase) {
        CyclePhase.MENSTRUAL -> 2500  // 10 glasses (replace fluid loss & ease cramps)
        CyclePhase.FOLLICULAR -> 2000 // 8 glasses (baseline cellular hydration)
        CyclePhase.OVULATORY -> 2250  // 9 glasses (peak metabolic demand)
        CyclePhase.LUTEAL -> 2750     // 11 glasses (combat progesterone fluid retention & bloating)
    }
}

/**
 * Explains why this target is recommended for the active phase.
 */
fun getWaterTipForPhase(phase: CyclePhase, language: AppLanguage): String {
    return when (language) {
        AppLanguage.GERMAN -> when (phase) {
            CyclePhase.MENSTRUAL -> "🩸 Menstruation (2.500 ml): Gleicht Flüssigkeitsverlust aus, regt die Durchblutung an und lindernde Gebärmutterkrämpfe."
            CyclePhase.FOLLICULAR -> "🍇 Follikelphase (2.000 ml): Stabile Grundhydratisierung zur Unterstützung des Zellaufbaus und der steigenden Energie."
            CyclePhase.OVULATORY -> "✨ Eisprung (2.250 ml): Erhöhter Flüssigkeitsbedarf zur Unterstützung des Östrogen-Peaks und der Zervixschleim-Produktion."
            CyclePhase.LUTEAL -> "🌊 Lutealphase (2.750 ml): Progesteron fördert Wassereinlagerungen. Ausreichend Trinken spült Natrium aus und lindert Blähungen."
        }
        AppLanguage.ENGLISH -> when (phase) {
            CyclePhase.MENSTRUAL -> "🩸 Menstrual (2,500 ml): Replenishes lost fluid, improves blood flow, and eases menstrual cramping."
            CyclePhase.FOLLICULAR -> "🍇 Follicular (2,000 ml): Baseline cellular hydration supporting follicle growth and rising stamina."
            CyclePhase.OVULATORY -> "✨ Ovulatory (2,250 ml): Supports peak estrogen levels and cervical fluid synthesis."
            CyclePhase.LUTEAL -> "🌊 Luteal (2,750 ml): Progesterone triggers water retention. Higher intake flushes excess sodium and reduces bloating."
        }
        AppLanguage.ALBANIAN -> when (phase) {
            CyclePhase.MENSTRUAL -> "🩸 Menstruacione (2,500 ml): Zëvendëson lëngjet e humbura dhe lehtëson dhimbjet përmes qarkullimit optimal."
            CyclePhase.FOLLICULAR -> "🍇 Follikulare (2,000 ml): Hidratim bazë për rritjen e energjisë dhe zhvillimin qelizor."
            CyclePhase.OVULATORY -> "✨ Ovulacioni (2,250 ml): Mbështet nivelin kulmor të estrogjenit dhe prodhimin e lëngjeve."
            CyclePhase.LUTEAL -> "🌊 Luteale (2,750 ml): Progesteroni shkakton mbajtjen e ujit. Pirja e mjaftueshme shpëlan natriumin dhe pakëson fryrjen."
        }
    }
}

/**
 * Returns 3 phase-tailored reflection prompts for emotional awareness.
 */
fun getJournalPromptsForPhase(phase: CyclePhase, language: AppLanguage): List<String> {
    return when (language) {
        AppLanguage.GERMAN -> when (phase) {
            CyclePhase.MENSTRUAL -> listOf(
                "Welchem Bedürfnis nach Ruhe oder Rückzug möchte dein Körper heute Raum geben?",
                "Welches Gefühl oder welche emotionale Last möchtest du mit diesem Zyklusbeginn loslassen?",
                "Wie kannst du dir heute selbst besonders sanft, geduldig und verständnisvoll begegnen?"
            )
            CyclePhase.FOLLICULAR -> listOf(
                "Welches neue Projekt, Ziel oder welche Idee lässt deine Motivation heute aufblühen?",
                "Wie möchtest du deine wachsende mentale Klarheit und Energie diese Woche nutzen?",
                "Was erfüllt dich gerade mit Zuversicht und frischem Schwung?"
            )
            CyclePhase.OVULATORY -> listOf(
                "Wo und mit wem fühlst du dich heute am stärksten verbunden und ausdrucksstark?",
                "Wie kannst du deine natürliche Ausstrahlung und dein Selbstvertrauen heute bewusst einsetzen?",
                "Welche positive Botschaft oder kreative Energie möchtest du heute teilen?"
            )
            CyclePhase.LUTEAL -> listOf(
                "Welche emotionalen Nuancen oder körperlichen Empfindungen tauchen auf, und wie kannst du ihnen wertfrei begegnen?",
                "Welche persönliche Grenze oder Achtsamkeitsübung würde deiner Seelenruhe heute guttun?",
                "Was brauchst du heute, um dich trotz Hormonschwankungen geerdet und geborgen zu fühlen?"
            )
        }
        AppLanguage.ENGLISH -> when (phase) {
            CyclePhase.MENSTRUAL -> listOf(
                "What need for rest or solitude is your body asking you to honor today?",
                "What emotion or tension would you like to release with the start of this new cycle?",
                "How can you show yourself gentleness and self-compassion today?"
            )
            CyclePhase.FOLLICULAR -> listOf(
                "What new project, goal, or idea excites you as your energy begins to bloom?",
                "How can you harness your rising mental clarity and focus today?",
                "What is filling you with optimism and fresh momentum right now?"
            )
            CyclePhase.OVULATORY -> listOf(
                "Where do you feel most vibrant, confident, and connected to others today?",
                "How can you channel your peak radiance into meaningful relationships or creative work?",
                "What positive message or energy do you wish to express today?"
            )
            CyclePhase.LUTEAL -> listOf(
                "What emotional nuances or physical sensations are surfacing, and how can you hold space for them without judgment?",
                "What healthy boundary or act of self-care would protect your peace of mind today?",
                "What helps you feel grounded and safe as your body prepares for renewal?"
            )
        }
        AppLanguage.ALBANIAN -> when (phase) {
            CyclePhase.MENSTRUAL -> listOf(
                "Cilës nevojë për pushim apo qetësi dëshiron t'i japësh hapësirë sot?",
                "Cilën ndjenjë apo ngarkesë dëshiron ta lirosh me fillimin e këtij cikli?",
                "Si mund t'i tregosh vetes butësi dhe mirëkuptim sot?"
            )
            CyclePhase.FOLLICULAR -> listOf(
                "Cili projekt apo ide e re po e bën energjinë tënde të lulëzojë sot?",
                "Si dëshiron ta shfrytëzosh qartësinë tënde mendore këtë javë?",
                "Çfarë po të mbushet me optimizëm dhe shpresë tani?"
            )
            CyclePhase.OVULATORY -> listOf(
                "Ku dhe me kë ndihesh më së shumti e lidhur dhe shprehëse sot?",
                "Si mund ta kanalizosh energjinë tënde kulmore në diçka me rëndësi?",
                "Cilën mesazh apo energji dëshiron ta ndash sot?"
            )
            CyclePhase.LUTEAL -> listOf(
                "Cilat emocione apo ndjesi trupore po shfaqen, dhe si mund t'i pranosh pa gjykim?",
                "Cili kufi personal do t'i bënte mirë qetësisë sate sot?",
                "Çfarë të ndihmon të ndihesh e tokëzuar dhe e sigurt sot?"
            )
        }
    }
}

/**
 * Cycle-Synced Water Intake Tracker Card Module
 */
@Composable
fun WaterTrackerCard(
    currentWaterMl: Int,
    currentPhase: CyclePhase,
    onWaterChange: (Int) -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val hapticManager = rememberHapticFeedbackManager()
    val targetGoalMl = getWaterGoalForPhase(currentPhase)
    val tipText = getWaterTipForPhase(currentPhase, language)

    val progressFraction = (currentWaterMl.toFloat() / targetGoalMl.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "water_progress"
    )

    val glassesCount = currentWaterMl / 250
    val targetGlasses = targetGoalMl / 250

    val primaryWaterColor = Color(0xFF0284C7) // Sky Blue
    val waterBgColor = Color(0xFFE0F2FE)     // Soft Ice Blue

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("water_tracker_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = waterBgColor,
                        border = BorderStroke(1.dp, primaryWaterColor.copy(alpha = 0.3f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Water",
                                tint = primaryWaterColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = when (language) {
                                AppLanguage.GERMAN -> "Zyklus-Wasserbedarf"
                                AppLanguage.ENGLISH -> "Cycle Water Tracker"
                                AppLanguage.ALBANIAN -> "Hidratimi i Ciklit"
                            },
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$glassesCount / $targetGlasses ${if (language == AppLanguage.GERMAN) "Gläser" else if (language == AppLanguage.ALBANIAN) "gota" else "glasses"} • $currentWaterMl / $targetGoalMl ml",
                            fontSize = 11.5.sp,
                            color = primaryWaterColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Percentage Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (progressFraction >= 1f) Color(0xFFDCFCE7) else waterBgColor,
                    border = BorderStroke(1.dp, if (progressFraction >= 1f) Color(0xFF16A34A) else primaryWaterColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (progressFraction >= 1f) "✓ 100%" else "${(progressFraction * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (progressFraction >= 1f) Color(0xFF15803D) else primaryWaterColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            // Animated Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape),
                    color = primaryWaterColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Quick Add Action Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // +250ml Glass
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = primaryWaterColor,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            hapticManager.performClick()
                            onWaterChange(currentWaterMl + 250)
                        }
                        .testTag("water_add_250")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🥛 +250 ml", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }

                // +500ml Bottle
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            hapticManager.performClick()
                            onWaterChange(currentWaterMl + 500)
                        }
                        .testTag("water_add_500")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🧴 +500 ml",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Undo -250ml
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            hapticManager.performClick()
                            onWaterChange((currentWaterMl - 250).coerceAtLeast(0))
                        }
                        .testTag("water_remove_250")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Undo",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Phase Hydration Tip Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = waterBgColor.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, primaryWaterColor.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = tipText,
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

/**
 * Contextual Daily Journal & Emotional Awareness Reflection Module
 */
@Composable
fun JournalReflectionCard(
    currentEntry: String,
    savedPrompt: String,
    currentPhase: CyclePhase,
    onEntryChange: (entry: String, prompt: String) -> Unit,
    onSaveJournal: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val hapticManager = rememberHapticFeedbackManager()
    val focusManager = LocalFocusManager.current
    val prompts = remember(currentPhase, language) {
        getJournalPromptsForPhase(currentPhase, language)
    }

    var activePromptIndex by remember(savedPrompt, prompts) {
        val foundIdx = prompts.indexOf(savedPrompt)
        mutableIntStateOf(if (foundIdx >= 0) foundIdx else 0)
    }

    val activePrompt = prompts.getOrElse(activePromptIndex) { prompts.first() }

    val phaseColor = Color(currentPhase.colorHex)

    val titleText = when (language) {
        AppLanguage.GERMAN -> "Zyklus-Tagebuch & Reflexion"
        AppLanguage.ENGLISH -> "Cycle Journal & Reflection"
        AppLanguage.ALBANIAN -> "Ditari i Ciklit & Reflektimi"
    }

    val shuffleText = when (language) {
        AppLanguage.GERMAN -> "🎲 Thema wechseln"
        AppLanguage.ENGLISH -> "🎲 Shuffle Prompt"
        AppLanguage.ALBANIAN -> "🎲 Ndërro pyetjen"
    }

    val saveText = when (language) {
        AppLanguage.GERMAN -> "Tagebuch speichern 📝"
        AppLanguage.ENGLISH -> "Save Journal Entry 📝"
        AppLanguage.ALBANIAN -> "Ruaj Ditarin 📝"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("journal_reflection_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = phaseColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.3f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = "Journal",
                                tint = phaseColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = titleText,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${currentEntry.length} / 1000 Zeichen",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Shuffle Prompt Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            hapticManager.performClick()
                            activePromptIndex = (activePromptIndex + 1) % prompts.size
                            onEntryChange(currentEntry, prompts[activePromptIndex])
                        }
                        .testTag("journal_shuffle_prompt_btn")
                ) {
                    Text(
                        text = shuffleText,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Phase Reflection Prompt Banner Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = phaseColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = phaseColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (language) {
                                AppLanguage.GERMAN -> "Reflexionsimpuls"
                                AppLanguage.ENGLISH -> "Reflection Prompt"
                                AppLanguage.ALBANIAN -> "Impulsi i Reflektimit"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = phaseColor
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“$activePrompt”",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Reflection Text Area
            OutlinedTextField(
                value = currentEntry,
                onValueChange = { input ->
                    if (input.length <= 1000) {
                        onEntryChange(input, activePrompt)
                    }
                },
                placeholder = {
                    Text(
                        text = when (language) {
                            AppLanguage.GERMAN -> "Schreibe hier deine Gedanken, Gefühle oder Beobachtungen auf..."
                            AppLanguage.ENGLISH -> "Write your thoughts, feelings, or reflections here..."
                            AppLanguage.ALBANIAN -> "Shkruaj mendimet, ndjenjat apo reflektimet tua këtu..."
                        },
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("journal_input_field"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = phaseColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            // Save Action Button
            Button(
                onClick = {
                    hapticManager.performConfirm()
                    focusManager.clearFocus()
                    onSaveJournal()
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("journal_save_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = saveText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                )
            }
        }
    }
}
