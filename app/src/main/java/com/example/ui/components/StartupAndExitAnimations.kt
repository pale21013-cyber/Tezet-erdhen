package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.FollicularPurple
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RosePrimary
import kotlinx.coroutines.delay

/**
 * Startup Splash Entrance Animation: A high-class blooming aura logo scaling up with
 * a pulsating aura glow halo ring and elegant title fade-in.
 */
@Composable
fun AuraStartupSplashScreen(
    onSplashFinished: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN
) {
    val logoScale = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "startup_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_scale"
    )

    LaunchedEffect(Unit) {
        // Spring bloom animation for logo
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
        delay(1200) // Keep splash visible for 1.2 seconds
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF180B10),
                        Color(0xFF0F080A)
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
            // Glowing Aura Pulse Halo Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Outer Pulse Halo
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                            alpha = contentAlpha.value * 0.4f
                        }
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(RosePrimary.copy(alpha = 0.6f), Color.Transparent)
                            )
                        )
                )

                // Central Logo Circle
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF261219),
                    border = BorderStroke(2.dp, RosePrimary.copy(alpha = 0.8f)),
                    shadowElevation = 12.dp,
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer {
                            scaleX = logoScale.value
                            scaleY = logoScale.value
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Aura Logo",
                            tint = RosePrimary,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Title & Tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer { alpha = contentAlpha.value }
            ) {
                Text(
                    text = "AURA CYCLE",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    letterSpacing = 2.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = FollicularPurple.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, FollicularPurple.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = when (language) {
                            AppLanguage.GERMAN -> "✨ Offline-First • Predictive Cycle Intelligence"
                            AppLanguage.ENGLISH -> "✨ Offline-First • Predictive Cycle Intelligence"
                            AppLanguage.ALBANIAN -> "✨ Privatësi i Plotë • Inteligjenca e Ciklit"
                        },
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF472B6),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

/**
 * Closing Winking Animation Overlay: A playful, charming winking logo & sparkling send-off
 * when the app is closed or exiting.
 */
@Composable
fun AuraWinkingExitOverlay(
    onAnimationComplete: () -> Unit,
    language: AppLanguage = AppLanguage.GERMAN
) {
    var isWinking by remember { mutableStateOf(false) }
    val overlayScale = remember { Animatable(0.7f) }
    val overlayAlpha = remember { Animatable(0f) }

    val winkText = when (language) {
        AppLanguage.GERMAN -> "Bis bald! ✨ 😉"
        AppLanguage.ENGLISH -> "See you soon! ✨ 😉"
        AppLanguage.ALBANIAN -> "Shihemi së shpejti! ✨ 😉"
    }

    LaunchedEffect(Unit) {
        // Fade & Scale in exit overlay
        overlayAlpha.animateTo(1f, tween(200))
        overlayScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        delay(250)
        // Trigger the Wink! 😉
        isWinking = true
        delay(700)
        // Complete exit
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .graphicsLayer { alpha = overlayAlpha.value }
            .testTag("winking_exit_overlay"),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF1F1218),
            border = BorderStroke(2.dp, RosePrimary),
            shadowElevation = 16.dp,
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
                // Animated Winking Mascot / Face
                Surface(
                    shape = CircleShape,
                    color = RosePrimary.copy(alpha = 0.2f),
                    border = BorderStroke(1.5.dp, RosePrimary),
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        androidx.compose.animation.AnimatedContent(
                            targetState = isWinking,
                            transitionSpec = {
                                (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut())
                            },
                            label = "winking_mascot"
                        ) { winking ->
                            if (!winking) {
                                Text(text = "🌸 (◕‿◕) ✨", fontSize = 28.sp)
                            } else {
                                Text(text = "🌸 (◕‿─) 😉", fontSize = 32.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "AURA CYCLE",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = winkText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RosePrimary
                )
            }
        }
    }
}
