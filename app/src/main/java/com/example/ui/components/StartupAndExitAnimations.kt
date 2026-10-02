package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RosePrimary
import kotlinx.coroutines.delay

/**
 * White-Liquid-ish Startup Animation (No static logo icon):
 * Concentric white-liquid fluid ripple waves blooming outwards on a silky pearl white fluid canvas.
 */
@Composable
fun AuraStartupSplashScreen(
    onSplashFinished: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN
) {
    val liquidBloom = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "white_liquid_ripples")
    
    val rippleScale1 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple_scale_1"
    )

    val rippleAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple_alpha_1"
    )

    val rippleScale2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, delayMillis = 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple_scale_2"
    )

    val rippleAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, delayMillis = 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple_alpha_2"
    )

    LaunchedEffect(Unit) {
        liquidBloom.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(600)
        )
        delay(1250)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF), // Pure Liquid White
                        Color(0xFFF1F5F9), // Soft Pearl Ice White
                        Color(0xFFE2E8F0)  // Liquid Silk Slate
                    )
                )
            )
            .testTag("startup_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // White Liquid Fluid Ripple Drops (NO static logo icon!)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(180.dp)
            ) {
                // Outer Liquid Wave 1
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .graphicsLayer {
                            scaleX = rippleScale1 * liquidBloom.value
                            scaleY = rippleScale1 * liquidBloom.value
                            alpha = rippleAlpha1
                        }
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.95f),
                                    Color(0xFFFBCFE8).copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Outer Liquid Wave 2
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer {
                            scaleX = rippleScale2 * liquidBloom.value
                            scaleY = rippleScale2 * liquidBloom.value
                            alpha = rippleAlpha2
                        }
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFFF472B6).copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Central Glossy Pearl Liquid Drop Core
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(2.5.dp, Brush.horizontalGradient(listOf(RosePrimary, Color(0xFFC084FC)))),
                    shadowElevation = 16.dp,
                    modifier = Modifier
                        .size(90.dp)
                        .graphicsLayer {
                            scaleX = liquidBloom.value
                            scaleY = liquidBloom.value
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(
                            Brush.radialGradient(
                                colors = listOf(Color.White, Color(0xFFFFF1F2))
                            )
                        )
                    ) {
                        Text(
                            text = "💧",
                            fontSize = 38.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App Title & Subtitle in sleek White Liquid aesthetics
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer { alpha = textAlpha.value }
            ) {
                // Main TEZ Logo Mark
                TezetErdhenLogoMark(
                    tint = Color(0xFF0F172A),
                    width = 88.dp,
                    height = 40.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Subtitle: Tezet erdhen
                Text(
                    text = "Tezet erdhen",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, RosePrimary.copy(alpha = 0.35f)),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = when (language) {
                            AppLanguage.GERMAN -> "✨ Zyklus-Intelligenz • White Liquid Design"
                            AppLanguage.ENGLISH -> "✨ Cycle Intelligence • White Liquid Design"
                            AppLanguage.ALBANIAN -> "✨ Inteligjenca e Ciklit • Dizajni i Bardhë"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RosePrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

/**
 * Exit Confirmation Modal Dialog ("Möchtest du die App beenden?"):
 * Asks the user if she is sure she wants to exit before closing the app.
 */
@Composable
fun AuraExitConfirmationDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN
) {
    val title = when (language) {
        AppLanguage.GERMAN -> "App beenden?"
        AppLanguage.ENGLISH -> "Exit App?"
        AppLanguage.ALBANIAN -> "Mbyll aplikacionin?"
    }

    val message = when (language) {
        AppLanguage.GERMAN -> "Bist du sicher, dass du Tezet erdhen jetzt beenden möchtest?"
        AppLanguage.ENGLISH -> "Are you sure you want to exit Tezet erdhen now?"
        AppLanguage.ALBANIAN -> "A je e sigurt që dëshiron të mbyllësh Tezet erdhen?"
    }

    val confirmBtnText = when (language) {
        AppLanguage.GERMAN -> "Ja, beenden"
        AppLanguage.ENGLISH -> "Yes, exit"
        AppLanguage.ALBANIAN -> "Po, mbyll"
    }

    val cancelBtnText = when (language) {
        AppLanguage.GERMAN -> "Abbrechen"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.ALBANIAN -> "Anulo"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        icon = {
            Surface(
                shape = CircleShape,
                color = Color(0xFFFFF1F2),
                border = BorderStroke(1.dp, RosePrimary.copy(alpha = 0.4f)),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    TezetErdhenLogoMark(
                        tint = RosePrimary,
                        width = 32.dp,
                        height = 15.dp
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                fontFamily = OutfitDisplayFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = message,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                color = Color(0xFF475569),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmExit,
                colors = ButtonDefaults.textButtonColors(contentColor = RosePrimary)
            ) {
                Text(
                    text = confirmBtnText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.5.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF64748B))
            ) {
                Text(
                    text = cancelBtnText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        },
        modifier = Modifier.testTag("exit_confirmation_dialog")
    )
}

/**
 * Closing Send-off Overlay:
 * Plays an elegant farewell animation with TEZ branding (no yellow emojis) when exit is confirmed.
 */
@Composable
fun AuraWinkingExitOverlay(
    onAnimationComplete: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN
) {
    val overlayScale = remember { Animatable(0.7f) }
    val overlayAlpha = remember { Animatable(0f) }

    val exitFarewellText = when (language) {
        AppLanguage.GERMAN -> "Bis bald! ✨"
        AppLanguage.ENGLISH -> "See you soon! ✨"
        AppLanguage.ALBANIAN -> "Shihemi së shpejti! ✨"
    }

    LaunchedEffect(Unit) {
        overlayAlpha.animateTo(1f, tween(200))
        overlayScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        delay(850)
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .graphicsLayer { alpha = overlayAlpha.value }
            .testTag("winking_exit_overlay"),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(2.dp, RosePrimary),
            shadowElevation = 20.dp,
            modifier = Modifier
                .padding(24.dp)
                .graphicsLayer {
                    scaleX = overlayScale.value
                    scaleY = overlayScale.value
                }
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circular Brand Emblem
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFF1F2),
                    border = BorderStroke(1.5.dp, RosePrimary),
                    modifier = Modifier.size(92.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        TezetErdhenLogoMark(
                            tint = Color(0xFF0F172A),
                            width = 54.dp,
                            height = 25.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Tezet erdhen",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = exitFarewellText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RosePrimary
                )
            }
        }
    }
}
