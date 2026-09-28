package com.example.localization

import com.example.ml.CyclePhase

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    GERMAN("de", "Deutsch", "🇩🇪"),
    ALBANIAN("sq", "Shqip", "🇦🇱"),
    ENGLISH("en", "English", "🇬🇧");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: GERMAN
        }
    }
}

data class StringsBundle(
    val appName: String,
    val tabToday: String,
    val tabCalendar: String,
    val tabPixels: String,
    val tabInsights: String,
    val tabSettings: String,
    val topBarStatus: String,

    // Cycle Wheel
    val cycleDayPrefix: String,
    val daysUntilNextPeriod: String,
    val chancePrefix: String,
    val trackButton: String,

    // Flow Section
    val flowTitle: String,
    val flowNone: String,
    val flowLight: String,
    val flowMedium: String,
    val flowHeavy: String,
    val flowSpotting: String,
    val flowColorTitle: String,
    val clotsAdd: String,
    val clotsChecked: String,

    // Multi-tagging
    val emotionsTitle: String,
    val emotionsSubtitle: String,
    val symptomsTitle: String,
    val symptomsSubtitle: String,

    // Smart Defaults & Form
    val smartDefaultsTitle: String,
    val smartDefaultsSleep: String,
    val smartDefaultsAct: String,
    val smartDefaultsPill: String,
    val yes: String,
    val no: String,
    val applyBtn: String,
    val pillTitle: String,
    val pillSubtitle: String,
    val sleepTitle: String,
    val sleepStars: String,
    val actTitle: String,
    val actRest: String,
    val actModerate: String,
    val actIntensive: String,
    val notesTitle: String,
    val notesPlaceholder: String,
    val saveLogBtn: String,
    val saveSuccessMsg: String,

    // Calendar & Pixels
    val legendPeriod: String,
    val legendPredicted: String,
    val legendLogged: String,
    val legendSelected: String,
    val legendFertile: String,
    val legendOvulation: String,
    val legendLowChance: String,
    val babyChanceHighTitle: String,
    val babyChanceHighDesc: String,
    val babyChancePeakTitle: String,
    val babyChancePeakDesc: String,
    val babyChanceLowTitle: String,
    val babyChanceLowDesc: String,

    // Calendar Day Detail Modal
    val dayModalTitle: String,
    val dayModalSavedSection: String,
    val dayModalEditSection: String,
    val dayModalNoEntries: String,
    val dayModalUpdateSaveBtn: String,
    val dayModalFlowLabel: String,
    val dayModalPillLabel: String,
    val dayModalPillTaken: String,
    val dayModalPillNotTaken: String,
    val dayModalSleepLabel: String,
    val dayModalActivityLabel: String,
    val dayModalTagsLabel: String,
    val dayModalNotesLabel: String,
    val dayModalCloseBtn: String,
    val dayModalFertileWindow: String,
    val dayModalSafeWindow: String,
    val dayModalPeriodFlow: String,

    // More Quick Actions
    val shortcutPillTakenSuccess: String,
    val shortcutLogPeriodLabel: String,
    val shortcutCheckFertilityLabel: String,
    val shortcutLogSymptomsLabel: String,
    val shortcutTakePillLabel: String,

    val pixelsTitle: String,
    val pixelsSubtitle: String,
    val periodDaysLabel: String,
    val pixelsLegendFlow: String,
    val pixelsLegendMood: String,
    val pixelsLegendEmpty: String,

    // ML Insights
    val mlTitle: String,
    val mlSubtitle: String,
    val confidenceLabel: String,
    val avgCycleLabel: String,
    val varianceLabel: String,
    val nextPeriodLabel: String,
    val daysUnit: String,
    val soonText: String,
    val probCurveTitle: String,
    val probCurveSubtitle: String,
    val todayLabel: String,
    val peakOnsetLabel: String,
    val daysFutureLabel: String,
    val guideTitle: String,
    val nutritionLabel: String,
    val workoutLabel: String,
    val energyLabel: String,

    // Security & Language tab
    val languageSectionTitle: String,
    val privacyTitle: String,
    val privacySubtitle: String,
    val privacyBulletPoints: String,

    // Backup & Restore
    val backupSectionTitle: String,
    val backupSectionSubtitle: String,
    val backupExportBtn: String,
    val backupImportBtn: String,
    val backupImportSuccess: String,
    val backupImportError: String,

    val dataMgmtTitle: String,
    val resetDemoBtn: String,

    // Security & Offline
    val lockScreenTitle: String,
    val lockScreenSubtitle: String,
    val pinPrompt: String,
    val pinIncorrectError: String,

    // Theme & Appearance
    val themeSectionTitle: String,
    val themeSystem: String,
    val themeLight: String,
    val themeDark: String,

    // Fast actions (Modes of the person)
    val fastActionsTitle: String,
    val fastActionsSubtitle: String,
    val fastMoodHappy: String,
    val fastMoodCalm: String,
    val fastMoodSensitive: String,
    val fastMoodEnergetic: String,
    val fastMoodTired: String,
    val fastModeLoggedMsg: String,

    // In-App Updater & CI/CD
    val updaterTitle: String,
    val updaterSubtitle: String,
    val checkUpdatesBtn: String,
    val downloadInstallBtn: String,
    val downloadingProgress: String,
    val systemInstallerPrompt: String,
    val cicdInfoTitle: String,
    val cicdInfoDesc: String,

    // Onboarding Experience
    val onboardingWelcomeTitle: String,
    val onboardingWelcomeSubtitle: String,
    val onboardingStepOf: String,
    val onboardingGoalTitle: String,
    val onboardingGoalSubtitle: String,
    val onboardingGoal1: String,
    val onboardingGoal1Desc: String,
    val onboardingGoal2: String,
    val onboardingGoal2Desc: String,
    val onboardingGoal3: String,
    val onboardingGoal3Desc: String,
    val onboardingGoal4: String,
    val onboardingGoal4Desc: String,
    val onboardingLastPeriodTitle: String,
    val onboardingLastPeriodSubtitle: String,
    val onboardingLastPeriodToday: String,
    val onboardingLastPeriodYesterday: String,
    val onboardingLastPeriodDaysAgo: String,
    val onboardingCycleLengthTitle: String,
    val onboardingCycleLengthSubtitle: String,
    val onboardingCycleRegular: String,
    val onboardingCycleIrregular: String,
    val onboardingPeriodDurationTitle: String,
    val onboardingPeriodDurationSubtitle: String,
    val onboardingSymptomsTitle: String,
    val onboardingSymptomsSubtitle: String,
    val onboardingFinishTitle: String,
    val onboardingFinishSubtitle: String,
    val onboardingNext: String,
    val onboardingBack: String,
    val onboardingFinish: String,
    val onboardingSkip: String
)

