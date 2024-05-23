package com.example.echolingua.ui.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.echolingua.ui.page.LanguageSelectStateHolder
import com.example.echolingua.util.Translator
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "RecognizedTextPreview"

/**
 * RecognizedTextPreview is a composable function that displays the recognized text on the screen.
 * @param recognizeText The recognized text object.
 * @param widthZoomRatio The zoom ratio of the width of the recognized text.
 * @param heightZoomRatio The zoom ratio of the height of the recognized text.
 * @param spaceX The space between the left edge of the screen and the scaled image.
 * @param spaceY The space between the top edge of the screen and the scaled image.
 * @param showOriginalText A boolean value that indicates whether to show the original text or the translated text.
 */
@Composable
fun RecognizedTextPreview(
    recognizeText: Text,
    widthZoomRatio: Float,
    heightZoomRatio: Float,
    spaceX: Float = 0f,
    spaceY: Float = 0f,
    showOriginalText: Boolean = false
) {
    val boxHorizontalPadding = 20f
    val boxVerticalPadding = 5f
    Box(modifier = Modifier.fillMaxSize()) {
        for (block in recognizeText.textBlocks) {
            for (line in block.lines) {
                val realWidth = getLineWidth(line) * widthZoomRatio
                val realHeight = getLineHeight(line) * heightZoomRatio
                var textToShow by remember { mutableStateOf(line.text) }
                var translatedText by remember { mutableStateOf("") }
                LaunchedEffect(
                    LanguageSelectStateHolder.sourceLanguage.value,
                    LanguageSelectStateHolder.targetLanguage.value
                ) {
                    withContext(Dispatchers.Default) {
                        Log.d(TAG, "RecognizedTextPreview: translate text")
                        Translator.translateWithAutoDetect(line.text) { result ->
                            translatedText = result
                            if (!showOriginalText) {
                                textToShow = result
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationX =
                                spaceX + (line.cornerPoints?.get(0)?.x
                                    ?: 0) * widthZoomRatio
                            translationY =
                                spaceY + (line.cornerPoints?.get(0)?.y
                                    ?: 0) * heightZoomRatio
                            transformOrigin = TransformOrigin(0f, 0f)
                            rotationZ = getRotationZ(line).toFloat()
                        }
                        .offset(
                            x = -boxHorizontalPadding.pixelToDp(),
                            y = -boxVerticalPadding.pixelToDp()
                        )
                        .size(
                            (realWidth.coerceAtLeast(0f) + boxHorizontalPadding * 2).pixelToDp(),
                            (realHeight.coerceAtLeast(0f) + boxVerticalPadding * 2).pixelToDp()
                        )
                        .background(
                            color = Color.White.copy(alpha = if (showOriginalText) 0.2f else 0.9f),
                            shape = MaterialTheme.shapes.extraSmall
                        )
                ) {
                    if (!showOriginalText) {
                        AutoResizeText(
                            text = textToShow,
                            realWidth = realWidth,
                            realHeight = realHeight,
                            horizontalPadding = boxHorizontalPadding,
                            verticalPadding = boxVerticalPadding
                        )
                    }

                }
            }
        }
    }
}

private fun getLineHeight(line: Text.Line): Float {
    line.cornerPoints?.get(3)?.let { bottomPoint ->
        line.cornerPoints?.get(0)?.let { topPoint ->
            return sqrt(
                (bottomPoint.y - topPoint.y).toDouble().pow(2) +
                        (bottomPoint.x - topPoint.x).toDouble().pow(2)
            ).toFloat()
        }
    }
    return 0f
}

private fun getLineWidth(line: Text.Line): Float {
    line.cornerPoints?.get(1)?.let { rightPoint ->
        line.cornerPoints?.get(0)?.let { leftPoint ->
            return sqrt(
                (rightPoint.y - leftPoint.y).toDouble().pow(2) +
                        (rightPoint.x - leftPoint.x).toDouble().pow(2)
            ).toFloat()
        }
    }
    return 0f
}

private fun getRotationZ(line: Text.Line): Double {
    line.cornerPoints?.get(0)?.let { leftPoint ->
        line.cornerPoints?.get(1)?.let { rightPoint ->
            return Math.toDegrees(
                atan2(
                    (rightPoint.y - leftPoint.y).toDouble(),
                    (rightPoint.x - leftPoint.x).toDouble()
                )
            )
        }
    }
    return 0.0
}

@Composable
fun Number.pixelToDp(): Dp = with(LocalDensity.current) {
    toFloat().toDp()
}

@Composable
fun Number.pixelToSp(): TextUnit = with(LocalDensity.current) {
    toFloat().toSp()
}