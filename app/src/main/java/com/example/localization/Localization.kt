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
    appName = "Tezet erdhen",
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

    lockScreenTitle = "Tezet erdhen Geschützt",
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
    appName = "Tezet erdhen",
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

    lockScreenTitle = "Tezet erdhen E Mbrojtur",
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
    appName = "Tezet erdhen",
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

    lockScreenTitle = "Tezet erdhen Protected",
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

data class PhaseMeaningInfo(
    val phase: CyclePhase,
    val title: String,
    val dayRangeText: String,
    val colorHex: Long,
    val emoji: String,
    val shortMeaning: String,
    val hormoneSummary: String,
    val bodySignals: String,
    val nutritionTip: String,
    val activityTip: String
)

fun getLocalizedPhaseMeaning(phase: CyclePhase, language: AppLanguage): PhaseMeaningInfo {
    return when (language) {
        AppLanguage.GERMAN -> when (phase) {
            CyclePhase.MENSTRUAL -> PhaseMeaningInfo(
                phase = phase,
                title = "Menstruationsphase (Rot)",
                dayRangeText = "Ca. Tag 1 - 5 des Zyklus",
                colorHex = 0xFFF43F5E,
                emoji = "🩸",
                shortMeaning = "Der Beginn des Zyklus. Die Gebärmutterschleimhaut erneuert sich. Dein Körper leistet wertvolle Arbeit und verdient jetzt Ruhe, Wärme und Selbstfürsorge.",
                hormoneSummary = "Östrogen & Progesteron auf Tiefststand",
                bodySignals = "Blutung, mäßige Krämpfe, erhöhtes Schlafbedürfnis",
                nutritionTip = "Warme Suppen, Ingwertee & eisenhaltiges Gemüse",
                activityTip = "Sanftes Dehnen, Spaziergänge & erholsame Pausen"
            )
            CyclePhase.FOLLICULAR -> PhaseMeaningInfo(
                phase = phase,
                title = "Follikelphase (Lila)",
                dayRangeText = "Ca. Tag 6 - 13 des Zyklus",
                colorHex = 0xFFA855F7,
                emoji = "💜",
                shortMeaning = "Die Eizellreifung beginnt. Der Östrogenspiegel steigt spürbar an, bringt neue Lebensfreude, Tatdrang und frische Ideen.",
                hormoneSummary = "Östrogen steigt stetig an",
                bodySignals = "Mehr Elan, klare Haut, steigende Ausdauer",
                nutritionTip = "Frisches Obst, Sprossen & ballaststoffreiche Kost",
                activityTip = "Dynamisches Ausdauertraining & neue Projekte starten"
            )
            CyclePhase.OVULATORY -> PhaseMeaningInfo(
                phase = phase,
                title = "Eisprung & Fruchtbarkeit (Türkis)",
                dayRangeText = "Ca. Tag 14 - 16 des Zyklus",
                colorHex = 0xFF0D9488,
                emoji = "🩵",
                shortMeaning = "Das Ei wird freigesetzt – dies ist das fruchtbare Fenster des Zyklus mit der höchsten Empfängniswahrscheinlichkeit.",
                hormoneSummary = "Östrogenspitze & LH-Anstieg",
                bodySignals = "Spitzenenergie, flüssiger Zervikalschleim, hohe Attraktivität",
                nutritionTip = "Beeren, Antioxidantien & gesunde Omega-3-Fette",
                activityTip = "Intensives Krafttraining, Sport & soziale Kontakte"
            )
            CyclePhase.LUTEAL -> PhaseMeaningInfo(
                phase = phase,
                title = "Lutealphase (Bernstein)",
                dayRangeText = "Ca. Tag 17 - 28 des Zyklus",
                colorHex = 0xFFF59E0B,
                emoji = "🟠",
                shortMeaning = "Progesteron übernimmt die Regie, um den Körper vorzubereiten. Das Tempo verlangsamt sich natürlich vor der nächsten Periode.",
                hormoneSummary = "Progesteron-Dominanz",
                bodySignals = "Gesteigerter Appetit, Bedürfnis nach Struktur, evtl. PMS",
                nutritionTip = "Magnesiumhaltige Nüsse, Kürbiskerne & Wurzelgemüse",
                activityTip = "Pilates, Entspannung & gemütliche Abendroutinen"
            )
        }
        AppLanguage.ALBANIAN -> when (phase) {
            CyclePhase.MENSTRUAL -> PhaseMeaningInfo(
                phase = phase,
                title = "Faza e Menstruacioneve (E Kuqe)",
                dayRangeText = "Përafërsisht Ditët 1 - 5 të Ciklit",
                colorHex = 0xFFF43F5E,
                emoji = "🩸",
                shortMeaning = "Fillimi i ciklit të ri. Mukoza e mitrës rinovohet. Trupi ka nevojë për qetësi, ngrohtësi dhe kujdes të veçantë.",
                hormoneSummary = "Estrogjeni & Progesteroni në nivel minimal",
                bodySignals = "Gjakderdhje, lehtësi për pushim",
                nutritionTip = "Ushqime të ngrohta, çaj dhe zarzavate me hekur",
                activityTip = "Ecje të qeta dhe riatdhesim energjie"
            )
            CyclePhase.FOLLICULAR -> PhaseMeaningInfo(
                phase = phase,
                title = "Faza Follikulare (Vjollcë)",
                dayRangeText = "Përafërsisht Ditët 6 - 13 të Ciklit",
                colorHex = 0xFFA855F7,
                emoji = "💜",
                shortMeaning = "Veza fillon të piqet. Niveli i estrogjenit rritet, duke sjellë energji të re, kreativitet dhe motivim të lartë.",
                hormoneSummary = "Rritje e vazhdueshme e estrogjenit",
                bodySignals = "Energji në rritje, lëkurë e pastër, përqendrim",
                nutritionTip = "Fruta të freskëta dhe perime plot vitamina",
                activityTip = "Stërvitje dinamike dhe fokus në ide të reja"
            )
            CyclePhase.OVULATORY -> PhaseMeaningInfo(
                phase = phase,
                title = "Ovulacioni & Fertiliteti (Turkez)",
                dayRangeText = "Përafërsisht Ditët 14 - 16 të Ciklit",
                colorHex = 0xFF0D9488,
                emoji = "🩵",
                shortMeaning = "Lirimi i vezës markon dritaren më fertile të ciklit me gjasat më të larta për shtatzëni.",
                hormoneSummary = "Kulmi i estrogjenit dhe hormonit LH",
                bodySignals = "Vetëbesim maksimal, energji fizike kulmore",
                nutritionTip = "Ushqime me fibra dhe yndyra të shëndetshme Omega-3",
                activityTip = "Aktivitet intensiv fizik dhe sociale"
            )
            CyclePhase.LUTEAL -> PhaseMeaningInfo(
                phase = phase,
                title = "Faza Luteale (Ngjyrë Qelibari)",
                dayRangeText = "Përafërsisht Ditët 17 - 28 të Ciklit",
                colorHex = 0xFFF59E0B,
                emoji = "🟠",
                shortMeaning = "Progesteroni dominon për të përgatitur trupin. Ritmi i trupit ngadalësohet natyrshëm para ciklit të ardhshëm.",
                hormoneSummary = "Dominimi i progesteronit",
                bodySignals = "Apetit i shtuar, dëshirë për organizim",
                nutritionTip = "Çokollatë e zezë me magnez dhe fara kungulli",
                activityTip = "Pilates, stërvitje me peshën e trupit dhe qetësi"
            )
        }
        AppLanguage.ENGLISH -> when (phase) {
            CyclePhase.MENSTRUAL -> PhaseMeaningInfo(
                phase = phase,
                title = "Menstrual Phase (Red)",
                dayRangeText = "Approx. Days 1 - 5 of Cycle",
                colorHex = 0xFFF43F5E,
                emoji = "🩸",
                shortMeaning = "The start of a new cycle. The uterine lining sheds and renews. Your body deserves gentle rest and warmth.",
                hormoneSummary = "Estrogen & Progesterone at lowest levels",
                bodySignals = "Flow, mild cramping, need for restorative sleep",
                nutritionTip = "Warm broths, ginger tea & iron-rich greens",
                activityTip = "Gentle stretching, slow walks & cozy self-care"
            )
            CyclePhase.FOLLICULAR -> PhaseMeaningInfo(
                phase = phase,
                title = "Follicular Phase (Purple)",
                dayRangeText = "Approx. Days 6 - 13 of Cycle",
                colorHex = 0xFFA855F7,
                emoji = "💜",
                shortMeaning = "Follicles mature in the ovaries. Rising estrogen brings a surge of vitality, mental clarity, and creative motivation.",
                hormoneSummary = "Estrogen continuously rising",
                bodySignals = "Rising energy, clear skin, social enthusiasm",
                nutritionTip = "Fresh berries, sprouted grains & vibrant fruits",
                activityTip = "Cardio, strength workouts & starting new projects"
            )
            CyclePhase.OVULATORY -> PhaseMeaningInfo(
                phase = phase,
                title = "Ovulation & Fertile Window (Teal)",
                dayRangeText = "Approx. Days 14 - 16 of Cycle",
                colorHex = 0xFF0D9488,
                emoji = "🩵",
                shortMeaning = "Egg release occurs during this peak fertile window, offering the highest chance of pregnancy in the cycle.",
                hormoneSummary = "Peak Estrogen & LH surge",
                bodySignals = "Peak confidence, high stamina & radiant glow",
                nutritionTip = "Antioxidant berries, fiber & omega-3 healthy fats",
                activityTip = "High-intensity workouts & vibrant social activities"
            )
            CyclePhase.LUTEAL -> PhaseMeaningInfo(
                phase = phase,
                title = "Luteal Phase (Amber)",
                dayRangeText = "Approx. Days 17 - 28 of Cycle",
                colorHex = 0xFFF59E0B,
                emoji = "🟠",
                shortMeaning = "Progesterone rises to prepare the uterine lining. Body energy naturally winds down into focus and calm organization.",
                hormoneSummary = "Progesterone dominance",
                bodySignals = "Appetite changes, desire for quiet, possible PMS",
                nutritionTip = "Magnesium-rich dark chocolate, seeds & root veggies",
                activityTip = "Pilates, bodyweight exercise & relaxing evening routines"
            )
        }
    }
}