val GermanStrings = StringsBundle(
    appName = "Aura Cycle",
    tabToday = "Heute",
    tabCalendar = "Kalender",
    tabPixels = "Pixel",
    tabInsights = "Einblicke",
    tabSettings = "Einstellungen",
    topBarStatus = "Offline • Verschlüsselt",

    cycleDayPrefix = "Tag",
    daysUntilNextPeriod = "Tage bis zur nächsten Periode",
    chancePrefix = "Chance",
    trackButton = "Erfassen",

    flowTitle = "Menstruationsfluss",
    flowNone = "Keine",
    flowLight = "Leicht",
    flowMedium = "Mittel",
    flowHeavy = "Stark",
    flowSpotting = "Schmierblutung",
    flowColorTitle = "Flussfarbe & Gerinnsel",
    clotsAdd = "+ Gerinnsel",
    clotsChecked = "✓ Gerinnsel",

    emotionsTitle = "Emotionale Nuancen (Mehrfachauswahl)",
    emotionsSubtitle = "Wähle mehrere Gefühle gleichzeitig aus, um deinen Zustand präzise zu erfassen",
    symptomsTitle = "Körperliche Symptome",
    symptomsSubtitle = "Erfasse Körpersignale für präzise lokale LSTM-Zyklusprognosen",

    smartDefaultsTitle = "Intelligente Standardwerte von gestern",
    smartDefaultsSleep = "Schlaf",
    smartDefaultsAct = "Aktivität",
    smartDefaultsPill = "Pille",
    yes = "Ja",
    no = "Nein",
    applyBtn = "Übernehmen",
    pillTitle = "Tägliche Pille / Nahrungsergänzung",
    pillSubtitle = "Verhütung oder tägliche Vitaminaufnahme",
    sleepTitle = "Schlafqualität",
    sleepStars = "Sterne",
    actTitle = "Körperliche Aktivität",
    actRest = "Ruhe",
    actModerate = "Mäßig",
    actIntensive = "Intensiv",
    notesTitle = "Privates Tagebuch & Notizen",
    notesPlaceholder = "Energieniveau, Gelüste, Gedanken...",
    saveLogBtn = "Tageseintrag sicher speichern",
    saveSuccessMsg = "Tageseintrag sicher gespeichert!",

    legendPeriod = "Erfasste Periode",
    legendPredicted = "ML-Prognose",
    legendLogged = "Tageseintrag",
    legendSelected = "Ausgewählt",
    legendFertile = "Fruchtbar (Baby-Chance)",
    legendOvulation = "Eisprung (Höchste Chance)",
    legendLowChance = "Niedrige Chance",
    babyChanceHighTitle = "👶 Hohe Baby-Chance (Fruchtbare Tage)",
    babyChanceHighDesc = "Geschlechtsverkehr an diesem Tag kann zu einer Schwangerschaft führen.",
    babyChancePeakTitle = "👶✨ Höchste Baby-Chance (Eisprung)",
    babyChancePeakDesc = "Der Eisprung findet statt oder steht unmittelbar bevor – maximale Empfängniswahrscheinlichkeit.",
    babyChanceLowTitle = "🛡️ Geringe Empfängnischance",
    babyChanceLowDesc = "Geschlechtsverkehr führt an diesem Tag sehr unwahrscheinlich zu einer Schwangerschaft.",

    dayModalTitle = "Tageseintrag & Details",
    dayModalSavedSection = "Bisher gespeicherte Angaben",
    dayModalEditSection = "Eintrag anpassen & ergänzen",
    dayModalNoEntries = "Noch keine Symptome oder Notizen für diesen Tag gespeichert. Trage unten deine Daten ein, damit das lokale LSTM-Modell sie berücksichtigen kann.",
    dayModalUpdateSaveBtn = "Tageseintrag aktualisieren & speichern",
    dayModalFlowLabel = "Menstruationsfluss",
    dayModalPillLabel = "Pille / Verhütung",
    dayModalPillTaken = "✓ Eingenommen",
    dayModalPillNotTaken = "✗ Nicht genommen",
    dayModalSleepLabel = "Schlafqualität",
    dayModalActivityLabel = "Körperliche Aktivität",
    dayModalTagsLabel = "Stimmungen & Körpersymptome",
    dayModalNotesLabel = "Persönliche Notizen",
    dayModalCloseBtn = "Schließen",
    dayModalFertileWindow = "👶 Fruchtbare Tage (Baby-Chance)",
    dayModalSafeWindow = "🛡️ Geringe Empfängnischance",
    dayModalPeriodFlow = "🩸 Periode",

    shortcutPillTakenSuccess = "Tägliche Pille erfolgreich für heute als eingenommen erfasst!",
    shortcutLogPeriodLabel = "Periode eintragen",
    shortcutCheckFertilityLabel = "Baby-Chance prüfen",
    shortcutLogSymptomsLabel = "Symptome erfassen",
    shortcutTakePillLabel = "Pille genommen",

    pixelsTitle = "Jahr in Pixeln 🌸",
    pixelsSubtitle = "Menstruations- & Gesundheits-Heatmap",
    periodDaysLabel = "Periodentage",
    pixelsLegendFlow = "Menstruationsfluss",
    pixelsLegendMood = "Erfasste Stimmung/Symptome",
    pixelsLegendEmpty = "Kein Eintrag",

    mlTitle = "On-Device ML-Vorhersagemodell",
    mlSubtitle = "Hybrides LSTM-Sequenzmodell • 100% Offline",
    confidenceLabel = "Zuverlässigkeit",
    avgCycleLabel = "Durchschn. Zyklus",
    varianceLabel = "Schwankung (±)",
    nextPeriodLabel = "Nächste Periode",
    daysUnit = "Tage",
    soonText = "Bald",
    probCurveTitle = "30-Tage Prognosekurve für Periodenbeginn",
    probCurveSubtitle = "Zeitliche Wahrscheinlichkeitsdichte basierend auf Zyklen und Symptomspitzen",
    todayLabel = "Heute",
    peakOnsetLabel = "Erwarteter Spitzenbeginn",
    daysFutureLabel = "+30 Tage",
    guideTitle = "Hormon- & Wohlfühl-Leitfaden nach Phasen",
    nutritionLabel = "Ernährung",
    workoutLabel = "Training",
    energyLabel = "Energie",

    languageSectionTitle = "Sprache / Gjuha / Language",
    privacyTitle = "Privacy by Design",
    privacySubtitle = "100% Offline-First Architektur",
    privacyBulletPoints = "• Alle Daten werden lokal in einer geräteverschlüsselten SQLite-Datenbank gespeichert.\n" +
            "• Keine Drittanbieter-Tracker, keine Werbe-SDKs, keine Cloud-Telemetrie.\n" +
            "• Neuronale LSTM-Zeitreihenvorhersagen laufen vollständig lokal auf deinem Gerät.\n" +
            "• App-Shortcuts und Widgets funktionieren komplett ohne Internetverbindung.",

    backupSectionTitle = "Datensicherung & Übertragung",
    backupSectionSubtitle = "Exportiere deine Zyklen, Tageseinträge und LSTM-Modellzustände als JSON oder lade ein bestehendes Backup.",
    backupExportBtn = "📥 Backup herunterladen & teilen",
    backupImportBtn = "📂 Backup-Datei wiederherstellen",
    backupImportSuccess = "✓ Daten und LSTM-Zustand erfolgreich importiert!",
    backupImportError = "Fehler beim Import: Ungültiges Dateiformat.",

    dataMgmtTitle = "Datenverwaltung",
    resetDemoBtn = "Demo-Zyklen zurücksetzen & neu laden",

    lockScreenTitle = "Aura Cycle Geschützt",
    lockScreenSubtitle = "100% Offline & Lokal Verschlüsselt",
    pinPrompt = "Oder 4-stellige PIN eingeben (Standard: 1234)",
    pinIncorrectError = "Falsche PIN. Bitte erneut versuchen.",

    themeSectionTitle = "Erscheinungsbild & Farbschema",
    themeSystem = "System",
    themeLight = "Hell",
    themeDark = "Dunkel",

    fastActionsTitle = "Schnellaktionen (Modus & Stimmung)",
    fastActionsSubtitle = "Beim Gedrückthalten des App-Icons oder im Startbildschirm-Widget verfügbar",
    fastMoodHappy = "Glücklich",
    fastMoodCalm = "Ruhig & Entspannt",
    fastMoodSensitive = "Sensibel",
    fastMoodEnergetic = "Energiegeladen",
    fastMoodTired = "Erschöpft",
    fastModeLoggedMsg = "Modus '%s' sofort erfasst!",

    updaterTitle = "In-App System-Updates & Version",
    updaterSubtitle = "Lädt das APK herunter, beendet die App sauber und übergibt an den Android-Systeminstaller",
    checkUpdatesBtn = "Auf Updates prüfen",
    downloadInstallBtn = "Neueste APK herunterladen & installieren",
    downloadingProgress = "Herunterladen: %d%%",
    systemInstallerPrompt = "App wird beendet und Systeminstaller gestartet...",
    cicdInfoTitle = "GitHub Actions CI/CD Pipeline",
    cicdInfoDesc = "Automatische Versionserhöhung (VersionCode & VersionName) bei jedem Build, APK-Generierung & GitHub Release Veröffentlichung.",

    onboardingWelcomeTitle = "Willkommen bei Aura",
    onboardingWelcomeSubtitle = "Deine persönliche, 100% private & KI-gestützte Zyklus-Begleitung. Beantworte ein paar kurze Fragen, um Aura perfekt auf dich abzustimmen.",
    onboardingStepOf = "Schritt %d von %d",
    onboardingGoalTitle = "Was ist dein Hauptziel?",
    onboardingGoalSubtitle = "Wähle deinen primären Fokus, damit wir Vorhersagen und Ratschläge anpassen können",
    onboardingGoal1 = "Zyklus & Periode tracken",
    onboardingGoal1Desc = "Genaue Vorhersagen des Periodenstarts und der Phasen",
    onboardingGoal2 = "Kinderwunsch & Fruchtbarkeit",
    onboardingGoal2Desc = "Fruchtbare Tage und Eisprung optimal planen",
    onboardingGoal3 = "Körper & Stimmung verstehen",
    onboardingGoal3Desc = "Zusammenhänge zwischen Hormonen, Schlaf und Emotionen",
    onboardingGoal4 = "Pille & Verhütung",
    onboardingGoal4Desc = "Tägliche Erinnerungen und Einnahme-Sicherheit",
    onboardingLastPeriodTitle = "Wann begann deine letzte Periode?",
    onboardingLastPeriodSubtitle = "Dies kalibriert das lokale neuronale LSTM-Vorhersagemodell sofort",
    onboardingLastPeriodToday = "Heute begonnen",
    onboardingLastPeriodYesterday = "Gestern begonnen",
    onboardingLastPeriodDaysAgo = "Vor %d Tagen",
    onboardingCycleLengthTitle = "Wie lang ist dein Zyklus?",
    onboardingCycleLengthSubtitle = "Vom ersten Blutungstag bis zum Tag vor der nächsten Periode",
    onboardingCycleRegular = "Regelmäßig (±1–2 Tage)",
    onboardingCycleIrregular = "Schwankend / Unregelmäßig",
    onboardingPeriodDurationTitle = "Wie lange dauert deine Blutung meistens?",
    onboardingPeriodDurationSubtitle = "Durchschnittliche Anzahl der Blutungstage",
    onboardingSymptomsTitle = "Welche Signale möchtest du tracken?",
    onboardingSymptomsSubtitle = "Wähle deine wichtigsten Wohlfühl-Faktoren",
    onboardingFinishTitle = "Dein Profil ist bereit! 🌸",
    onboardingFinishSubtitle = "Aura hat dein individuelles LSTM-Vorhersagemodell auf deinem Gerät eingerichtet. Alle Daten bleiben zu 100% privat und offline verschlüsselt.",
    onboardingNext = "Weiter",
    onboardingBack = "Zurück",
    onboardingFinish = "Jetzt starten & personalisieren",
    onboardingSkip = "Überspringen"
)

