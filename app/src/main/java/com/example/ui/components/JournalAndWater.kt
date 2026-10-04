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
import com.example.localization.AppPersona
import com.example.localization.getPersonaPlaceholder
import com.example.localization.getPersonaPromptBadge
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
 * Returns 3 phase-tailored reflection prompts matching the user's selected persona and language.
 */
fun getJournalPromptsForPhase(
    phase: CyclePhase,
    language: AppLanguage,
    persona: AppPersona = AppPersona.LOVING
): List<String> {
    return when (persona) {
        AppPersona.LOVING -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Welcher liebevollen Fürsorge oder welchem warmen Rückzugsort möchtest du heute bewusst Raum geben? 🌸",
                    "Welche Last darfst du mit diesem Neubeginn sanft von deinen Schultern gleiten lassen?",
                    "Wie kannst du dir selbst heute die Geduld und Zuwendung schenken, die du verdienst?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Welcher schöne Traum oder welches neue Vorhaben lässt dein Herz heute aufblühen? ✨",
                    "Wie möchtest du deine frische, aufkeimende Lebensfreude heute ausleben?",
                    "Was erfüllt dich gerade mit Zuversicht und dankbarer Begeisterung?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Wofür möchtest du deine wunderbare Ausstrahlung und dein offenes Herz heute nutzen? 💕",
                    "Welche Verbindung zu einem lieben Menschen möchtest du heute bewusst vertiefen?",
                    "Welche positive Inspiration möchtest du in die Welt hinaustragen?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Was darfst du heute voller Mitgefühl für dich selbst annehmen, ohne es zu bewerten? ☕",
                    "Welche liebevolle Grenze schützt deinen inneren Frieden heute am besten?",
                    "Was schenkt dir heute ein Gefühl von Geborgenheit und Erdung?"
                )
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "What gentle act of kindness or cozy sanctuary does your body crave today? 🌸",
                    "What tension or emotional weight are you ready to gently release with this cycle?",
                    "How can you show yourself deep compassion and patient care today?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "What wonderful dream or new intention is making your spirit bloom today? ✨",
                    "How would you like to channel your fresh, uplifting energy today?",
                    "What is filling your heart with optimism and joyful momentum right now?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "How can you share your beautiful radiance and open heart with others today? 💕",
                    "Which cherished relationship or bond would you love to nurture today?",
                    "What positive inspiration or warmth do you want to bring into the world?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "What can you tenderly accept about yourself today without any judgment? ☕",
                    "What loving boundary will best protect your inner peace and tranquility today?",
                    "What brings you a comforting feeling of warmth, grounding, and safety?"
                )
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Cilës përkujdesjeje të ngrohtë apo qetësie dëshiron t'i japësh hapësirë sot? 🌸",
                    "Çfarë ngarkese emocionale je gati ta lirosh butësisht me këtë fillim të ri?",
                    "Si mund t'i falësh vetes mirëkuptim dhe përkëdhelje sot?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Cila ëndërr apo ide e bukur po ta ngroh zemrën teksa energjia lulëzon? ✨",
                    "Si dëshiron ta jetosh këtë freski dhe gëzim të brendshëm sot?",
                    "Çfarë po të mbush me shpresë dhe mirënjohje të thellë tani?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Si dëshiron ta ndash rrezatimin tënd të ngrohtë dhe zemrën e hapur sot? 💕",
                    "Cilën marrëdhënie me një njeri të dashur dëshiron ta thellosh sot?",
                    "Çfarë frymëzimi pozitiv dëshiron të përcjellësh tek të tjerët?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Çfarë mund të pranosh me butësi për veten sot pa e gjykuar aspak? ☕",
                    "Cili kufi i dashur do ta mbronte më së miri qetësinë tënde të brendshme?",
                    "Çfarë të fal ngrohtësi dhe siguri shpirtërore sot?"
                )
            }
        }
        AppPersona.SARCASTIC -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Was ist dein Überlebensplan gegen Krämpfe, nervige Menschen und die Schwerkraft? 🍫",
                    "Auf einer Skala von 1 bis 'Fass mich nicht an' – wo stehst du heute?",
                    "Welche vollkommen unnötige Verpflichtung sagst du heute mit bestem Gewissen ab?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Die Hormone kooperieren ausnahmsweise – was reißt du ein, bevor die Laune kippt? 😏",
                    "Nutzt du deine plötzliche Produktivität oder tust du nur so, als hättest du alles im Griff?",
                    "Wer hat heute das Privileg, von deinen grandiosen Ideen überrollt zu werden?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Du hast heute mehr Charisma als der Rest der Menschheit – wer muss dir heute aus dem Weg gehen? 💅",
                    "Hauptgewinn-Energie freigeschaltet. Welche fragwürdige Entscheidung klingt heute brillant?",
                    "Bist du heute wirklich so fabelhaft oder ist das nur der biologische Täuschungsversuch?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Was hat dich heute schon grundlos aufgeregt und wie viele Snacks sind zur Rettung da? 🍿",
                    "Warum atmen alle um dich herum so laut und wie vermeidest du Eskalationen?",
                    "Welcher dramatische Gedanke verdient heute einen Oscar für die beste Fiktion?"
                )
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "What is your survival strategy against cramps, annoying humans, and reality today? 🍫",
                    "On a scale from 1 to 'don't even look in my direction' — where are you at today?",
                    "Which totally unnecessary obligation are you canceling with zero guilt?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Hormones are surprisingly cooperating — what will you conquer before the plot twists? 😏",
                    "Are you actually being productive today or just aggressively organizing snacks?",
                    "Who gets the distinct privilege of being overwhelmed by your sudden brilliance?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Peak main-character energy unlocked. Who needs to politely step out of your way? 💅",
                    "You have more charisma than necessary today. What questionable decision sounds great?",
                    "Are you genuinely this fabulous today or is biology playing tricks on you?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "What mildly irritated you for no reason, and how many snacks are deployed to cope? 🍿",
                    "Why is everyone around you breathing so loudly today, and can they stop?",
                    "Which Oscar-worthy dramatic scenario is your brain rehearsing right now?"
                )
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Cili është plani yt i mbijetesës ndaj ngërçeve, njerëzve të bezdisshëm dhe botës? 🍫",
                    "Në shkallën 1 deri 'mos më afro asnjë milimetër' – ku ndodhesh sot?",
                    "Cilin obligim të kotë po e anulon sot pa pikë faji?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Hormonet po bashkëpunojnë çuditërisht – çfarë do të shkatërrosh para se të ndërrojë moti? 😏",
                    "A po punon vërtet me këtë energji apo thjesht po bën sikur e ke jetën në vijë?",
                    "Kush ka nderin të mahnitet nga idetë e tua sot?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Vetëbesimi ka arritur stratosferën – kush do të të lirojë rrugën me mirësjellje sot? 💅",
                    "Ke më shumë karizëm se ç'duhet sot. Cilën aventurë po planifikon?",
                    "A je vërtet kaq e shkëlqyer sot apo thjesht biologjia po të gënjen bukur?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Çfarë të nervozoi kot sot dhe sa ushqime të shpejta ke përgatitur për shpëtim? 🍿",
                    "Pse po marrin frymë kaq zhurmshëm njerëzit rreth teje sot?",
                    "Cili skenar filmi me drama po luhet kot në mendjen tënde tani?"
                )
            }
        }
        AppPersona.LOGICAL -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Welche Regenerationsphasen und Energie-Pausen planst du heute strukturiert ein?",
                    "Welche nicht-dringenden Aufgaben delegierst oder verschiebst du zur Ressourcenschonung?",
                    "Welche Gewohnheiten unterstützen deine Erholung heute am effektivsten?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Welche strategischen Ziele korrelieren am besten mit deiner steigenden Leistungsfähigkeit?",
                    "Wie strukturierst du deine Woche, um den Produktivitätsschub optimal zu nutzen?",
                    "Welches komplexe Projekt gehst du mit deiner aktuellen mentalen Klarheit an?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Auf welche zentralen Vorhaben, Verhandlungen oder Entscheidungen fokussierst du dich heute?",
                    "Wie setzt du deine maximale Kommunikationsstärke heute zielgerichtet ein?",
                    "Welche messbaren Fortschritte möchtest du in deiner heutigen Spitzenphase erzielen?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Welche Maßnahmen zur Stressreduktion und Blutzuckerstabilisierung wendest du heute an?",
                    "Wie passt du deine Arbeitsumgebung an, um Konzentrationsschwankungen auszugleichen?",
                    "Welche offenen Aufgaben schließt du strukturiert ab, bevor der nächste Zyklus beginnt?"
                )
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Which structured recovery intervals and rest periods are you scheduling today?",
                    "Which non-urgent tasks can be systematically postponed to conserve mental bandwidth?",
                    "What quantifiable habits will best support your physical recovery today?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Which strategic goals best align with your rising cognitive capacity today?",
                    "How will you structure your workflow to maximize the upcoming productivity window?",
                    "What complex project or learning curve will you tackle with your current focus?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Which key presentations, negotiations, or high-impact tasks are you executing today?",
                    "How will you intentionally leverage your peak communication and social clarity?",
                    "What measurable milestones do you intend to achieve during this high-stamina window?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "What specific protocols for stress reduction and glucose stability are you applying today?",
                    "How are you adjusting your workload to optimize focus amid changing energy levels?",
                    "Which open loops and project checklists can you systematically wrap up today?"
                )
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Cilat faza pushimi dhe rikuperimi po i planifikon me strukturë sot?",
                    "Cilat detyra jo-urgjente mund t'i shtysh për të ruajtur energjinë mendore?",
                    "Cilat zakone mbështesin më së miri rigjenerimin tënd fizik sot?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Cilat objektiva strategjike përshtaten më mirë me kapacitetin tënd në rritje sot?",
                    "Si po e strukturon javën për të përfituar maksimalisht nga qartësia mendore?",
                    "Cilën sfidë apo projekt kompleks po e zgjidh me fokusin aktual?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Në cilat takime, prezantime apo vendime kyçe po përqendrohesh sot?",
                    "Si po e përdor aftësinë tënde kulmore komunikuese dhe sociale me plan?",
                    "Cilat arritje të matshme synon të përmbyllësh gjatë kësaj faze kulmore?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Cilat masa për reduktimin e stresit dhe stabilitetin e sheqerit po zbaton sot?",
                    "Si po e përshtat ambientin e punës për të ruajtur përqendrimin optimal?",
                    "Cilat detyra të hapura mund t'i mbyllësh me radhë para ciklit të ri?"
                )
            }
        }
        AppPersona.FUNNY -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Wenn deine Gebärmutter heute sprechen könnte, welcher Soundtrack würde laufen? 🎸🍫",
                    "Was ist heute deine offizielle Entschuldigung für hemmungslosen Snack-Konsum?",
                    "Wie lautet dein persönlicher Zauberspruch gegen den heutigen Gemütszustand?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Du läufst auf 200% Raketentreibstoff! Welches Chaos bringst du heute in Ordnung? 🚀⚡",
                    "Bist du bereit, wie eine Superheldin durch den Tag zu fliegen oder brauchst du erst Kaffee?",
                    "Was ist heute deine geheime Mission zur Eroberung des Universums?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Achtung, Promi-Alarm! Mit wem teilst du heute deine unverschämt geniale Ausstrahlung? 🎉😎",
                    "Könntest du heute spontan eine Rede vor Tausenden halten und Standing Ovations kriegen?",
                    "Welcher Streich oder welche Überraschung steht heute auf deinem Programm?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Die Gefühle fahren heute Achterbahn mit 5 Loopings. Schnallst du dich an oder nimmst du Popcorn? 🎢🍿",
                    "Welcher winzige Anlass könnte heute zu einem hollywoodreifen Drama führen?",
                    "Wie lautet die Snack-Pyramide, die heute dein Überleben sichert?"
                )
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "If your uterus had a theme song today, what heavy metal track would it play? 🎸🍫",
                    "What is your official, totally scientific excuse for eating all the snacks today?",
                    "What is your secret superpower to defeat the blanket monster and get through today?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "You're running on 200% premium rocket fuel! What exciting chaos will you create? 🚀⚡",
                    "Are you ready to superhero-land into your day, or should coffee happen first?",
                    "What is today's top-secret mission in your master plan to rule the world?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Warning: Celebrity alert! Who gets the honor of basking in your glorious aura today? 🎉😎",
                    "Could you give a TED Talk with zero preparation today and still win a Grammy?",
                    "What fun mischief or joyful surprise are you plotting today?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Your emotions are riding a 5-loop rollercoaster. Are you buckling up or bringing popcorn? 🎢🍿",
                    "What minuscule event could trigger a full Hollywood movie monologue today?",
                    "What does your emergency survival snack pyramid look like right now?"
                )
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> listOf(
                    "Nëse mitra jote do të zgjidhte një këngë sot, cili rock i çmendur do të ishte? 🎸🍫",
                    "Cila është justifikimi yt zyrtar dhe shumë 'shkencor' për të ngrënë gjithë çokollatat?",
                    "Cila është superfuqia jote sekrete për të fituar ndaj dembelizmit sot?"
                )
                CyclePhase.FOLLICULAR -> listOf(
                    "Je me 200% bateri rakete sot! Çfarë mrekullie apo rrëmuje të bukur do të bësh? 🚀⚡",
                    "A je gati të fluturosh si superhero sot apo duhet edhe një kafe më parë?",
                    "Cili është misioni yt sekret për të pushtuar botën sot?"
                )
                CyclePhase.OVULATORY -> listOf(
                    "Kujdes: Erdhi ylli i skenës! Kush do të ketë fatin të të shohë kaq rrezatuese sot? 🎉😎",
                    "A mund të mbash një fjalim të paparë sot pa u përgatitur fare dhe të marrësh duartrokitje?",
                    "Çfarë shakaje apo surprize të bukur po kurdis sot?"
                )
                CyclePhase.LUTEAL -> listOf(
                    "Emocionet janë në trenin e shpejtë me 5 rrotullime. A do të lidhësh rripin apo do të marrësh kokoshka? 🎢🍿",
                    "Cili detaj i vogël mund të shkaktojë një dramë të tërë telenovele sot?",
                    "Si duket piramida jote e ushqimeve të shpëtimit për sot?"
                )
            }
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
    persona: AppPersona = AppPersona.LOVING,
    modifier: Modifier = Modifier
) {
    val hapticManager = rememberHapticFeedbackManager()
    val focusManager = LocalFocusManager.current
    val prompts = remember(currentPhase, language, persona) {
        getJournalPromptsForPhase(currentPhase, language, persona)
    }

    var activePromptIndex by remember(savedPrompt, prompts) {
        val foundIdx = prompts.indexOf(savedPrompt)
        mutableIntStateOf(if (foundIdx >= 0) foundIdx else 0)
    }

    val activePrompt = prompts.getOrElse(activePromptIndex) { prompts.first() }

    val phaseColor = Color(currentPhase.colorHex)

    val promptBadge = getPersonaPromptBadge(persona, language)
    val placeholderText = getPersonaPlaceholder(persona, language)

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
                            text = promptBadge,
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
                        text = placeholderText,
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
