package com.example.echolingua.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp

object TextProcessUtil {

    @Composable
    fun calTextStyle(
        text: String, maxWidthInPx: Int, updateStyleCallback: (TextStyle) -> Unit
    ): TextStyle {
        val headLineLargeStyle = MaterialTheme.typography.headlineLarge
        val headLineMediumStyle = MaterialTheme.typography.headlineMedium
        val headLineSmallStyle = MaterialTheme.typography.headlineSmall
        val titleLargeStyle = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.5.sp
        )
        val bodyLargeStyle = MaterialTheme.typography.bodyLarge
        val styleList = listOf(
            headLineLargeStyle,
            headLineMediumStyle,
            headLineSmallStyle,
            titleLargeStyle,
            bodyLargeStyle
        )
        for (i in 0..4) {
            if (calTextLine(
                    text = text, textStyle = styleList[i], maxWidthInPx = maxWidthInPx
                ) < i + 3
            ) {
                updateStyleCallback(styleList[i])
                return styleList[i]
            }
        }
        updateStyleCallback(bodyLargeStyle)
        return bodyLargeStyle
    }

    @Composable
    fun calTextLine(text: String, textStyle: TextStyle, maxWidthInPx: Int): Int {
        val textMeasurer = rememberTextMeasurer()
        val textLayoutResult: TextLayoutResult = textMeasurer.measure(
            text = text,
            style = textStyle,
            constraints = Constraints(maxWidth = maxWidthInPx),
        )
        return textLayoutResult.lineCount
    }
}