val AlbanianStrings = StringsBundle(
    appName = "Aura Cycle",
    tabToday = "Sot",
    tabCalendar = "Kalendari",
    tabPixels = "Pikselat",
    tabInsights = "Statistikat",
    tabSettings = "Cilësimet",
    topBarStatus = "Offline • E Kriptuar",

    cycleDayPrefix = "Dita",
    daysUntilNextPeriod = "ditë deri në ciklin tjetër",
    chancePrefix = "Mundësia",
    trackButton = "Regjistro",

    flowTitle = "Rrjedha Menstruale",
    flowNone = "Pa rrjedhë",
    flowLight = "E lehtë",
    flowMedium = "Mesatare",
    flowHeavy = "E rëndë",
    flowSpotting = "Njollosje",
    flowColorTitle = "Ngjyra e Rrjedhjes & Koagulat",
    clotsAdd = "+ Koagula",
    clotsChecked = "✓ Koagula",

    emotionsTitle = "Nuancat Emocionale (Përzgjedhje e shumëfishtë)",
    emotionsSubtitle = "Zgjidhni disa ndjenja njëkohësisht për të kapur gjendjen tuaj të saktë",
    symptomsTitle = "Simptomat Fizike",
    symptomsSubtitle = "Regjistroni sinjalet e trupit për parashikime të sakta LSTM",

    smartDefaultsTitle = "Cilësimet e mençura nga dje",
    smartDefaultsSleep = "Gjumi",
    smartDefaultsAct = "Aktiviteti",
    smartDefaultsPill = "Pilula",
    yes = "Po",
    no = "Jo",
    applyBtn = "Apliko",
    pillTitle = "Pilula Ditore / Suplement",
    pillSubtitle = "Kontracepsion ose marrje vitaminash ditore",
    sleepTitle = "Cilësia e Gjumit",
    sleepStars = "Yje",
    actTitle = "Aktiviteti Fizik",
    actRest = "Pushim",
    actModerate = "Mesatar",
    actIntensive = "Intensiv",
    notesTitle = "Ditar Privat & Shënime",
    notesPlaceholder = "Niveli i energjisë, mendimet, dëshirat...",
    saveLogBtn = "Ruaj Regjistrimin Ditor",
    saveSuccessMsg = "Regjistrimi ditor u ruajt i sigurt!",

    legendPeriod = "Periudhë e regjistruar",
    legendPredicted = "Parashikim ML",
    legendLogged = "Regjistrim ditor",
    legendSelected = "E zgjedhur",
    legendFertile = "Ditë fertile (Mundësi për fëmijë)",
    legendOvulation = "Ovulacioni (Shansi më i lartë)",
    legendLowChance = "Shansë e ulët",
    babyChanceHighTitle = "👶 Mundësi e lartë për fëmijë (Ditë fertile)",
    babyChanceHighDesc = "Marrëdhëniet intime në këtë ditë mund të çojnë në shtatzëni.",
    babyChancePeakTitle = "👶✨ Shansi maksimal për fëmijë (Ovulacioni)",
    babyChancePeakDesc = "Ovulacioni po ndodh ose është shumë afër – mundësia maksimale e fekondimit.",
    babyChanceLowTitle = "🛡️ Shansë shumë e ulët për fëmijë",
    babyChanceLowDesc = "Marrëdhëniet intime në këtë ditë nuk kanë gjasa të çojnë në shtatzëni.",

    dayModalTitle = "Detajet e Ditës & Regjistrimi",
    dayModalSavedSection = "Të dhënat e ruajtura deri tani",
    dayModalEditSection = "Ndrysho ose shto më shumë detaje",
    dayModalNoEntries = "Ende nuk ka simptoma ose shënime të ruajtura për këtë ditë. Plotësoni të dhënat më poshtë në mënyrë që modeli LSTM t'i llogarisë.",
    dayModalUpdateSaveBtn = "Përditëso & ruaj regjistrimin ditor",
    dayModalFlowLabel = "Rrjedhja menstruale",
    dayModalPillLabel = "Pilula / Kontracepsioni",
    dayModalPillTaken = "✓ E marrë",
    dayModalPillNotTaken = "✗ Jo e marrë",
    dayModalSleepLabel = "Cilësia e gjumit",
    dayModalActivityLabel = "Aktiviteti fizik",
    dayModalTagsLabel = "Gjendja emocionale & simptomat",
    dayModalNotesLabel = "Shënime private",
    dayModalCloseBtn = "Mbyll",
    dayModalFertileWindow = "👶 Ditë fertile (Mundësi për fëmijë)",
    dayModalSafeWindow = "🛡️ Shansë e ulët për fëmijë",
    dayModalPeriodFlow = "🩸 Perioda",

    shortcutPillTakenSuccess = "Pilula ditore u shënua me sukses si e marrë për sot!",
    shortcutLogPeriodLabel = "Regjistro ciklin",
    shortcutCheckFertilityLabel = "Kontrollo pjellorinë",
    shortcutLogSymptomsLabel = "Regjistro simptomat",
    shortcutTakePillLabel = "Pillën marrë",

    pixelsTitle = "Viti në Piksela 🌸",
    pixelsSubtitle = "Harta termike e ciklit dhe shëndetit",
    periodDaysLabel = "Ditë cikli",
    pixelsLegendFlow = "Rrjedhja e ciklit",
    pixelsLegendMood = "Humori/Simptomat",
    pixelsLegendEmpty = "Pa të dhëna",

    mlTitle = "Modeli Parashikues ML në Pajisje",
    mlSubtitle = "Modeli Hibrid Sekuencial LSTM • 100% Offline",
    confidenceLabel = "Besueshmëria",
    avgCycleLabel = "Cikli mesatar",
    varianceLabel = "Variacioni (±)",
    nextPeriodLabel = "Cikli tjetër",
    daysUnit = "ditë",
    soonText = "Së shpejti",
    probCurveTitle = "Kurba e Mundësisë 30-ditore e Ciklit",
    probCurveSubtitle = "Densiteti kohor i mundësisë bazuar në cikle dhe simptoma",
    todayLabel = "Sot",
    peakOnsetLabel = "Kulmi i pritshëm",
    daysFutureLabel = "+30 Ditë",
    guideTitle = "Udhëzuesi i Hormoneve & Mirëqenies sipas Fazave",
    nutritionLabel = "Ushqyerja",
    workoutLabel = "Stërvitja",
    energyLabel = "Energjia",

    languageSectionTitle = "Gjuha / Sprache / Language",
    privacyTitle = "Privatësia sipas Dizajnit",
    privacySubtitle = "Arkitektura 100% Jashtë Linje",
    privacyBulletPoints = "• Të gjitha të dhënat ruhen lokalisht në një bazë të dhënash të koduar SQLite në pajisje.\n" +
            "• Pa gjurmues të palëve të treta, pa reklama SDK, pa telemetri në cloud.\n" +
            "• Parashikimet e serive kohore LSTM ekzekutohen plotësisht lokalisht në pajisjen tuaj.\n" +
            "• Shkurtoret e aplikacionit dhe mini-programet e ekranit kryesor funksionojnë pa internet.",

    backupSectionTitle = "Ruajtja & Transferimi i të Dhënave",
    backupSectionSubtitle = "Eksporto ciklet, regjistrimet ditore dhe modelin LSTM në JSON ose rikthe një backup ekzistues.",
    backupExportBtn = "📥 Shkarko & Shpërndaj Backup",
    backupImportBtn = "📂 Rikthe skedarin Backup",
    backupImportSuccess = "✓ Të dhënat dhe modeli LSTM u rikthyen me sukses!",
    backupImportError = "Gabim gjatë importit: Skedar i pavlefshëm.",

    dataMgmtTitle = "Menaxhimi i të Dhënave",
    resetDemoBtn = "Rivendos & ringarko ciklet provë",

    lockScreenTitle = "Aura Cycle E Mbrojtur",
    lockScreenSubtitle = "100% Jashtë Linje & E Kriptuar Lokalisht",
    pinPrompt = "Ose shkruaj PIN-in me 4 shifra (Parazgjedhur: 1234)",
    pinIncorrectError = "PIN i pasaktë. Ju lutem provoni përsëri.",

    themeSectionTitle = "Dukja & Tema e Ngjyrave",
    themeSystem = "Sistemi",
    themeLight = "E çelët",
    themeDark = "E errët",

    fastActionsTitle = "Veprime të Shpejta (Gjendja / Humori)",
    fastActionsSubtitle = "E disponueshme duke mbajtur shtypur ikonën e aplikacionit ose në mini-program (widget)",
    fastMoodHappy = "E lumtur",
    fastMoodCalm = "E qetë",
    fastMoodSensitive = "E ndjeshme",
    fastMoodEnergetic = "Energjike",
    fastMoodTired = "E lodhur",
    fastModeLoggedMsg = "Gjendja '%s' u regjistrua me sukses!",

    updaterTitle = "Përditësimet e Sistemit & Versioni",
    updaterSubtitle = "Shkarkon skedarin APK, mbyll aplikacionin dhe e instalon përmes instaluesit të sistemit Android",
    checkUpdatesBtn = "Kontrollo për përditësime",
    downloadInstallBtn = "Shkarko APK-në & Instalo me Sistem",
    downloadingProgress = "Duke shkarkuar: %d%%",
    systemInstallerPrompt = "Aplikacioni po mbyllet dhe instaluesi i sistemit po hapet...",
    cicdInfoTitle = "GitHub Actions CI/CD Pipeline",
    cicdInfoDesc = "Inkrementim automatik i numrit të versionit në çdo iteracion, krijim i paketës APK & publikim automatik.",

    onboardingWelcomeTitle = "Mirë se vini në Aura",
    onboardingWelcomeSubtitle = "Shoqëruesi juaj personal, 100% privat dhe i fuqizuar me IA për ciklin menstrual. Përgjigjuni disa pyetjeve të shkurtra për ta përshtatur.",
    onboardingStepOf = "Hapi %d nga %d",
    onboardingGoalTitle = "Cili është qëllimi juaj kryesor?",
    onboardingGoalSubtitle = "Zgjidhni fokusin tuaj kryesor për parashikime të përshtatura",
    onboardingGoal1 = "Gjurmimi i ciklit & periodave",
    onboardingGoal1Desc = "Parashikime të sakta të fillimit të ciklit dhe fazave",
    onboardingGoal2 = "Planifikimi i shtatzënisë & pjelloria",
    onboardingGoal2Desc = "Gjetja e ditëve pjellore dhe ovulacionit",
    onboardingGoal3 = "Kuptimi i trupit dhe humorit",
    onboardingGoal3Desc = "Lidhja midis hormoneve, gjumit dhe ndjenjave",
    onboardingGoal4 = "Kontracepsioni & pilula",
    onboardingGoal4Desc = "Kujtesa ditore dhe siguria e marrjes",
    onboardingLastPeriodTitle = "Kur filloi perioda juaj e fundit?",
    onboardingLastPeriodSubtitle = "Kjo kalibron menjëherë modelin neural lokal LSTM",
    onboardingLastPeriodToday = "Filloi sot",
    onboardingLastPeriodYesterday = "Filloi dje",
    onboardingLastPeriodDaysAgo = "%d ditë më parë",
    onboardingCycleLengthTitle = "Sa zgjat mesatarisht cikli juaj?",
    onboardingCycleLengthSubtitle = "Nga dita e parë e gjakderdhjes deri në ditën para ciklit tjetër",
    onboardingCycleRegular = "I rregullt (±1–2 ditë)",
    onboardingCycleIrregular = "I ndryshueshëm / I parregullt",
    onboardingPeriodDurationTitle = "Sa ditë zgjat gjakderdhja zakonisht?",
    onboardingPeriodDurationSubtitle = "Numri mesatar i ditëve të rrjedhës",
    onboardingSymptomsTitle = "Cilat sinjale dëshironi të gjurmoni?",
    onboardingSymptomsSubtitle = "Zgjidhni faktorët kryesorë të mirëqenies",
    onboardingFinishTitle = "Profili juaj është gati! 🌸",
    onboardingFinishSubtitle = "Aura konfiguroi modelin tuaj individual LSTM në pajisje. Të gjitha të dhënat mbeten 100% private dhe të kriptuara offline.",
    onboardingNext = "Vazhdo",
    onboardingBack = "Kthehu",
    onboardingFinish = "Fillo & Personalizo",
    onboardingSkip = "Kalo"
)

