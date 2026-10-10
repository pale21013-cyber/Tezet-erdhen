package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.util.rememberHapticFeedbackManager
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * Interactive Core Animation Techniques Lab Showcase Card
 * Showcases all 9 Animation Techniques: Keyframes, Spring Animations, Gestures,
 * Physics, SVG Paths, Layout Transitions, Shaders, Particles, and Rive/Lottie style procedural animations.
 */
@Composable
fun CoreAnimationTechniquesCard(
    language: AppLanguage = AppLanguage.GERMAN,
    modifier: Modifier = Modifier
) {
    val hapticManager = rememberHapticFeedbackManager()
    var expanded by remember { mutableStateOf(false) }
    var selectedTechniqueIndex by remember { mutableIntStateOf(0) }

    val techniques = listOf(
        "1. Keyframes" to "Choreographed timeline values at set points in time.",
        "2. Spring Animations" to "Physics-based motion for snappy, bouncy transitions.",
        "3. Gestures" to "Interactive user input drag & swipe detection.",
        "4. Physics" to "Real-world gravity, velocity, and bounce simulation.",
        "5. SVG Paths" to "Custom vector path drawing and shape morphing.",
        "6. Layout Transitions" to "Smooth sizing and reordering between states.",
        "7. Shaders" to "Direct pixel manipulation and render effects.",
        "8. Particles" to "Confetti, sparkles, and explosive physics.",
        "9. Rive/Lottie" to "High-fidelity procedural vector character/lotus state machine."
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("core_animation_lab_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        hapticManager.performClick()
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Animation,
                                contentDescription = "Animation Lab",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = when (language) {
                                AppLanguage.GERMAN -> "Core Animation Studio & Techniken"
                                AppLanguage.ENGLISH -> "Core Animation Studio & Lab"
                                AppLanguage.ALBANIAN -> "Studio & Lab e Animacioneve"
                            },
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when (language) {
                                AppLanguage.GERMAN -> "Entdecke alle 9 Animationstechniken live"
                                AppLanguage.ENGLISH -> "Explore all 9 core animation techniques live"
                                AppLanguage.ALBANIAN -> "Eksploro 9 teknikat e animacionit live"
                            },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(4.dp))

                // Technique Selector Tabs
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    techniques.forEachIndexed { index, (title, desc) ->
                        val isSelected = selectedTechniqueIndex == index
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    hapticManager.performClick()
                                    selectedTechniqueIndex = index
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = desc,
                                        fontSize = 11.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Demo Box for Selected Technique
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (selectedTechniqueIndex) {
                            0 -> KeyframesDemo()
                            1 -> SpringAnimationDemo()
                            2 -> GestureDemo()
                            3 -> PhysicsDemo()
                            4 -> SvgPathDemo()
                            5 -> LayoutTransitionDemo()
                            6 -> ShaderDemo()
                            7 -> ParticleDemo()
                            8 -> RiveLottieDemo()
                        }
                    }
                }
            }
        }
    }
}

/** 1. Keyframes Demo */
@Composable
private fun KeyframesDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "keyframes_demo")
    val scaleVal by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                1f at 0 using FastOutSlowInEasing
                1.35f at 400 using FastOutSlowInEasing
                0.9f at 800 using FastOutSlowInEasing
                1f at 1200
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "keyframe_scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Keyframe Choreography", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(scaleVal)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
        }
    }
}

/** 2. Spring Animation Demo */
@Composable
private fun SpringAnimationDemo() {
    var pressed by remember { mutableStateOf(false) }
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (pressed) 1.25f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "spring_scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Snappy Spring Physics (Tap Me)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Button(
            onClick = { pressed = !pressed },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.scale(scale)
        ) {
            Text("Spring Bounce")
        }
    }
}

/** 3. Gesture Demo */
@Composable
private fun GestureDemo() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Drag Me (Gestures)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .size(140.dp, 60.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(-60f, 60f)
                        offsetY = (offsetY + dragAmount.y).coerceIn(-30f, 30f)
                    }
                }
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("Drag & Swipe", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
        }
    }
}

/** 4. Physics Demo */
@Composable
private fun PhysicsDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "physics_bounce")
    val bounceY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -35f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "physics_y"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Gravity & Bouncing Ball Physics", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset(y = bounceY.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFFEC4899), Color(0xFFBE185D)))),
            contentAlignment = Alignment.Center
        ) {
            Text("⚽", fontSize = 20.sp)
        }
    }
}

/** 5. SVG Paths Demo */
@Composable
private fun SvgPathDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "path_demo")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "path_progress"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Custom SVG Path Drawing", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Canvas(modifier = Modifier.size(120.dp, 60.dp)) {
            val path = Path().apply {
                moveTo(0f, size.height / 2f)
                cubicTo(
                    size.width * 0.25f, 0f,
                    size.width * 0.75f, size.height.toFloat(),
                    size.width, size.height / 2f
                )
            }
            drawPath(
                path = path,
                color = Color(0xFF8B5CF6),
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}

/** 6. Layout Transitions Demo */
@Composable
private fun LayoutTransitionDemo() {
    var expandedState by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable { expandedState = !expandedState }
    ) {
        Text("Layout Transition (Tap to Expand)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Animated Sizing", color = Color.White, fontWeight = FontWeight.Bold)
                if (expandedState) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Smoothly animates container height and width changes automatically across UI states.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

/** 7. Shaders Demo */
@Composable
private fun ShaderDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "shader_demo")
    val blurRadius by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blur_anim"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("RenderEffect Shaders & Blur", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .size(100.dp, 50.dp)
                .blur(blurRadius.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF06B6D4), Color(0xFF3B82F6)))),
            contentAlignment = Alignment.Center
        ) {
            Text("Shader FX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

/** 8. Particles Demo */
@Composable
private fun ParticleDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "particle_anim")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Particle Confetti & Sparkles", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Canvas(modifier = Modifier.size(120.dp, 60.dp)) {
            val particleColors = listOf(Color(0xFFEC4899), Color(0xFFA855F7), Color(0xFF3B82F6), Color(0xFFF59E0B))
            for (i in 0..12) {
                val angle = (i * 30f) * (Math.PI / 180f)
                val distance = animProgress * 50f
                val cx = size.width / 2f + (cos(angle) * distance).toFloat()
                val cy = size.height / 2f + (sin(angle) * distance).toFloat()
                drawCircle(
                    color = particleColors[i % particleColors.size],
                    radius = (3f + (i % 3)).dp.toPx(),
                    center = Offset(cx, cy)
                )
            }
        }
    }
}

/** 9. Rive / Lottie Style Procedural Character State Machine Demo */
@Composable
private fun RiveLottieDemo() {
    val infiniteTransition = rememberInfiniteTransition(label = "rive_lottie")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_anim"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Rive/Lottie Procedural State Machine", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(pulse)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFFF43F5E), Color(0xFF881337)))),
            contentAlignment = Alignment.Center
        ) {
            Text("🌸", fontSize = 24.sp)
        }
    }
}
