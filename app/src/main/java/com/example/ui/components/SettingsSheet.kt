package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.ThemeSetting
import com.example.updater.UpdateStatus

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.AuraButtonFontSize
import com.example.ui.components.AuraMarqueeText
import com.example.ui.components.AuraOutlinedButton
import com.example.ui.components.AuraPrimaryButton

@Composable
fun SettingsSheet(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    currentThemeSetting: ThemeSetting,
    onThemeSettingChange: (ThemeSetting) -> Unit,
    onFastMoodLog: (String) -> Unit = {},
    onResetDemoData: () -> Unit,
    onReplayOnboarding: () -> Unit = {},
    onExportBackup: () -> Unit = {},
    onImportBackup: (String) -> Unit = {},
    targetRepository: String = "pale21013-cyber/Tezet-erdhen",
    onTargetRepositoryChange: (String) -> Unit = {},
    updateStatus: UpdateStatus = UpdateStatus.Idle,
    onCheckForUpdates: () -> Unit = {},
    onDownloadAndInstallUpdate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(currentLanguage)
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val content = stream.bufferedReader().use { it.readText() }
                    onImportBackup(content)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Language Selector Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("language_selector_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = strings.languageSectionTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Deutsch • Shqip • English",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (lang in AppLanguage.entries) {
                        val isSelected = lang == currentLanguage
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onLanguageChange(lang) }
                                .testTag("language_option_${lang.code}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = lang.flag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = lang.displayName,
                                        fontSize = 12.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Theme & Appearance Card (High Contrast Light / Dark / System)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("theme_selector_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Theme",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = strings.themeSectionTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${strings.themeLight} • ${strings.themeDark} • ${strings.themeSystem}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val themeOptions = listOf(
                        Triple(ThemeSetting.SYSTEM, strings.themeSystem, Icons.Default.BrightnessAuto),
                        Triple(ThemeSetting.LIGHT, strings.themeLight, Icons.Default.LightMode),
                        Triple(ThemeSetting.DARK, strings.themeDark, Icons.Default.DarkMode)
                    )

                    for ((themeOpt, label, icon) in themeOptions) {
                        val isSelected = themeOpt == currentThemeSetting
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onThemeSettingChange(themeOpt) }
                                .testTag("theme_option_${themeOpt.code}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = label,
                                        fontSize = 12.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Fast Actions (Modes of Person / App Icon & Widget Shortcuts)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("fast_actions_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Fast Actions",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.fastActionsTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.fastActionsSubtitle,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fast action mode chips (tap to instantly test & log)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val fastMoods = listOf(
                        Pair("Happy", "🌸 ${strings.fastMoodHappy}"),
                        Pair("Calm", "🧘 ${strings.fastMoodCalm}"),
                        Pair("Sensitive", "🥺 ${strings.fastMoodSensitive}"),
                        Pair("Energetic", "⚡ ${strings.fastMoodEnergetic}")
                    )

                    for ((moodKey, moodLabel) in fastMoods) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onFastMoodLog(moodKey) }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 11.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = moodLabel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3.5. Notification & Background Worker Settings Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("notifications_settings_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLanguage == AppLanguage.GERMAN) "Zyklusphasen-Benachrichtigungen" else if (currentLanguage == AppLanguage.ALBANIAN) "Njoftimet e Fazave të Ciklit" else "Cycle Phase Notifications",
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.GERMAN) "Automatischer WorkManager-Hintergrunddienst" else if (currentLanguage == AppLanguage.ALBANIAN) "Shërbimi në prapavijë i WorkManager" else "Automatic WorkManager background worker",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    text = if (currentLanguage == AppLanguage.GERMAN) "Der Hintergrunddienst prüft automatisch deinen Phasenübergang und erinnert dich an deine personalisierten Ernährungs- und Bewegungsempfehlungen." else if (currentLanguage == AppLanguage.ALBANIAN) "Kjo prapavijë kontrollon automatikisht ndryshimin e fazës dhe ju kujton këshillat e ushqimit dhe lëvizjes." else "The background worker automatically checks your phase transition and reminds you of your tailored nutrition & movement recommendations.",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val notifPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        com.example.worker.CyclePhaseWorkerScheduler.triggerImmediateCheck(context)
                    }
                }

                Button(
                    onClick = {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            com.example.worker.CyclePhaseWorkerScheduler.triggerImmediateCheck(context)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth().testTag("test_notification_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.GERMAN) "Test-Benachrichtigung jetzt auslösen" else if (currentLanguage == AppLanguage.ALBANIAN) "Dërgo njoftim provë tani" else "Trigger Test Notification Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                }
            }
        }

        // 4. In-App Updates & CI/CD Pipeline Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("updater_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = "Updater",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.updaterTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "🌸 v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = strings.updaterSubtitle,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = targetRepository,
                    onValueChange = onTargetRepositoryChange,
                    label = { Text("GitHub Repository", fontSize = 11.sp) },
                    placeholder = { Text("owner/repo") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("github_repo_input")
                )

                when (val status = updateStatus) {
                    is UpdateStatus.Idle -> {
                        Button(
                            onClick = onCheckForUpdates,
                            shape = RoundedCornerShape(14.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth().testTag("check_updates_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.checkUpdatesBtn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                    is UpdateStatus.Checking -> {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.5.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = strings.checkUpdatesBtn + "...",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    is UpdateStatus.UpdateAvailable -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "v${status.info.latestVersionName} (Build ${status.info.latestVersionCode})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                if (status.info.releaseNotes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    MarkdownText(
                                        markdown = status.info.releaseNotes,
                                        baseFontSize = 12.sp,
                                        baseColor = MaterialTheme.colorScheme.onSurface,
                                        accentColor = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = onDownloadAndInstallUpdate,
                            shape = RoundedCornerShape(14.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth().testTag("download_install_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.downloadInstallBtn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                    is UpdateStatus.Downloading -> {
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = String.format(strings.downloadingProgress, status.progressPercent),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${status.progressPercent}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { status.progressPercent / 100f },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                    is UpdateStatus.ReadyToInstall -> {
                        Text(
                            text = strings.systemInstallerPrompt,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is UpdateStatus.Error -> {
                        Text(
                            text = status.message,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        OutlinedButton(
                            onClick = onCheckForUpdates,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(strings.checkUpdatesBtn)
                        }
                    }
                    is UpdateStatus.UpToDate -> {
                        Text(
                            text = "✓ App ist auf dem neuesten Stand!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // CI/CD Description box
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "⚙️ ${strings.cicdInfoTitle}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = strings.cicdInfoDesc,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 5. Privacy & Offline Architecture Card
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
                        color = Color(0xFFDCFCE7),
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Security",
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = strings.privacyTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.privacySubtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = strings.privacyBulletPoints,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 6. Data Backup & Transfer (JSON Export/Import with LSTM state)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("backup_transfer_card")
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = "Backup",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.backupSectionTitle,
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.backupSectionSubtitle,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Export Button
                AuraPrimaryButton(
                    text = strings.backupExportBtn,
                    icon = Icons.Default.FileDownload,
                    onClick = onExportBackup,
                    modifier = Modifier.testTag("export_backup_button")
                )

                // Import Button
                AuraOutlinedButton(
                    text = strings.backupImportBtn,
                    icon = Icons.Default.FileUpload,
                    onClick = {
                        try {
                            filePickerLauncher.launch("*/*")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.testTag("import_backup_button")
                )
            }
        }

        // 7. Database & Data Management
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = strings.dataMgmtTitle,
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedButton(
                    onClick = onResetDemoData,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, FollicularPurple.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("reset_demo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = FollicularPurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.resetDemoBtn,
                        color = FollicularPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onReplayOnboarding,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = RosePrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("replay_onboarding_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Onboarding",
                        tint = RosePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🌸 ${strings.onboardingWelcomeTitle}",
                        color = RosePrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