data class ScientificPhaseGuide(
    val phase: CyclePhase,
    val phaseTitle: String,
    val dayRangeText: String,
    val emoji: String,
    val colorHex: Long,
    val physiologyTitle: String,
    val hormonesAndPhysiology: String,
    val moodAndEnergy: String,
    val nutritionTitle: String,
    val nutritionHighlights: List<String>,
    val nutritionDetails: List<Pair<String, String>>,
    val metabolismFact: String,
    val activityTitle: String,
    val activityLevelLabel: String,
    val activityHighlights: List<String>,
    val activityDetails: List<Pair<String, String>>
)

fun getScientificPhaseGuide(phase: CyclePhase, language: AppLanguage): ScientificPhaseGuide {
    return when (language) {
        AppLanguage.GERMAN -> when (phase) {
            CyclePhase.MENSTRUAL -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Menstruationsphase",
                dayRangeText = "Ca. Tag 1–5",
                emoji = "🩸",
                colorHex = 0xFFF43F5E,
                physiologyTitle = "Physiologie & Hormone",
                hormonesAndPhysiology = "Östrogen & Progesteron auf Tiefststand. Lokale Prostaglandine bewirken Gebärmutterkontraktionen (Krämpfe). Blutverlust von ca. 30–60 ml führt zu Eisenverlust. Magen-Darm-Motilität verlangsamt.",
                moodAndEnergy = "Niedriges Energielevel, erhöhtes Ruhebedürfnis, Anfälligkeit für Erschöpfung und Krämpfe.",
                nutritionTitle = "Wissenschaftliche Ernährungsempfehlungen",
                nutritionHighlights = listOf("Eisen & Vit C", "Magnesium", "Omega-3", "Bekömmlich"),
                nutritionDetails = listOf(
                    "Eisen & Vitamin C" to "Pflanzliches & tierisches Eisen (Linsen, Kichererbsen, Kürbiskerne, mageres Fleisch) kombiniert mit Vitamin C (Paprika, Zitrusfrüchte) für maximale Eisenaufnahme.",
                    "Magnesium" to "Wirkt muskelentspannend und krampflindernd (Dunkle Schokolade, Nüsse, Haferflocken).",
                    "Omega-3-Fettsäuren" to "Hemmen die Synthese entzündungsfördernder Prostaglandine & reduzieren Schmerzintensität (Leinsamen, Walnüsse, fetter Seefisch).",
                    "Bekömmlichkeit" to "Warme, leicht verdauliche Speisen (Suppen, Eintöpfe), da die Magen-Darm-Motilität verlangsamt sein kann."
                ),
                metabolismFact = "Fokus auf Schmerzlinderung, Regeneration & Eisen-Auffüllung statt Kalorienrestriktion.",
                activityTitle = "Physiologische Aktivitätsvorschläge",
                activityLevelLabel = "Sanft & Regenerativ",
                activityHighlights = listOf("Restorative Yoga", "Sanftes Dehnen", "Spaziergänge", "Schlaf"),
                activityDetails = listOf(
                    "Bewegungstyp" to "Sanftes Dehnen, leichtes Mobilisationstraining & entspannte Spaziergänge an frischer Luft.",
                    "Fokus" to "Schonung des Beckenbodens, Entspannung der Gebärmutter und vorrangige Erholung."
                )
            )
            CyclePhase.FOLLICULAR -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Follikelphase",
                dayRangeText = "Ca. Tag 6–13",
                emoji = "💜",
                colorHex = 0xFFA855F7,
                physiologyTitle = "Physiologie & Hormone",
                hormonesAndPhysiology = "FSH & Östrogen steigen kontinuierlich an. Östrogen verbessert die Insulinsensitivität und optimiert die Speicherung von Muskelglykogen.",
                moodAndEnergy = "Steigendes Energielevel, hohe mentale Schärfe, verbesserte Stimmung und höhere Stresstoleranz.",
                nutritionTitle = "Wissenschaftliche Ernährungsempfehlungen",
                nutritionHighlights = listOf("Komplexe Carbs", "Präbiotika & Ballaststoffe", "Zink"),
                nutritionDetails = listOf(
                    "Komplexe Kohlenhydrate" to "Vollkornprodukte, Hafer und Quinoa werden durch die verbesserte Insulinsensitivität optimal verwertet.",
                    "Präbiotika & Ballaststoffe" to "Unterstützen die Darmflora (Östrobolom) für den geregelten Abbau & Ausscheidung von überschüssigem Östrogen.",
                    "Zink" to "Unterstützt die Zellteilung & Eizellreifung (Kürbiskerne, Hülsenfrüchte, Vollkorn)."
                ),
                metabolismFact = "Optimale Kohlenhydratverwertung & Muskelglykogenspeicherung durch hohe Insulinsensitivität.",
                activityTitle = "Physiologische Aktivitätsvorschläge",
                activityLevelLabel = "Hohe Energie & Kraftaufbau",
                activityHighlights = listOf("HIIT & Ausdauer", "Krafttraining Peak", "Muskelaufbau", "Neue Ziele"),
                activityDetails = listOf(
                    "Bewegungstyp" to "Dynamisches Ausdauertraining, Intervall-Workouts (HIIT) & intensives Krafttraining.",
                    "Fokus" to "Beste Phase für Muskelaufbau, Kraftzuwachs & neue sportliche Herausforderungen."
                )
            )
            CyclePhase.OVULATORY -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Ovulationsphase (Eisprung)",
                dayRangeText = "Ca. Tag 14",
                emoji = "🩵",
                colorHex = 0xFF0D9488,
                physiologyTitle = "Physiologie & Hormone",
                hormonesAndPhysiology = "Östrogen erreicht seinen Höchststand, gefolgt von einem steilen LH-Peak und kurzem Testosteronanstieg.",
                moodAndEnergy = "Höchste Vitalität, gesteigertes Selbstvertrauen, oft temporär gedämpfter Appetit durch den Östrogen-Peak.",
                nutritionTitle = "Wissenschaftliche Ernährungsempfehlungen",
                nutritionHighlights = listOf("Antioxidantien", "Leichte Proteine", "Darmgesundheit"),
                nutritionDetails = listOf(
                    "Antioxidantien" to "Beeren, grünes Blattgemüse und Nüsse schützen die Zellen vor dem oxidativen Stress beim Eisprung.",
                    "Leichte Proteine" to "Fisch, Tofu, Eier oder Hülsenfrüchte zur Erhaltung des Sättigungsgefühls."
                ),
                metabolismFact = "Östrogenspitze dämpft den Appetit naturally bei maximaler physischer Leistungsfähigkeit.",
                activityTitle = "Physiologische Aktivitätsvorschläge",
                activityLevelLabel = "Peak Performance & Vitalität",
                activityHighlights = listOf("Maximalkraft (PRs)", "Tempoläufe", "Group Fitness", "Power"),
                activityDetails = listOf(
                    "Bewegungstyp" to "Maximalkraft (Personal Records), Tempoläufe, schwere Kniebeugen & schweißtreibende Gruppenworkouts.",
                    "Fokus" to "Höchste physische Ausstrahlung & Kraft; achte auf Gelenkstabilität (Einfluss von Relaxin)."
                )
            )
            CyclePhase.LUTEAL -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Lutealphase",
                dayRangeText = "Ca. Tag 15–28",
                emoji = "🟠",
                colorHex = 0xFFF59E0B,
                physiologyTitle = "Physiologie & Hormone",
                hormonesAndPhysiology = "Progesteron dominiert. Basaltemperatur steigt um 0,3–0,5 °C. Ruheenergiebedarf (REE) steigt nachweislich um 100–300 kcal/Tag. Insulinsensitivität nimmt leicht ab.",
                moodAndEnergy = "Progesteron & spätes Absinken beider Hormone senken den Serotoninspiegel -> Reizbarkeit, Stimmungstiefs, PMS & Cravings.",
                nutritionTitle = "Wissenschaftliche Ernährungsempfehlungen",
                nutritionHighlights = listOf("+150–250 kcal Plus", "Vit B6 & Magnesium", "Blutzucker-Balance"),
                nutritionDetails = listOf(
                    "Kalorienplus einplanen" to "Ein moderates Plus von 150–250 kcal/Tag aus hochwertigen Quellen (Nüsse, Avocado, komplexe Carbs) verhindert Heißhungerattacken.",
                    "Vitamin B6 & Magnesium" to "Essenziell als Kofaktoren für Serotonin- & Dopaminsynthese. Studien zeigen Linderung von PMS & Reizbarkeit (Kichererbsen, Bananen, Kartoffeln, dunkler Kakao).",
                    "Blutzuckerstabilisierung" to "Mahlzeiten mit niedrigem glykämischem Index und ausreichend Protein kombinieren."
                ),
                metabolismFact = "Grundumsatz steigt um 100-300 kcal/Tag! Ein leichtes Kalorienplus verhindert PMS-Heißhunger.",
                activityTitle = "Physiologische Aktivitätsvorschläge",
                activityLevelLabel = "Moderates Training & Entschleunigung",
                activityHighlights = listOf("Pilates", "Eigengewicht", "Wanderungen", "Entspannung"),
                activityDetails = listOf(
                    "Bewegungstyp" to "Pilates, gezieltes Krafttraining mit Eigengewicht, zügige Wanderungen & Entspannungstechniken.",
                    "Fokus" to "Cortisol senken, Blutzucker stabilisieren & den Körper sanft auf die Regeneration vorbereiten."
                )
            )
        }
        AppLanguage.ALBANIAN -> when (phase) {
            CyclePhase.MENSTRUAL -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Faza e Menstruacioneve",
                dayRangeText = "Ditët 1–5",
                emoji = "🩸",
                colorHex = 0xFFF43F5E,
                physiologyTitle = "Fiziologjia & Hormonet",
                hormonesAndPhysiology = "Estrogjeni & Progesteroni në nivel minimal. Humbja e gjakut sjell humbje të hekurit. Motiliteti i zorrëve ngadalësohet.",
                moodAndEnergy = "Energji e ulët, nevojë për pushim, prirje për plogështi dhe ngërçe.",
                nutritionTitle = "Rekomandime Ushqimore Shkencore",
                nutritionHighlights = listOf("Hekur & Vit C", "Magnez", "Omega-3", "Ushqim i Ngrohtë"),
                nutritionDetails = listOf(
                    "Hekur & Vitaminë C" to "Lente, thjerrëza, fara kungulli kombinuar me vitaminë C për përthithje maksimale të hekurit.",
                    "Magnez" to "Efect ushqyes për qetësimin e muskujve dhe ngërçeve (çokollatë e zezë, tëra, bajame).",
                    "Omega-3" to "Ulin inflamacionin dhe zbusin dhimbjet (fara liri, arra, peshk).",
                    "Ushqime të Ngrohta" to "Supa dhe gjellë të ngrohta për tretje më të lehtë."
                ),
                metabolismFact = "Fokus në riatdhesim të hekurit dhe qetësim dhimbjesh.",
                activityTitle = "Aktiviteti Fizik i Sugjeruar",
                activityLevelLabel = "Butësi & Ripërtëritje",
                activityHighlights = listOf("Joga e Butë", "Shtriqje", "Ecje të Qeta", "Pushim"),
                activityDetails = listOf(
                    "Lloji i Lëvizjes" to "Shtriqje të lehta, ecje në ajër të pastër dhe joga qetësuese.",
                    "Fokusi" to "Relaksim i trupit dhe pushim rikuperues."
                )
            )
            CyclePhase.FOLLICULAR -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Faza Follikulare",
                dayRangeText = "Ditët 6–13",
                emoji = "💜",
                colorHex = 0xFFA855F7,
                physiologyTitle = "Fiziologjia & Hormonet",
                hormonesAndPhysiology = "Estrogjeni rritet. Ndjeshmëria ndaj insulinës përmirësohet dhe ruajtja e glikogjenit optimizohet.",
                moodAndEnergy = "Rritje e energjisë, përqendrim i lartë mendor dhe humor i shkëlqyer.",
                nutritionTitle = "Rekomandime Ushqimore Shkencore",
                nutritionHighlights = listOf("Karbohidrate Komplekse", "Prebiotikë", "Zink"),
                nutritionDetails = listOf(
                    "Karbohidrate Komplekse" to "Dredhëza, tëra, kuinoa përdoren shkëlqyeshëm nga trupi.",
                    "Prebiotikë & Fibra" to "Mbrojnë mikrobiomën e zorrëve për degradim optimal të estrogjenit.",
                    "Zink" to "Mbështet pjekjen e vezës dhe ndarjen qelizore."
                ),
                metabolismFact = "Përdorim optimal i karbohidrateve falë ndjeshmërisë së lartë ndaj insulinës.",
                activityTitle = "Aktiviteti Fizik i Sugjeruar",
                activityLevelLabel = "Energji e Lartë & Stërvitje",
                activityHighlights = listOf("HIIT & Kardio", "Peshëngritje", "Rritje Muskujsh"),
                activityDetails = listOf(
                    "Lloji i Lëvizjes" to "Stërvitje kardio intensive, HIIT dhe peshëngritje.",
                    "Fokusi" to "Rritje e forcës fizike dhe sfidim i rekordeve personale."
                )
            )
            CyclePhase.OVULATORY -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Faza e Ovulacionit",
                dayRangeText = "Dita 14",
                emoji = "🩵",
                colorHex = 0xFF0D9488,
                physiologyTitle = "Fiziologjia & Hormonet",
                hormonesAndPhysiology = "Kulmi i estrogjenit, niveli i lartë i hormonit LH dhe rritje e lehtë e testosteronit.",
                moodAndEnergy = "Vitalitet maksimal, vetëbesim i lartë, oreks natyrshëm i kontrolluar.",
                nutritionTitle = "Rekomandime Ushqimore Shkencore",
                nutritionHighlights = listOf("Antioksidues", "Proteina të Lehta", "Fibra"),
                nutritionDetails = listOf(
                    "Antioksidues" to "Fruta mali, perime jeshile dhe arra mbrojnë qelizat.",
                    "Proteina të Lehta" to "Peshk, vezë, tofut për ngopje të qëndrueshme."
                ),
                metabolismFact = "Niveli i lartë i estrogjenit mban oreksin të balancuar gjatë pikës kulmore.",
                activityTitle = "Aktiviteti Fizik i Sugjeruar",
                activityLevelLabel = "Performancë Maksimale",
                activityHighlights = listOf("Forcë Maksimale", "Vrapim i Shpejtë", "Performancë"),
                activityDetails = listOf(
                    "Lloji i Lëvizjes" to "Ushtrime me pesha të rënda, vrapim dinamik dhe stërvitje me grup.",
                    "Fokusi" to "Energji maksimale fizike."
                )
            )
            CyclePhase.LUTEAL -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Faza Luteale",
                dayRangeText = "Ditët 15–28",
                emoji = "🟠",
                colorHex = 0xFFF59E0B,
                physiologyTitle = "Fiziologjia & Hormonet",
                hormonesAndPhysiology = "Progesteroni dominos. Temperatura e trupit rritet me 0.3-0.5 °C. Metobolizmi bazal (REE) rritet me 100-300 kcal/ditë.",
                moodAndEnergy = "Rënia e hormoneve ul serotoninën -> prirje për ngacmim, PMS dhe dëshirë për karbohidrate.",
                nutritionTitle = "Rekomandime Ushqimore Shkencore",
                nutritionHighlights = listOf("+150–250 kcal Plus", "Vit B6 & Magnez", "Balancë e Sheqerit"),
                nutritionDetails = listOf(
                    "Planifikim i Kalorive" to "Shtim me 150–250 kcal/ditë nga yndyra të shëndetshme (arra, avokado) parandalon sulmet e urisë.",
                    "Vitaminë B6 & Magnez" to "Essenjale për sintezën e serotoninës dhe zbutjen e simptomave të PMS.",
                    "Stabilizim i Sheqerit" to "Kombinim i proteinave me karbohidrate me indeks të ulët glicemik."
                ),
                metabolismFact = "Harxhimi i energjisë rritet me 100-300 kcal/ditë! Calorie plus i lehtë mbron nga PMS.",
                activityTitle = "Aktiviteti Fizik i Sugjeruar",
                activityLevelLabel = "Stërvitje Moderuar & Qetësi",
                activityHighlights = listOf("Pilates", "Pesha të Lehta", "Ecje në Natyrë"),
                activityDetails = listOf(
                    "Lloji i Lëvizjes" to "Pilates, stërvitje me peshën e trupit dhe ecje qetësuese.",
                    "Fokusi" to "Ulj e kortizolit dhe përgatitje për pushim."
                )
            )
        }
        AppLanguage.ENGLISH -> when (phase) {
            CyclePhase.MENSTRUAL -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Menstrual Phase",
                dayRangeText = "Approx. Days 1–5",
                emoji = "🩸",
                colorHex = 0xFFF43F5E,
                physiologyTitle = "Physiology & Hormones",
                hormonesAndPhysiology = "Estrogen & Progesterone at lowest levels. Local prostaglandins cause uterine contractions. Blood loss (30-60 ml) leads to iron loss.",
                moodAndEnergy = "Low energy level, high need for rest, susceptibility to fatigue and cramps.",
                nutritionTitle = "Evidence-Based Nutrition Guidance",
                nutritionHighlights = listOf("Iron & Vit C", "Magnesium", "Omega-3", "Warm Foods"),
                nutritionDetails = listOf(
                    "Iron & Vitamin C" to "Plant/animal iron (lentils, chickpeas, pumpkin seeds, lean meat) combined with Vitamin C for max absorption.",
                    "Magnesium" to "Relaxes muscles & eases cramping (dark chocolate, nuts, oats).",
                    "Omega-3 Fatty Acids" to "Inhibit inflammatory prostaglandins & reduce pain intensity (flaxseeds, walnuts, oily fish).",
                    "Digestibility" to "Warm, easily digestible meals (soups, stews) as gut motility slows down."
                ),
                metabolismFact = "Focus on pain relief, recovery & replenishing iron rather than calorie restriction.",
                activityTitle = "Physiological Activity Suggestions",
                activityLevelLabel = "Gentle & Restorative",
                activityHighlights = listOf("Restorative Yoga", "Gentle Stretch", "Walks", "Sleep"),
                activityDetails = listOf(
                    "Movement Type" to "Gentle stretching, light mobility, and relaxing outdoor walks.",
                    "Focus" to "Pelvic floor care, uterine relaxation, and prioritizing restorative sleep."
                )
            )
            CyclePhase.FOLLICULAR -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Follicular Phase",
                dayRangeText = "Approx. Days 6–13",
                emoji = "💜",
                colorHex = 0xFFA855F7,
                physiologyTitle = "Physiology & Hormones",
                hormonesAndPhysiology = "FSH & Estrogen continuously rise. Estrogen improves insulin sensitivity and optimizes muscle glycogen storage.",
                moodAndEnergy = "Rising energy levels, sharp mental focus, elevated mood, and stress tolerance.",
                nutritionTitle = "Evidence-Based Nutrition Guidance",
                nutritionHighlights = listOf("Complex Carbs", "Prebiotics & Fiber", "Zinc"),
                nutritionDetails = listOf(
                    "Complex Carbohydrates" to "Whole grains, oats, and quinoa are optimally utilized due to high insulin sensitivity.",
                    "Prebiotics & Fiber" to "Support gut microbiome (estrobolome) for regulated estrogen breakdown.",
                    "Zinc" to "Supports cell division & egg follicle maturation (seeds, legumes)."
                ),
                metabolismFact = "Optimal carbohydrate utilization & muscle glycogen storage due to high insulin sensitivity.",
                activityTitle = "Physiological Activity Suggestions",
                activityLevelLabel = "High Energy & Strength Building",
                activityHighlights = listOf("HIIT & Cardio", "Strength Peak", "Muscle Growth", "New Goals"),
                activityDetails = listOf(
                    "Movement Type" to "Dynamic cardio, interval workouts (HIIT), and heavy strength training.",
                    "Focus" to "Prime phase for muscle hypertrophy, strength gains, and athletic PRs."
                )
            )
            CyclePhase.OVULATORY -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Ovulation Phase",
                dayRangeText = "Approx. Day 14",
                emoji = "🩵",
                colorHex = 0xFF0D9488,
                physiologyTitle = "Physiology & Hormones",
                hormonesAndPhysiology = "Estrogen reaches peak level, followed by a sharp LH surge and brief testosterone rise.",
                moodAndEnergy = "Peak vitality, heightened confidence, temporarily suppressed appetite from estrogen peak.",
                nutritionTitle = "Evidence-Based Nutrition Guidance",
                nutritionHighlights = listOf("Antioxidants", "Lean Proteins", "Satiety"),
                nutritionDetails = listOf(
                    "Antioxidants" to "Berries, leafy greens, and nuts protect cells against oxidative stress during ovulation.",
                    "Lean Proteins" to "Fish, tofu, eggs, or legumes for steady, clean satiety."
                ),
                metabolismFact = "Estrogen peak naturally suppresses appetite during peak physical stamina.",
                activityTitle = "Physiological Activity Suggestions",
                activityLevelLabel = "Peak Performance & Vitality",
                activityHighlights = listOf("Max Strength (PRs)", "Tempo Runs", "Group Fitness", "Power"),
                activityDetails = listOf(
                    "Movement Type" to "Max strength lifts, high-intensity intervals, and power group fitness.",
                    "Focus" to "Peak physical confidence & stamina; mind joint stability due to relaxin."
                )
            )
            CyclePhase.LUTEAL -> ScientificPhaseGuide(
                phase = phase,
                phaseTitle = "Luteal Phase",
                dayRangeText = "Approx. Days 15–28",
                emoji = "🟠",
                colorHex = 0xFFF59E0B,
                physiologyTitle = "Physiology & Hormones",
                hormonesAndPhysiology = "Progesterone dominates. Basal body temp rises by 0.3–0.5°C. Resting Energy Expenditure (REE) rises by 100–300 kcal/day.",
                moodAndEnergy = "Progesterone drop lowers serotonin levels -> irritability, PMS & carbohydrate cravings.",
                nutritionTitle = "Evidence-Based Nutrition Guidance",
                nutritionHighlights = listOf("+150–250 kcal Surplus", "Vit B6 & Magnesium", "Blood Sugar Balance"),
                nutritionDetails = listOf(
                    "Plan Calorie Surplus" to "A moderate 150–250 kcal/day surplus from quality sources (nuts, avocado, complex carbs) prevents intense cravings.",
                    "Vitamin B6 & Magnesium" to "Essential cofactors for serotonin & dopamine synthesis. Eases PMS & irritability (chickpeas, bananas, cocoa).",
                    "Blood Sugar Stability" to "Pair low glycemic index carbohydrates with quality protein."
                ),
                metabolismFact = "Resting metabolic rate rises by 100-300 kcal/day! A light calorie surplus prevents PMS binge cravings.",
                activityTitle = "Physiological Activity Suggestions",
                activityLevelLabel = "Moderate Training & Recovery",
                activityHighlights = listOf("Pilates", "Bodyweight", "Nature Walks", "De-stress"),
                activityDetails = listOf(
                    "Movement Type" to "Pilates, bodyweight resistance, brisk nature walks, and restorative breathwork.",
                    "Focus" to "Lowering cortisol, stabilizing blood sugar, and preparing for period recovery."
                )
            )
        }
    }
}