val EnglishStrings = StringsBundle(
    appName = "Aura Cycle",
    tabToday = "Today",
    tabCalendar = "Calendar",
    tabPixels = "Pixels",
    tabInsights = "Insights",
    tabSettings = "Settings",
    topBarStatus = "Offline • Encrypted",

    cycleDayPrefix = "Day",
    daysUntilNextPeriod = "days until next period",
    chancePrefix = "Chance",
    trackButton = "Track",

    flowTitle = "Menstruation Flow",
    flowNone = "None",
    flowLight = "Light",
    flowMedium = "Medium",
    flowHeavy = "Heavy",
    flowSpotting = "Spotting",
    flowColorTitle = "Flow Color & Clots",
    clotsAdd = "+ Clots",
    clotsChecked = "✓ Clots",

    emotionsTitle = "Emotional Nuances (Multi-Select)",
    emotionsSubtitle = "Select multiple feelings simultaneously to capture your precise emotional state",
    symptomsTitle = "Physical Symptoms",
    symptomsSubtitle = "Track bodily signals to enrich local LSTM cycle predictions",

    smartDefaultsTitle = "Smart Defaults from Yesterday",
    smartDefaultsSleep = "Sleep",
    smartDefaultsAct = "Activity",
    smartDefaultsPill = "Pill",
    yes = "Yes",
    no = "No",
    applyBtn = "Apply",
    pillTitle = "Daily Pill / Supplement",
    pillSubtitle = "Contraception or daily vitamin intake",
    sleepTitle = "Sleep Quality",
    sleepStars = "Stars",
    actTitle = "Physical Activity",
    actRest = "Rest",
    actModerate = "Moderate",
    actIntensive = "Intensive",
    notesTitle = "Private Journal & Notes",
    notesPlaceholder = "Energy levels, food cravings, thoughts...",
    saveLogBtn = "Save Daily Record",
    saveSuccessMsg = "Daily record saved securely!",

    legendPeriod = "Logged Period",
    legendPredicted = "ML Predicted",
    legendLogged = "Logged Entry",
    legendSelected = "Selected",
    legendFertile = "Fertile (Baby Chance)",
    legendOvulation = "Ovulation (Peak Chance)",
    legendLowChance = "Low Chance",
    babyChanceHighTitle = "👶 High Baby Chance (Fertile Window)",
    babyChanceHighDesc = "Having sex on this day can lead to pregnancy.",
    babyChancePeakTitle = "👶✨ Peak Baby Chance (Ovulation)",
    babyChancePeakDesc = "Ovulation is occurring or imminent – maximum probability of conception.",
    babyChanceLowTitle = "🛡️ Low Pregnancy Chance",
    babyChanceLowDesc = "Having sex on this day is very unlikely to lead to pregnancy.",

    dayModalTitle = "Day Log & Details",
    dayModalSavedSection = "What is currently saved",
    dayModalEditSection = "Edit or add more details",
    dayModalNoEntries = "No symptoms or notes logged yet for this day. Fill in details below so the on-device LSTM model can factor them into predictions.",
    dayModalUpdateSaveBtn = "Update & Save Day Entry",
    dayModalFlowLabel = "Period Flow",
    dayModalPillLabel = "Pill / Contraception",
    dayModalPillTaken = "✓ Taken",
    dayModalPillNotTaken = "✗ Not taken",
    dayModalSleepLabel = "Sleep Quality",
    dayModalActivityLabel = "Physical Activity",
    dayModalTagsLabel = "Moods & Physical Symptoms",
    dayModalNotesLabel = "Personal Diary Notes",
    dayModalCloseBtn = "Close",
    dayModalFertileWindow = "👶 Fertile Window (Baby Chance)",
    dayModalSafeWindow = "🛡️ Low Pregnancy Chance",
    dayModalPeriodFlow = "🩸 Period Flow",

    shortcutPillTakenSuccess = "Daily pill successfully logged as taken for today!",
    shortcutLogPeriodLabel = "Record Period Flow",
    shortcutCheckFertilityLabel = "Check Baby Chance",
    shortcutLogSymptomsLabel = "Log Symptoms",
    shortcutTakePillLabel = "Take Pill",

    pixelsTitle = "Year in Pixels 🌸",
    pixelsSubtitle = "Menstrual & Health Heatmap",
    periodDaysLabel = "Period Days",
    pixelsLegendFlow = "Period Flow",
    pixelsLegendMood = "Logged Mood/Symptom",
    pixelsLegendEmpty = "No Entry",

    mlTitle = "On-Device ML Predictor",
    mlSubtitle = "Hybrid LSTM Sequence Model • 100% Offline",
    confidenceLabel = "Confidence",
    avgCycleLabel = "Avg Cycle",
    varianceLabel = "Variance (±)",
    nextPeriodLabel = "Next Period",
    daysUnit = "days",
    soonText = "Soon",
    probCurveTitle = "30-Day Period Onset Probability",
    probCurveSubtitle = "Temporal density curve factoring historical cycles and symptom spikes",
    todayLabel = "Today",
    peakOnsetLabel = "Expected Peak Onset",
    daysFutureLabel = "+30 Days",
    guideTitle = "Phase Hormone & Wellness Guide",
    nutritionLabel = "Nutrition",
    workoutLabel = "Workout",
    energyLabel = "Energy",

    languageSectionTitle = "Language / Sprache / Gjuha",
    privacyTitle = "Privacy by Design",
    privacySubtitle = "100% Offline-First Architecture",
    privacyBulletPoints = "• All data is stored locally in an on-device encrypted SQLite database.\n" +
            "• Zero third-party trackers, zero advertising SDKs, zero cloud telemetry.\n" +
            "• Neural LSTM time-series predictions execute 100% locally on your device.\n" +
            "• App shortcuts and Home Screen widgets operate without network connectivity.",

    backupSectionTitle = "Data Backup & Transfer",
    backupSectionSubtitle = "Export your cycles, daily entries, and LSTM model state as JSON or restore an existing backup file.",
    backupExportBtn = "📥 Download & Share Backup",
    backupImportBtn = "📂 Restore Backup File",
    backupImportSuccess = "✓ Data and LSTM model state restored successfully!",
    backupImportError = "Error importing backup: Invalid file format.",

    dataMgmtTitle = "Data Management",
    resetDemoBtn = "Reset & Re-seed Historical Demo Cycles",

    lockScreenTitle = "Aura Cycle Protected",
    lockScreenSubtitle = "100% Offline & Locally Encrypted",
    pinPrompt = "Or enter 4-digit PIN (Default: 1234)",
    pinIncorrectError = "Incorrect PIN. Try again.",

    themeSectionTitle = "Appearance & Color Scheme",
    themeSystem = "System",
    themeLight = "Light",
    themeDark = "Dark",

    fastActionsTitle = "Fast Actions (Mood & Mode)",
    fastActionsSubtitle = "Available by holding the app icon or inside the home screen widget",
    fastMoodHappy = "Happy",
    fastMoodCalm = "Calm & Relaxed",
    fastMoodSensitive = "Sensitive",
    fastMoodEnergetic = "Energetic",
    fastMoodTired = "Exhausted",
    fastModeLoggedMsg = "Mode '%s' logged instantly!",

    updaterTitle = "In-App System Updates & Version",
    updaterSubtitle = "Downloads APK, cleanly closes the app, and invokes the Android System Package Installer",
    checkUpdatesBtn = "Check for Updates",
    downloadInstallBtn = "Download APK & Install via System",
    downloadingProgress = "Downloading: %d%%",
    systemInstallerPrompt = "Closing app and starting System Package Installer...",
    cicdInfoTitle = "GitHub Actions CI/CD Pipeline",
    cicdInfoDesc = "Automated version code & name incrementing on every workflow run, APK packaging, and GitHub Releases.",

    onboardingWelcomeTitle = "Welcome to Aura",
    onboardingWelcomeSubtitle = "Your personal, 100% private & AI-powered cycle companion. Answer a few brief questions to calibrate Aura to your body.",
    onboardingStepOf = "Step %d of %d",
    onboardingGoalTitle = "What is your primary goal?",
    onboardingGoalSubtitle = "Select your main focus so we can tailor predictions and insights",
    onboardingGoal1 = "Track cycle & period",
    onboardingGoal1Desc = "Accurate period start dates and phase predictions",
    onboardingGoal2 = "Conception & fertility",
    onboardingGoal2Desc = "Pinpoint fertile window and ovulation peak",
    onboardingGoal3 = "Understand body & mood",
    onboardingGoal3Desc = "Connect hormones with sleep, stress, and energy",
    onboardingGoal4 = "Pill & contraception",
    onboardingGoal4Desc = "Daily reminder schedules and intake tracking",
    onboardingLastPeriodTitle = "When did your last period start?",
    onboardingLastPeriodSubtitle = "This calibrates the on-device LSTM prediction engine immediately",
    onboardingLastPeriodToday = "Started today",
    onboardingLastPeriodYesterday = "Started yesterday",
    onboardingLastPeriodDaysAgo = "%d days ago",
    onboardingCycleLengthTitle = "How long is your cycle?",
    onboardingCycleLengthSubtitle = "From the first day of bleeding to the day before the next period",
    onboardingCycleRegular = "Regular (±1–2 days)",
    onboardingCycleIrregular = "Variable / Irregular",
    onboardingPeriodDurationTitle = "How many days does bleeding last?",
    onboardingPeriodDurationSubtitle = "Typical number of flow days",
    onboardingSymptomsTitle = "What would you like to track?",
    onboardingSymptomsSubtitle = "Choose your key wellness indicators",
    onboardingFinishTitle = "Your profile is ready! 🌸",
    onboardingFinishSubtitle = "Aura has initialized your personalized LSTM model on-device. All data remains 100% offline, encrypted, and strictly private.",
    onboardingNext = "Continue",
    onboardingBack = "Back",
    onboardingFinish = "Start & Personalize",
    onboardingSkip = "Skip"
)

