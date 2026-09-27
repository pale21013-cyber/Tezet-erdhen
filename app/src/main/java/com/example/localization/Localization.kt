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
    val biometricGateTitle: String,
    val biometricToggleTitle: String,
    val biometricToggleSubtitle: String,
    val lockNowBtn: String,
    val dataMgmtTitle: String,
    val resetDemoBtn: String,

    // Lock screen
    val lockScreenTitle: String,
    val lockScreenSubtitle: String,
    val unlockBiometricBtn: String,
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
    val cicdInfoDesc: String
)

val GermanStrings = StringsBundle(
    appName = "Aura Cycle",
    tabToday = "Heute",
    tabCalendar = "Kalender",
    tabPixels = "Jahr in Pixeln",
    tabInsights = "ML-Erkenntnisse",
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
    biometricGateTitle = "Biometrische Schutzsperre",
    biometricToggleTitle = "App mit Biometrie sperren",
    biometricToggleSubtitle = "Fingerabdruck, Gesichtsscan oder PIN beim Öffnen anfordern",
    lockNowBtn = "App jetzt sperren (Biometrischen Schutz testen)",
    dataMgmtTitle = "Datenverwaltung",
    resetDemoBtn = "Demo-Zyklen zurücksetzen & neu laden",

    lockScreenTitle = "Aura Cycle Geschützt",
    lockScreenSubtitle = "100% Offline & Biometrisch Verschlüsselt",
    unlockBiometricBtn = "Mit Fingerabdruck / Gesicht entsperren",
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
    cicdInfoDesc = "Automatische Versionserhöhung (VersionCode & VersionName) bei jedem Build, APK-Generierung & GitHub Release Veröffentlichung."
)

val AlbanianStrings = StringsBundle(
    appName = "Aura Cycle",
    tabToday = "Sot",
    tabCalendar = "Kalendari",
    tabPixels = "Viti në Piksela",
    tabInsights = "Statistikat ML",
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
    biometricGateTitle = "Porta Mbrojtëse Biometrike",
    biometricToggleTitle = "Blloko aplikacionin me biometri",
    biometricToggleSubtitle = "Kërko shenjën e gishtit, skanimin e fytyrës ose PIN gjatë hapjes",
    lockNowBtn = "Blloko aplikacionin tani (Testo mbrojtjen biometrike)",
    dataMgmtTitle = "Menaxhimi i të Dhënave",
    resetDemoBtn = "Rivendos & ringarko ciklet provë",

    lockScreenTitle = "Aura Cycle E Mbrojtur",
    lockScreenSubtitle = "100% Jashtë Linje & E Kriptuar Biometrikisht",
    unlockBiometricBtn = "Zhblloko me Shenjë Gishti / Fytyrë",
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
    cicdInfoDesc = "Inkrementim automatik i numrit të versionit në çdo iteracion, krijim i paketës APK & publikim automatik."
)

val EnglishStrings = StringsBundle(
    appName = "Aura Cycle",
    tabToday = "Today",
    tabCalendar = "Calendar",
    tabPixels = "Year in Pixels",
    tabInsights = "ML Insights",
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
    biometricGateTitle = "Biometric Protection Gate",
    biometricToggleTitle = "Lock App with Biometrics",
    biometricToggleSubtitle = "Require fingerprint, face scan, or PIN upon launch",
    lockNowBtn = "Lock App Immediately (Test Biometric Gate)",
    dataMgmtTitle = "Data Management",
    resetDemoBtn = "Reset & Re-seed Historical Demo Cycles",

    lockScreenTitle = "Aura Cycle Protected",
    lockScreenSubtitle = "100% Offline & Biometrically Encrypted",
    unlockBiometricBtn = "Unlock with Fingerprint / Face",
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
    cicdInfoDesc = "Automated version code & name incrementing on every workflow run, APK packaging, and GitHub Releases."
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