enum class AppPersona(
    val code: String,
    val icon: String,
    val nameDe: String,
    val nameSq: String,
    val nameEn: String,
    val subtitleDe: String,
    val subtitleSq: String,
    val subtitleEn: String
) {
    LOVING(
        code = "loving",
        icon = "🌸",
        nameDe = "Liebevoll",
        nameSq = "E dashur",
        nameEn = "Loving",
        subtitleDe = "Warm, einfühlsam & unterstützend",
        subtitleSq = "E ngrohtë, e ndjeshme & mbështetëse",
        subtitleEn = "Warm, caring & supportive"
    ),
    SARCASTIC(
        code = "sarcastic",
        icon = "🥂",
        nameDe = "Sarkastisch",
        nameSq = "Sarkastike",
        nameEn = "Sarcastic",
        subtitleDe = "Witzig, direkt & unerschrocken",
        subtitleSq = "Witty, direkte & pa doreza",
        subtitleEn = "Witty, blunt & unapologetic"
    ),
    LOGICAL(
        code = "logical",
        icon = "📊",
        nameDe = "Logisch",
        nameSq = "Logike",
        nameEn = "Logical",
        subtitleDe = "Klinisch, präzise & datenbasiert",
        subtitleSq = "Klinike, e saktë & e bazuar në të dhëna",
        subtitleEn = "Clinical, precise & data-driven"
    ),
    FUNNY(
        code = "funny",
        icon = "🤪",
        nameDe = "Humorvoll",
        nameSq = "Humoristike",
        nameEn = "Funny",
        subtitleDe = "Locker, spritzig & spaßig",
        subtitleSq = "E lirëshme, gazmore & argëtuese",
        subtitleEn = "Playful, energetic & jovial"
    );

    fun getLocalizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.GERMAN -> nameDe
        AppLanguage.ALBANIAN -> nameSq
        AppLanguage.ENGLISH -> nameEn
    }

    fun getLocalizedSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.GERMAN -> subtitleDe
        AppLanguage.ALBANIAN -> subtitleSq
        AppLanguage.ENGLISH -> subtitleEn
    }

    companion object {
        fun fromCode(code: String): AppPersona {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: LOVING
        }
    }
}

