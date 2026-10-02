package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HealthPitchBlack
import com.example.ui.theme.OutfitDisplayFamily

const val TEZET_SVG_PATH_DATA = "M27.6 177.2L182.3 177.2L184.3 216.3L175.9 216.3Q175.1 204.4 171.3 197.8Q167.5 191.3 160.2 188.6Q152.8 186 140.9 186L140.9 186Q130.9 186 125.8 186.6Q120.8 187.2 119.3 188.5Q117.8 189.8 117.8 192.3L117.8 192.3L117.8 301.9Q117.8 310.7 119.5 316.1Q121.2 321.5 127 324.1Q132.7 326.7 144.2 327.1L144.2 327.1L144.2 334.8L66.8 334.8L66.8 327.1Q78.4 326.7 84.1 324.1Q89.7 321.5 91.5 316.1Q93.4 310.7 93.4 301.9L93.4 301.9L93.4 192.3Q93.4 189.8 92 188.5Q90.5 187.2 86.3 186.6Q82.1 186 73.3 186L73.3 186Q65.3 186 58.6 186.7Q51.8 187.4 46.7 190.3Q41.6 193.1 38.6 199.3Q35.6 205.4 35.2 216.3L35.2 216.3L27.6 216.3L27.6 177.2ZM241 189.4L241 189.4L241 248.2Q252.1 248.2 259.6 246.9Q267.1 245.6 271.5 242.7Q275.9 239.8 277.8 235.3Q279.8 230.8 279.8 224.7L279.8 224.7L286.9 224.7L286.9 280.6L279.8 280.6Q279.8 274.4 278 269.9Q276.3 265.4 272 262.5Q267.7 259.5 260.1 257.9Q252.5 256.4 241 256.4L241 256.4L241 302.7Q241 311.5 244.2 316.6Q247.4 321.7 254.8 323.9Q262.1 326 275 326L275 326Q289.6 326 298.3 323.2Q307 320.3 311.9 313.4Q316.8 306.6 319.5 294.5L319.5 294.5L327.3 294.5L324.2 334.8L197 334.8L197 327.1Q206.6 326.7 211.5 324.9Q216.3 323.2 217.9 318.3Q219.5 313.3 219.5 303.7L219.5 303.7L219.5 208.3Q219.5 198.7 218.1 193.8Q216.7 189 212.3 187.2Q207.9 185.3 199.1 184.9L199.1 184.9L199.1 177.2L317.4 177.2L319.3 213.2L312.1 213.2Q310.5 202.3 306.1 196.4Q301.7 190.5 293.1 188.2Q284.5 186 270.5 186L270.5 186L244.5 186Q242.9 186 242 186.9Q241 187.8 241 189.4ZM475.5 286.7L484.4 286.7L481.9 334.8L339.8 334.8L339.8 334L447.5 185.8L391.4 185.8Q377 185.8 369.8 192.3Q362.5 198.9 359.6 214.8L359.6 214.8L352.1 214.8L355.1 177.2L479.8 177.2L479.8 178L373.1 325.4L436.6 325.4Q447.1 325.4 453.8 323.8Q460.6 322.2 464.7 318.1Q468.8 314 471.2 306.3Q473.7 298.6 475.5 286.7L475.5 286.7Z"

/**
 * Custom vector logo mark of "TEZ" parsed from the provided SVG path
 */
@Composable
fun TezetErdhenLogoMark(
    modifier: Modifier = Modifier,
    tint: Color = HealthPitchBlack,
    width: Dp = 44.dp,
    height: Dp = 20.dp
) {
    val composePath = remember {
        PathParser().parsePathString(TEZET_SVG_PATH_DATA).toPath()
    }

    Canvas(modifier = modifier.size(width = width, height = height)) {
        val pathWidth = 456.8f
        val pathHeight = 157.6f
        val scaleX = size.width / pathWidth
        val scaleY = size.height / pathHeight
        val scaleFactor = minOf(scaleX, scaleY)

        val targetWidth = pathWidth * scaleFactor
        val targetHeight = pathHeight * scaleFactor
        val offsetX = (size.width - targetWidth) / 2f - 27.6f * scaleFactor
        val offsetY = (size.height - targetHeight) / 2f - 177.2f * scaleFactor

        translate(left = offsetX, top = offsetY) {
            scale(scale = scaleFactor, pivot = Offset.Zero) {
                drawPath(path = composePath, color = tint)
            }
        }
    }
}

/**
 * Top App Bar Header showing the iconic TEZ vector logo mark centered
 */
@Composable
fun TezetErdhenHeaderTitle(
    modifier: Modifier = Modifier,
    tint: Color = HealthPitchBlack
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        TezetErdhenLogoMark(
            tint = tint,
            width = 52.dp,
            height = 25.dp
        )
    }
}