fun getAppStrings(language: AppLanguage): StringsBundle {
    return when (language) {
        AppLanguage.GERMAN -> GermanStrings
        AppLanguage.ALBANIAN -> AlbanianStrings
        AppLanguage.ENGLISH -> EnglishStrings
    }
}

fun getLocalizedPhaseName(phase: CyclePhase, language: AppLanguage): String {
    return when (language) {
        AppLanguage.GERMAN -> when (phase) {
            CyclePhase.MENSTRUAL -> "Menstruationsphase"
            CyclePhase.FOLLICULAR -> "Follikelphase"
            CyclePhase.OVULATORY -> "Eisprungphase"
            CyclePhase.LUTEAL -> "Lutealphase"
        }
        AppLanguage.ALBANIAN -> when (phase) {
            CyclePhase.MENSTRUAL -> "Faza Menstruale"
            CyclePhase.FOLLICULAR -> "Faza Folikulare"
            CyclePhase.OVULATORY -> "Faza e Ovulacionit"
            CyclePhase.LUTEAL -> "Faza Luteale"
        }
        AppLanguage.ENGLISH -> phase.displayName
    }
}

fun getLocalizedChance(chance: String, language: AppLanguage): String {
    return when (language) {
        AppLanguage.GERMAN -> when (chance) {
            "Peak" -> "Spitze"
            "High" -> "Hoch"
            "Medium" -> "Mittel"
            "Low" -> "Niedrig"
            else -> chance
        }
        AppLanguage.ALBANIAN -> when (chance) {
            "Peak" -> "Kulm"
            "High" -> "E lartë"
            "Medium" -> "Mesatare"
            "Low" -> "E ulët"
            else -> chance
        }
        AppLanguage.ENGLISH -> chance
    }
}

