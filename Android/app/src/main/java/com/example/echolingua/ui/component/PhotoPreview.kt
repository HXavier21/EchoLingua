package com.example.echolingua.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import kotlin.math.absoluteValue

@Composable
fun PhotoPreview(painter: Painter) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imgSize = Size.Unspecified
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .graphicsLayer {
                    imgSize = size
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .pointerInput("transform") {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (zoom * scale).coerceIn(0.8f, 5f)
                        offset = calOffset(imgSize, scale, offset + pan * scale)
                    }
                }
                .pointerInput("tap") {
                    detectTapGestures(
                        onDoubleTap = { point ->
                            val center = Offset(imgSize.width / 2, imgSize.height / 2)
                            val realPoint = offset + center - point
                            scale = if (scale <= 1f) 2f else 1f
                            offset = if (scale <= 1f) Offset.Zero else {
                                calOffset(imgSize, scale, realPoint * 2f)
                            }
                        }
                    )
                }
        )
    }
}

private fun calOffset(
    imgSize: Size,
    scale: Float,
    offsetChanged: Offset
): Offset {
    if (imgSize == Size.Unspecified) return Offset.Zero
    val px = imgSize.width * (scale - 1f).absoluteValue / 2f
    val py = imgSize.height * (scale - 1f).absoluteValue / 2f
    var np = offsetChanged
    val xDiff = np.x.absoluteValue - px
    val yDiff = np.y.absoluteValue - py
    if (xDiff > 0)
        np = np.copy(x = px * np.x.absoluteValue / np.x)
    if (yDiff > 0)
        np = np.copy(y = py * np.y.absoluteValue / np.y)
    return np
}