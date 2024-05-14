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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
 */
@Composable
fun RecognizedTextPreview(
    recognizeText: Text,
    widthZoomRatio: Float,
    heightZoomRatio: Float,
    spaceX: Float = 0f,
    spaceY: Float = 0f
) {
    val boxHorizontalPadding = 20
    val boxVerticalPadding = 5
    Box(modifier = Modifier.fillMaxSize()) {
        for (block in recognizeText.textBlocks) {
            for (line in block.lines) {
                var letterPadding by remember { mutableFloatStateOf(0f) }
                var fontSize by remember { mutableFloatStateOf(0f) }
                val realWidth = getLineWidth(line) * widthZoomRatio
                val realHeight = getLineHeight(line) * heightZoomRatio
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
                            color = Color.White.copy(alpha = 0.9f),
                            shape = MaterialTheme.shapes.extraSmall
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    var paint by remember { mutableStateOf(Paint().asFrameworkPaint()) }
                    LaunchedEffect(key1 = line) {
                        fontSize = realHeight.coerceAtLeast(0f)
                        paint.textSize = fontSize
                        withContext(Dispatchers.IO) {
                            while (paint.measureText(line.text) > realWidth) {
                                fontSize -= 0.1f
                                paint = Paint().asFrameworkPaint().apply {
                                    textSize = fontSize
                                    letterSpacing = letterPadding
                                }
                            }
                            while (paint.measureText(line.text) < realWidth) {
                                letterPadding += 0.01f
                                paint = Paint().asFrameworkPaint().apply {
                                    textSize = fontSize
                                    letterSpacing = letterPadding
                                }
                            }
                        }
                    }
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(line) {
                                myDetectTapGestures(
                                    onTap = {
                                        Log.d(
                                            TAG,
                                            "RecognizedTextPreview: textWidth =" +
                                                    " ${paint.measureText(line.text)}, " +
                                                    "lineLength = $realWidth, " +
                                                    "textHeight = ${paint.textSize}, " +
                                                    "lineHeight = $realHeight " +
                                                    "text = ${line.text}"
                                        )
                                    }
                                )
                            }
                    ) {
                        drawIntoCanvas {
                            it.nativeCanvas.drawText(
                                line.text,
                                boxHorizontalPadding.toFloat(),
                                realHeight,
                                paint
                            )
                        }
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
private fun Number.pixelToDp(): Dp = with(LocalDensity.current) {
    toFloat().toDp()
}

@Composable
private fun Number.pixelToSp(): TextUnit = with(LocalDensity.current) {
    toFloat().toSp()
}