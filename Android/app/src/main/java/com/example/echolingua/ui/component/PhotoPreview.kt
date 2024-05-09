package com.example.echolingua.ui.component

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.Line
import okhttp3.internal.wait
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.max

private const val TAG = "PhotoPreview"

@Composable
fun PhotoPreview(
    imageFile: File,
    recognizeText: Text
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imgSize = Size.Unspecified
    var screenWidth by remember { mutableIntStateOf(0) }
    var screenHeight by remember { mutableIntStateOf(0) }
    val (imageWidth, imageHeight) = getImageResolution(imageFile)
    var widthZoomRatio by remember { mutableFloatStateOf(0f) }
    var heightZoomRatio by remember { mutableFloatStateOf(0f) }
    val boxHorizontalPadding = 0
    val boxVerticalPadding = 0
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                screenWidth = it.size.width
                screenHeight = it.size.height
                widthZoomRatio = screenWidth / imageWidth.toFloat()
                heightZoomRatio = screenHeight / imageHeight.toFloat()
            },
        color = Color.Black
    ) {
        Box(
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
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageFile),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize()) {
                for (block in recognizeText.textBlocks) {
                    for (line in block.lines) {
                        Text(
                            text = line.text,
                            style = TextStyle(
                                fontSize =
                                (getLineHeight(line) * heightZoomRatio)
                                    .coerceAtLeast(0f)
                                    .pixelToSp(),
                                background = Color.White
                            ),
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX =
                                        ((line.cornerPoints?.get(0)?.x
                                            ?: 0) - boxHorizontalPadding) * widthZoomRatio
                                    translationY =
                                        ((line.cornerPoints?.get(0)?.y
                                            ?: 0) - boxVerticalPadding) * heightZoomRatio
                                    transformOrigin = TransformOrigin(0f, 0f)
                                    rotationZ = getRotationZ(line).toFloat()
                                }
                                .height(
                                    ((getLineHeight(line) * heightZoomRatio)
                                        .coerceAtLeast(0f) + boxVerticalPadding * 2)
                                        .pixelToDp()
                                )
                                .border(0.5.dp, Color.Black)
                                .clickable {
                                    Log.d(TAG, "PhotoPreview: ${line.text} clicked")
                                },
                            maxLines = 1
                        )
                    }
                }
            }
        }
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

private fun getImageResolution(imageFile: File): Pair<Int, Int> {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(imageFile.absolutePath, options)
    val imageHeight = options.outHeight
    val imageWidth = options.outWidth
    return Pair(imageHeight, imageWidth)
}

private fun getLineHeight(line: Line): Int {
    line.cornerPoints?.get(3)?.let { bottomPoint ->
        line.cornerPoints?.get(0)?.let { topPoint ->
            return bottomPoint.y - topPoint.y
        }
    }
    return 0
}

private fun getLineWidth(line: Line): Int {
    line.cornerPoints?.get(1)?.let { rightPoint ->
        line.cornerPoints?.get(0)?.let { leftPoint ->
            return rightPoint.x - leftPoint.x
        }
    }
    return 0
}

private fun getRotationZ(line: Line): Double {
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