fun getLocalizedTagName(canonicalName: String, language: AppLanguage): String {
    return when (language) {
        AppLanguage.GERMAN -> when (canonicalName) {
            "Happy" -> "Glücklich"
            "Calm" -> "Ruhig"
            "Sensitive" -> "Sensibel"
            "Sad" -> "Traurig"
            "Anxious" -> "Ängstlich"
            "Irritable" -> "Gereizt"
            "Energetic" -> "Energiegeladen"
            "Exhausted" -> "Erschöpft"
            "Moody" -> "Launisch"
            "Confident" -> "Selbstbewusst"
            "Focused" -> "Fokussiert"
            "Vulnerable" -> "Verletzlich"
            "All Good" -> "Alles gut"
            "Cramps" -> "Krämpfe"
            "Headache" -> "Kopfschmerzen"
            "Acne" -> "Akne"
            "Bloating" -> "Blähbauch"
            "Tender Breasts" -> "Brustspannen"
            "Fatigue" -> "Müdigkeit"
            "Back Pain" -> "Rückenschmerzen"
            "Cravings" -> "Heißhunger"
            "Nausea" -> "Übelkeit"
            else -> canonicalName
        }
        AppLanguage.ALBANIAN -> when (canonicalName) {
            "Happy" -> "E lumtur"
            "Calm" -> "E qetë"
            "Sensitive" -> "E ndjeshme"
            "Sad" -> "E mërzitur"
            "Anxious" -> "Në ankth"
            "Irritable" -> "E irrituar"
            "Energetic" -> "Energjike"
            "Exhausted" -> "E rraskapitur"
            "Moody" -> "Luhatje humori"
            "Confident" -> "Me vetëbesim"
            "Focused" -> "E përqendruar"
            "Vulnerable" -> "E cenueshme"
            "All Good" -> "Gjithçka mirë"
            "Cramps" -> "Ngërçe barku"
            "Headache" -> "Dhimbje koke"
            "Acne" -> "Akne"
            "Bloating" -> "Fryrje barku"
            "Tender Breasts" -> "Ndjeshmëri gjoksi"
            "Fatigue" -> "Lodhje"
            "Back Pain" -> "Dhimbje shpine"
            "Cravings" -> "Dëshirë për ushqim"
            "Nausea" -> "Përzierje"
            else -> canonicalName
        }
        AppLanguage.ENGLISH -> canonicalName
    }
}