fun getPersonaMessage(persona: AppPersona, phase: CyclePhase, language: AppLanguage): String {
    return when (persona) {
        AppPersona.LOVING -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Du machst das großartig. Gönn dir heute ganz viel Ruhe, warme Suppe und eine Wärmflasche 🌸."
                CyclePhase.FOLLICULAR -> "Deine Energie steigt wunderschön an! Genieße die kreativen Schübe und frischen Ideen ✨."
                CyclePhase.OVULATORY -> "Du strahlst von innen heraus! Eine wunderbare Zeit für soziale Momente und Selbstvertrauen 💕."
                CyclePhase.LUTEAL -> "Höre jetzt besonders sanft auf deinen Körper. Mach es dir gemütlich und pass gut auf dich auf ☕."
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Je duke bërë një punë të mrekullueshme. Kujdesu për veten sot, çaj i ngrohtë dhe qetësi 🌸."
                CyclePhase.FOLLICULAR -> "Energjia jote po rritet bukur! Shijo idetë e reja dhe shpërthimin e kreativitetit ✨."
                CyclePhase.OVULATORY -> "Po shkëlqen nga brenda! Kohë e mrekullueshme për shoqëri dhe vetëbesim 💕."
                CyclePhase.LUTEAL -> "Dëgjo trupin tënd me shumë kujdes tani. Krijo një ambient të rehatshëm dhe relaksues ☕."
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> "You're doing amazing. Give yourself permission to rest deeply today, sip warm tea and cuddle up 🌸."
                CyclePhase.FOLLICULAR -> "Your energy is blossoming beautifully! Enjoy the fresh creative sparks and vitality ✨."
                CyclePhase.OVULATORY -> "You're glowing from within! A wonderful time for social connection and confidence 💕."
                CyclePhase.LUTEAL -> "Listen extra gently to your body right now. Create a cozy, nurturing sanctuary for yourself ☕."
            }
        }
        AppPersona.SARCASTIC -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Oh look, cramps again. Shocking. Grab a heating pad, eat chocolate, and cancel all plans."
                CyclePhase.FOLLICULAR -> "Hormone are cooperating for once. Try not to break anything with your sudden bursts of productivity."
                CyclePhase.OVULATORY -> "Confidence peak reached. Everyone else should probably step out of your way today."
                CyclePhase.LUTEAL -> "Ah yes, the emotional rollercoaster phase where everything is mildly annoying. Good luck."
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Oh, përsëri ngërçe. Çfarë surprize. Merr një mbështetëse të ngrohtë dhe anulo çdo plan."
                CyclePhase.FOLLICULAR -> "Hormonet po bashkëpunojnë për një herë. Kujdes mos prish ndonjë gjë me kaq shumë energji."
                CyclePhase.OVULATORY -> "Kulmi i vetëbesimit u arrit. Të gjithë të bëjnë rrugë sot."
                CyclePhase.LUTEAL -> "Faza e dramës emocionale ku çdo gjë të duket nervozuese. Fat të mbarë."
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> "Oh look, cramps again. Shocking. Grab a heating pad, eat your body weight in chocolate, and cancel everything."
                CyclePhase.FOLLICULAR -> "Hormones are cooperating for once. Try not to conquer the world with your sudden bursts of productivity."
                CyclePhase.OVULATORY -> "Peak confidence unlocked. Everyone else should probably step out of your way today."
                CyclePhase.LUTEAL -> "Ah yes, the wonderful phase where everything is mildly annoying and snacks are mandatory. Good luck."
            }
        }
        AppPersona.LOGICAL -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Biometrischer Status: Menstruationsphase. Endometriale Gewebeabstoßung im Gange. Empfohlen: Hydratation, Eisenaufnahme, Ruhe."
                CyclePhase.FOLLICULAR -> "Biometrischer Status: Follikelphase. Östrogenspiegel im Anstieg. Steigerung der körperlichen Belastbarkeit um 15%."
                CyclePhase.OVULATORY -> "Biometrischer Status: Ovulationsphase. LH-Peak verifiziert. Maximale kardiovaskuläre Leistungsfähigkeit."
                CyclePhase.LUTEAL -> "Biometrischer Status: Lutealphase. Progesterondominanz aktiv. Kalorienbedarf erhöht um ~200 kcal. Fokus auf Schlafqualität."
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Gjendja biometrike: Faza menstruale. Proçesi i pastrimit endometrial aktiv. Rekomandohet hidrateim dhe hekur."
                CyclePhase.FOLLICULAR -> "Gjendja biometrike: Faza folikulare. Rritje e estrogjenit dhe kapacitetit fizik."
                CyclePhase.OVULATORY -> "Gjendja biometrike: Faza ovulatore. Kulmi i performancës kardiovaskulare."
                CyclePhase.LUTEAL -> "Gjendja biometrike: Faza luteale. Dominim i progesteronit. Nevoja kalorike +200 kcal."
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> "Biometric status: Menstrual phase. Endometrial shedding in progress. Recommended: Hydration, iron intake, rest."
                CyclePhase.FOLLICULAR -> "Biometric status: Follicular phase. Estrogen rising. Physical endurance capacity increased by ~15%."
                CyclePhase.OVULATORY -> "Biometric status: Ovulatory phase. LH surge verified. Peak cardiovascular performance metrics."
                CyclePhase.LUTEAL -> "Biometric status: Progesterone dominance active. Caloric baseline elevated by ~200 kcal. Prioritize sleep."
            }
        }
        AppPersona.FUNNY -> when (language) {
            AppLanguage.GERMAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Deine Gebärmutter veranstaltet heute ein Heavy-Metal-Konzert. Ohren zu und Schokolade rein!"
                CyclePhase.FOLLICULAR -> "Du bist gerade unaufhaltskauf — äh, unaufhaltsam! Zeit, die Weltherrschaft zu planen 🚀."
                CyclePhase.OVULATORY -> "Du könntest heute Bäume ausreißen oder zumindest jeden in einem Debattierclub zerstören."
                CyclePhase.LUTEAL -> "Plot Twist: Deine Gefühle sind heute die Hauptattraktion im Kino. Popcorn bereitstellen 🍿."
            }
            AppLanguage.ALBANIAN -> when (phase) {
                CyclePhase.MENSTRUAL -> "Mitra juaj po luan muzikë rock sot. Vëzhgoni veten me shumë çokollatë!"
                CyclePhase.FOLLICULAR -> "Je bërë si bateri e re sot! Koha për të pushtuar botën 🚀."
                CyclePhase.OVULATORY -> "Energji maksimale! Mund të mposhtësh këdo në çdo debat sot."
                CyclePhase.LUTEAL -> "Ngjarja e ditës: Emocionet e tua janë kryefjala. Përgatit popcorn 🍿."
            }
            AppLanguage.ENGLISH -> when (phase) {
                CyclePhase.MENSTRUAL -> "Your uterus is hosting an uninvited metal concert today. Earplugs in, chocolate weaponized!"
                CyclePhase.FOLLICULAR -> "You are basically running on high-grade rocket fuel right now. Time to conquer things 🚀."
                CyclePhase.OVULATORY -> "You could arm-wrestle a grizzly bear today and win. Use your superpowers wisely."
                CyclePhase.LUTEAL -> "Plot twist: Your emotions are the main headliner at the cinema today. Grab massive amounts of popcorn 🍿."
            }
        }
    }
}

