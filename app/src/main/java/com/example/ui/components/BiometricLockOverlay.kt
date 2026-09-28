package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import com.example.localization.getLocalizedCalmingAffirmations
import com.example.localization.getLocalizedCalmingBadge
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RosePrimary
import kotlinx.coroutines.delay

@Composable
fun BiometricLockOverlay(
    errorMessage: String?,
    onBiometricUnlock: () -> Unit,
    onPinSubmit: (String) -> Unit,
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val strings = getAppStrings(language)
    val affirmations = remember(language) { getLocalizedCalmingAffirmations(language) }
    val calmingBadge = remember(language) { getLocalizedCalmingBadge(language) }

    var currentAffirmationIdx by remember { mutableIntStateOf(0) }
    var enteredPin by remember { mutableStateOf("") }
    val pulseScale = remember { Animatable(1f) }

    // Pulsing lock animation
    LaunchedEffect(Unit) {
        pulseScale.animateTo(
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Auto-cycle through warm calming affirmations gently every 13 seconds
    LaunchedEffect(language) {
        while (true) {
            delay(13_000L)
            currentAffirmationIdx = (currentAffirmationIdx + 1) % affirmations.size
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("biometric_lock_overlay"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Warmth & Calming Banner for Women above the lock icon (Large, prominent typography)
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.2.dp, RosePrimary.copy(alpha = 0.35f)),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        currentAffirmationIdx = (currentAffirmationIdx + 1) % affirmations.size
                    }
                    .testTag("calming_affirmation_banner")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = RosePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = calmingBadge,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RosePrimary,
                            letterSpacing = 0.15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    AnimatedContent(
                        targetState = affirmations[currentAffirmationIdx % affirmations.size],
                        transitionSpec = { fadeIn(tween(800)) togetherWith fadeOut(tween(500)) },
                        label = "affirmation_text"
                    ) { text ->
                        Text(
                            text = text,
                            fontSize = 15.5.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Indicator dots for the 13-second carousel
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in affirmations.indices) {
                            val isCurrent = i == (currentAffirmationIdx % affirmations.size)
                            Box(
                                modifier = Modifier
                                    .size(if (isCurrent) 7.dp else 5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCurrent) RosePrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App Branding & Lock Icon with glowing container
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(76.dp)
                    .scale(pulseScale.value)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = strings.lockScreenTitle,
                fontFamily = OutfitDisplayFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.3).sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = strings.lockScreenSubtitle,
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Biometric Unlock Instant Button with luxury styling
            Button(
                onClick = onBiometricUnlock,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("biometric_unlock_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Fingerprint",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strings.unlockBiometricBtn,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PIN Fallback header
            Text(
                text = strings.pinPrompt,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // PIN Indicator Dots with crisp border and fill
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                for (i in 0..3) {
                    val isFilled = i < enteredPin.length
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFilled) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (isFilled) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            )
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Numeric Keypad: High-contrast, tactile circular keys with distinct colors from background surface
            val keys = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("C", "0", "DEL")
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (row in keys) {
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        for (key in row) {
                            val isAction = key == "C" || key == "DEL"
                            Surface(
                                shape = CircleShape,
                                color = if (isAction)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else
                                    MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.2.dp,
                                    if (isAction) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                                ),
                                shadowElevation = if (isAction) 2.dp else 4.dp,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        when (key) {
                                            "C" -> enteredPin = ""
                                            "DEL" -> if (enteredPin.isNotEmpty()) enteredPin = enteredPin.dropLast(1)
                                            else -> {
                                                if (enteredPin.length < 4) {
                                                    enteredPin += key
                                                    if (enteredPin.length == 4) {
                                                        onPinSubmit(enteredPin)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    .testTag("pin_key_$key")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (key == "DEL") {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                                            contentDescription = "Backspace",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else if (key == "C") {
                                        Text(
                                            text = key,
                                            fontFamily = OutfitDisplayFamily,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Text(
                                            text = key,
                                            fontFamily = OutfitDisplayFamily,
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