data class PhaseAdvice(val nutrition: String, val workout: String, val energy: String)

fun getLocalizedPhaseAdvice(phase: CyclePhase, language: AppLanguage): PhaseAdvice {
    return when (language) {
        AppLanguage.GERMAN -> when (phase) {
            CyclePhase.MENSTRUAL -> PhaseAdvice(
                nutrition = "Eisenhaltiges Blattgemüse, warmer Ingwertee und nährende Brühen.",
                workout = "Sanftes regeneratives Yoga, Dehnübungen und entspannte Spaziergänge.",
                energy = "Ruhe und Rückzug priorisieren; auf tiefen, erholsamen Schlaf achten."
            )
            CyclePhase.FOLLICULAR -> PhaseAdvice(
                nutrition = "Fermentierte Lebensmittel, Sprossen und frische vitaminreiche Beeren.",
                workout = "Dynamisches Ausdauertraining, Intervall-Workouts und Krafttraining.",
                energy = "Östrogenspiegel steigt; beste Zeit für kreative Ideen und neue Projekte."
            )
            CyclePhase.OVULATORY -> PhaseAdvice(
                nutrition = "Ballaststoffreiches Gemüse, Antioxidantien und gesunde Omega-3-Samen.",
                workout = "Höchste Leistungsfähigkeit; optimal für schwere Gewichte und Tempoläufe.",
                energy = "Höchstes Selbstvertrauen, soziale Vitalität und strahlende Energie."
            )
            CyclePhase.LUTEAL -> PhaseAdvice(
                nutrition = "Magnesiumreiche dunkle Schokolade, Wurzelgemüse und Kürbiskerne.",
                workout = "Pilates, gezieltes Krafttraining mit Eigengewicht und ruhige Wanderungen.",
                energy = "Progesteronanstieg; schaffe dir eine ruhige Umgebung mit gemütlichem Tempo."
            )
        }
        AppLanguage.ALBANIAN -> when (phase) {
            CyclePhase.MENSTRUAL -> PhaseAdvice(
                nutrition = "Zarzavate me gjethe të pasura me hekur, çaj xhenxhefili i ngrohtë dhe lëngje ushqyese.",
                workout = "Joga e butë ripërtëritëse, frymëmarrje e thellë dhe ecje të qeta.",
                energy = "Kushtojini rëndësi pushimit dhe gjumit të thellë ripërtëritës."
            )
            CyclePhase.FOLLICULAR -> PhaseAdvice(
                nutrition = "Ushqime të fermentuara, fara të mbira dhe fruta të freskëta plot ngjyra.",
                workout = "Kardio me energji të lartë, stërvitje me intervale dhe peshëngritje.",
                energy = "Estrogjeni rritet; koha ideale për ide të reja dhe përqendrim maksimal."
            )
            CyclePhase.OVULATORY -> PhaseAdvice(
                nutrition = "Perime të pasura me fibra, boronica antioksiduese dhe fara omega-3.",
                workout = "Qëndrueshmëri maksimale, stërvitje dinamike dhe vrapim me intensitet.",
                energy = "Vetëbesim maksimal dhe energji e shkëlqyer sociale gjatë gjithë ditës."
            )
            CyclePhase.LUTEAL -> PhaseAdvice(
                nutrition = "Çokollatë e zezë me magnez, perime rrënjore dhe fara kungulli.",
                workout = "Pilates, rezistencë me peshën e trupit dhe ecje në natyrë.",
                energy = "Progesteroni arrin kulmin; pranoni qetësinë dhe ritmin e ngadaltë."
            )
        }
        AppLanguage.ENGLISH -> when (phase) {
            CyclePhase.MENSTRUAL -> PhaseAdvice(
                nutrition = "Iron-rich leafy greens, warm ginger tea, and hydrating broths.",
                workout = "Gentle restorative yoga, deep breathing, and slow walks.",
                energy = "Rest and turn inward; prioritize restorative sleep."
            )
            CyclePhase.FOLLICULAR -> PhaseAdvice(
                nutrition = "Fermented foods, sprouted grains, fresh vibrantly colored fruits.",
                workout = "High-energy cardio, HIIT, and strength lifting sessions.",
                energy = "Estrogen rising; optimal time for brainstorming and learning."
            )
            CyclePhase.OVULATORY -> PhaseAdvice(
                nutrition = "Fiber-rich vegetables, antioxidant berries, omega-3 seeds.",
                workout = "Peak endurance, social group workouts, and power running.",
                energy = "Highest confidence and social vitality of the cycle."
            )
            CyclePhase.LUTEAL -> PhaseAdvice(
                nutrition = "Magnesium-rich dark chocolate, root vegetables, pumpkin seeds.",
                workout = "Pilates, bodyweight resistance, and calm nature walks.",
                energy = "Progesterone peak; embrace calm environments and gentle pacing."
            )
        }
    }
}