fun getPersonaPromptBadge(persona: AppPersona, language: AppLanguage): String {
    return when (persona) {
        AppPersona.LOVING -> when (language) {
            AppLanguage.GERMAN -> "🌸 Liebevoller Impuls"
            AppLanguage.ENGLISH -> "🌸 Gentle Prompt"
            AppLanguage.ALBANIAN -> "🌸 Impuls i Butë"
        }
        AppPersona.SARCASTIC -> when (language) {
            AppLanguage.GERMAN -> "🥂 Sarkastischer Gedanke"
            AppLanguage.ENGLISH -> "🥂 Witty Reality Check"
            AppLanguage.ALBANIAN -> "🥂 Mendim Sarkastik"
        }
        AppPersona.LOGICAL -> when (language) {
            AppLanguage.GERMAN -> "📊 Analytischer Reflexionspunkt"
            AppLanguage.ENGLISH -> "📊 Analytical Focus Point"
            AppLanguage.ALBANIAN -> "📊 Pikë Analitike"
        }
        AppPersona.FUNNY -> when (language) {
            AppLanguage.GERMAN -> "🤪 Heitere Tagesfrage"
            AppLanguage.ENGLISH -> "🤪 Fun Daily Question"
            AppLanguage.ALBANIAN -> "🤪 Pyetje Gazmore"
        }
    }
}

