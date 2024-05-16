package com.example.echolingua.ui.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "AutoResizeText"

@Composable
fun AutoResizeText(
    text: String,
    realWidth: Float,
    realHeight: Float,
    horizontalPadding:Float
) {
    var letterPadding by remember { mutableFloatStateOf(0f) }
    var fontSize by remember { mutableFloatStateOf(0f) }
    var paint by remember { mutableStateOf(Paint().asFrameworkPaint()) }
    LaunchedEffect(text) {
        fontSize = 0f
        letterPadding = 0f
        fontSize = realHeight.coerceAtLeast(0f)
        var tempPaint = Paint().asFrameworkPaint()
        tempPaint.textSize = fontSize
        withContext(Dispatchers.Default) {
            while (tempPaint.measureText(text) > realWidth) {
                fontSize -= 0.01f
                tempPaint = Paint().asFrameworkPaint().apply {
                    textSize = fontSize
                    letterSpacing = letterPadding
                }
            }
            while (tempPaint.measureText(text) < realWidth) {
                letterPadding += 0.01f
                tempPaint = Paint().asFrameworkPaint().apply {
                    textSize = fontSize
                    letterSpacing = letterPadding
                }
            }
            paint = tempPaint
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(text) {
                myDetectTapGestures(
                    onTap = {
                        Log.d(
                            TAG,
                            "RecognizedTextPreview: textWidth =" +
                                    " ${paint.measureText(text)}, " +
                                    "lineLength = $realWidth, " +
                                    "textHeight = ${paint.textSize}, " +
                                    "lineHeight = $realHeight " +
                                    "text = $text"
                        )
                    }
                )
            }
    ) {
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                text,
                horizontalPadding,
                realHeight,
                paint
            )
        }
    }

}