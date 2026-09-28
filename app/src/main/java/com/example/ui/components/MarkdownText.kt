package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OutfitDisplayFamily

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    baseFontSize: TextUnit = 12.5.sp,
    baseColor: Color = MaterialTheme.colorScheme.onSurface,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    itemSpacing: Dp = 6.dp
) {
    if (markdown.isBlank()) return

    val lines = markdown.lines()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                continue
            }

            when {
                // Header level 1, 2, 3
                trimmed.startsWith("#") -> {
                    val level = trimmed.takeWhile { it == '#' }.length
                    val title = trimmed.dropWhile { it == '#' || it.isWhitespace() }
                    val headerSize = when (level) {
                        1 -> 16.sp
                        2 -> 14.5.sp
                        else -> 13.5.sp
                    }
                    Text(
                        text = title,
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = headerSize,
                        color = accentColor,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }

                // Bullet point item (* or - or + or digit followed by dot)
                trimmed.startsWith("* ") || trimmed.startsWith("- ") || trimmed.startsWith("+ ") -> {
                    val content = trimmed.substring(2).trim()
                    BulletItemRow(
                        content = content,
                        baseFontSize = baseFontSize,
                        baseColor = baseColor,
                        accentColor = accentColor
                    )
                }

                trimmed.matches(Regex("""^\d+\.\s+.*""")) -> {
                    val dotIndex = trimmed.indexOf('.')
                    val num = trimmed.substring(0, dotIndex + 1)
                    val content = trimmed.substring(dotIndex + 1).trim()
                    NumberedItemRow(
                        number = num,
                        content = content,
                        baseFontSize = baseFontSize,
                        baseColor = baseColor,
                        accentColor = accentColor
                    )
                }

                // Normal Paragraph
                else -> {
                    Text(
                        text = parseMarkdownToAnnotatedString(trimmed, baseColor, accentColor),
                        fontSize = baseFontSize,
                        lineHeight = (baseFontSize.value * 1.35f).sp,
                        color = baseColor
                    )
                }
            }
        }
    }
}

@Composable
private fun BulletItemRow(
    content: String,
    baseFontSize: TextUnit,
    baseColor: Color,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp, start = 2.dp, end = 8.dp)
                .size(5.dp)
                .clip(CircleShape)
                .background(accentColor)
        )
        Text(
            text = parseMarkdownToAnnotatedString(content, baseColor, accentColor),
            fontSize = baseFontSize,
            lineHeight = (baseFontSize.value * 1.35f).sp,
            color = baseColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NumberedItemRow(
    number: String,
    content: String,
    baseFontSize: TextUnit,
    baseColor: Color,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = number,
            fontWeight = FontWeight.Bold,
            fontSize = baseFontSize,
            color = accentColor,
            modifier = Modifier.width(22.dp)
        )
        Text(
            text = parseMarkdownToAnnotatedString(content, baseColor, accentColor),
            fontSize = baseFontSize,
            lineHeight = (baseFontSize.value * 1.35f).sp,
            color = baseColor,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun parseMarkdownToAnnotatedString(
    text: String,
    baseColor: Color,
    accentColor: Color
): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        val length = text.length

        while (cursor < length) {
            // Check for bold **text** or __text__
            if (cursor + 2 < length && ((text[cursor] == '*' && text[cursor + 1] == '*') || (text[cursor] == '_' && text[cursor + 1] == '_'))) {
                val delimiter = text.substring(cursor, cursor + 2)
                val end = text.indexOf(delimiter, cursor + 2)
                if (end != -1) {
                    val boldText = text.substring(cursor + 2, end)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = baseColor)) {
                        append(boldText)
                    }
                    cursor = end + 2
                    continue
                }
            }

            // Check for italic *text* or _text_
            if (text[cursor] == '*' || text[cursor] == '_') {
                val delimiter = text[cursor].toString()
                val end = text.indexOf(delimiter, cursor + 1)
                if (end != -1 && end > cursor + 1) {
                    val italicText = text.substring(cursor + 1, end)
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(italicText)
                    }
                    cursor = end + 1
                    continue
                }
            }

            // Check for inline code `code`
            if (text[cursor] == '`') {
                val end = text.indexOf('`', cursor + 1)
                if (end != -1) {
                    val codeText = text.substring(cursor + 1, end)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = accentColor)) {
                        append(codeText)
                    }
                    cursor = end + 1
                    continue
                }
            }

            // Regular character
            append(text[cursor])
            cursor++
        }
    }
}