fun getPersonaPlaceholder(persona: AppPersona, language: AppLanguage): String {
    return when (persona) {
        AppPersona.LOVING -> when (language) {
            AppLanguage.GERMAN -> "Schreibe hier ganz sanft auf, was dein Herz und dein Körper dir heute sagen..."
            AppLanguage.ENGLISH -> "Gently write down what your heart and body are feeling today..."
            AppLanguage.ALBANIAN -> "Shkruaj me butësi çfarë po ndjen zemra dhe trupi yt sot..."
        }
        AppPersona.SARCASTIC -> when (language) {
            AppLanguage.GERMAN -> "Lass deinen ungefilterten Gedanken freien Lauf – niemand verurteilt dich hier..."
            AppLanguage.ENGLISH -> "Unfiltered thoughts go here – no judgments, zero censorship..."
            AppLanguage.ALBANIAN -> "Shkruaj mendimet e pafiltruara – askush nuk të gjykon këtu..."
        }
        AppPersona.LOGICAL -> when (language) {
            AppLanguage.GERMAN -> "Dokumentiere relevante Beobachtungen, biologische Muster oder Maßnahmen..."
            AppLanguage.ENGLISH -> "Document key observations, biological patterns, and action items..."
            AppLanguage.ALBANIAN -> "Dokumento vëzhgimet kyçe, modelet dhe veprimet e ditës..."
        }
        AppPersona.FUNNY -> when (language) {
            AppLanguage.GERMAN -> "Hau in die Tasten: Anekdoten, Launen oder der tägliche Wahnsinn..."
            AppLanguage.ENGLISH -> "Type away: daily comedy, wild moods, or whatever is happening..."
            AppLanguage.ALBANIAN -> "Shkruaj këtu: humore, teka të çuditshme apo aventurat e ditës..."
        }
    }
}