fun getLocalizedCalmingBadge(language: AppLanguage): String {
    return when (language) {
        AppLanguage.GERMAN -> "🌸 Raum für Ruhe & Geborgenheit"
        AppLanguage.ALBANIAN -> "🌸 Hapësirë për Qetësi & Siguri"
        AppLanguage.ENGLISH -> "🌸 Safe Sanctuary & Calm"
    }
}

fun getLocalizedCalmingAffirmations(language: AppLanguage): List<String> {
    return when (language) {
        AppLanguage.GERMAN -> listOf(
            "Du bist im vollkommenen Einklang mit deinem Körper. Nimm dir einen tiefen, sanften Atemzug – du bist hier in Sicherheit. 🌸",
            "Höre liebevoll auf die Weisheit deines Körpers. Schenke dir heute Ruhe, Wärme und Mitgefühl. ✨",
            "Dein Wohlbefinden und deine Privatsphäre sind geschützt. Entspanne deine Schultern und lass los. 🤍",
            "Jeder Tag deines Zyklus hat seine eigene Kraft. Sei sanft zu dir selbst und vertraue deinem Rhythmus. 🌷"
        )
        AppLanguage.ALBANIAN -> listOf(
            "Je në harmoni të plotë me trupin tënd. Merr një frymëmarrje të thellë dhe të butë – këtu je e sigurt. 🌸",
            "Dëgjo me dashuri mençurinë e trupit tënd. Fali vetes qetësi, ngrohtësi dhe mirëkuptim sot. ✨",
            "Privatësia dhe shëndeti yt janë plotësisht të mbrojtura. Liro shpatullat dhe qetëso mendjen. 🤍",
            "Çdo fazë e ciklit tënd mbart bukurinë dhe forcën e vet. Ji e butë me veten dhe beso ritmin tënd. 🌷"
        )
        AppLanguage.ENGLISH -> listOf(
            "You are in tune with your body's rhythm. Take a gentle, deep breath — you are completely safe here. 🌸",
            "Listen with kindness to your body's wisdom. Give yourself peace, warmth, and grace today. ✨",
            "Your privacy and sacred space are protected. Relax your shoulders and exhale softly. 🤍",
            "Every day of your cycle holds its own quiet strength. Be gentle with yourself and trust your journey. 🌷"
        )
    }
}