fun getPersonaSaveMessage(persona: AppPersona, language: AppLanguage): String {
    return when (persona) {
        AppPersona.LOVING -> when (language) {
            AppLanguage.GERMAN -> "Wunderschön! Dein Tagebucheintrag wurde mit ganz viel Liebe gespeichert 🌸."
            AppLanguage.ENGLISH -> "Wonderful! Your journal entry was saved with love 🌸."
            AppLanguage.ALBANIAN -> "E mrekullueshme! Shënimi yt u ruajt me dashuri 🌸."
        }
        AppPersona.SARCASTIC -> when (language) {
            AppLanguage.GERMAN -> "Eintrag gespeichert. Versuche heute, nichts kaputt zu machen 🥂."
            AppLanguage.ENGLISH -> "Entry saved. Try not to break anything today 🥂."
            AppLanguage.ALBANIAN -> "Shënimi u ruajt. Fat në këtë ditë 🥂."
        }
        AppPersona.LOGICAL -> when (language) {
            AppLanguage.GERMAN -> "Biometrischer Datensatz erfolgreich persistiert. Synchronisation abgeschlossen 📊."
            AppLanguage.ENGLISH -> "Biometric log successfully persisted. Synchronization complete 📊."
            AppLanguage.ALBANIAN -> "Të dhënat biometrike u ruajtën me sukses 📊."
        }
        AppPersona.FUNNY -> when (language) {
            AppLanguage.GERMAN -> "Boom! Tagebuch-Eintrag im System verankert. Die Weltherrschaft rückt näher 🚀."
            AppLanguage.ENGLISH -> "Boom! Journal entry anchored in orbit. World domination is one step closer 🚀."
            AppLanguage.ALBANIAN -> "Boom! Shënimi u ruajt. Pushtimi i botës po afron 🚀."
        }
    }
}

