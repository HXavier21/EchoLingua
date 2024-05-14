package com.example.echolingua.ui.component

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.exifinterface.media.ExifInterface
import coil.compose.rememberAsyncImagePainter
import com.google.mlkit.vision.text.Text
import java.io.File
import kotlin.math.absoluteValue

private const val TAG = "PhotoPreview"

@Composable
fun PhotoPreview(
    imageFile: File,
    recognizeText: Text
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var screenWidth by remember { mutableFloatStateOf(0f) }
    var screenHeight by remember { mutableFloatStateOf(0f) }
    val (imageWidth, imageHeight) = getImageResolution(imageFile)
    var imageSize = Size.Zero
    var widthZoomRatio by remember { mutableFloatStateOf(0f) }
    var heightZoomRatio by remember { mutableFloatStateOf(0f) }
    var spaceX by remember { mutableFloatStateOf(0f) }
    var spaceY by remember { mutableFloatStateOf(0f) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                screenWidth = it.size.width.toFloat()
                screenHeight = it.size.height.toFloat()
                val (fakeImageWidth, fakeImageHeight) = getExpandedImageResolution(
                    Pair(screenWidth, screenHeight),
                    Pair(imageWidth, imageHeight)
                )
                widthZoomRatio = screenWidth / fakeImageWidth
                heightZoomRatio = screenHeight / fakeImageHeight
                val spaceXY = getSpaceXY(
                    Pair(screenWidth, screenHeight),
                    Pair(imageWidth, imageHeight),
                    Pair(widthZoomRatio, heightZoomRatio)
                )
                spaceX = spaceXY.first
                spaceY = spaceXY.second
            },
        color = Color.Black
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    imageSize = size
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .pointerInput("transform") {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (zoom * scale).coerceIn(0.6f, 5f)
                        offset =
                            if (scale >= 1) calOffset(imageSize, scale, offset + pan * scale)
                            else Offset(0f, pan.y * scale)
                    }
                }
                .pointerInput("tap") {
                    detectTapGestures(
                        onDoubleTap = { point ->
                            val center = Offset(imageSize.width / 2, imageSize.height / 2)
                            val realPoint = offset + center - point
                            scale = if (scale <= 1f) 2f else 1f
                            offset = if (scale <= 1f) Offset.Zero else {
                                calOffset(imageSize, scale, realPoint * 2f)
                            }
                            Log.d(
                                TAG,
                                "PhotoPreview: imageWidth: $imageWidth, imageHeight: $imageHeight" +
                                        "imageSizeWidth: ${imageSize.width}, imageSizeHeight: ${imageSize.height}"
                            )
                        }
                    )
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageFile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
            RecognizedTextPreview(
                recognizeText = recognizeText,
                widthZoomRatio = widthZoomRatio,
                heightZoomRatio = heightZoomRatio,
                spaceX = spaceX,
                spaceY = spaceY
            )
        }
    }
}

private fun calOffset(
    imgSize: Size,
    scale: Float,
    offsetChanged: Offset
): Offset {
    val px = imgSize.width * (scale - 1f).absoluteValue / 2f
    val py = imgSize.height * (scale - 1f).absoluteValue / 2f
    var np = offsetChanged
    val xDiff = np.x.absoluteValue - px
    val yDiff = np.y.absoluteValue - py
    if (xDiff > 100)
        np = np.copy(x = (px + 100) * np.x.absoluteValue / np.x)
    if (yDiff > 100)
        np = np.copy(y = (py + 100) * np.y.absoluteValue / np.y)
    return np
}

private fun getImageResolution(imageFile: File): Pair<Int, Int> {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(imageFile.absolutePath, options)
    val imageHeight = options.outHeight
    val imageWidth = options.outWidth
    val exifInterface = ExifInterface(imageFile.absolutePath)
    val orientation = when (
        exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
    ) {
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        else -> 0
    }
    return if (orientation == 90 || orientation == 270) {
        Pair(imageHeight, imageWidth)
    } else {
        Pair(imageWidth, imageHeight)
    }
}

private fun getExpandedImageResolution(
    screenSize: Pair<Float, Float>,
    imageSize: Pair<Int, Int>
): Pair<Float, Float> {
    val widthRatio = imageSize.first.toFloat() / screenSize.first
    val heightRatio = imageSize.second.toFloat() / screenSize.second
    val maxRatio = widthRatio.coerceAtLeast(heightRatio)
    return Pair(screenSize.first * maxRatio, screenSize.second * maxRatio)
}

private fun getSpaceXY(
    screenSize: Pair<Float, Float>,
    imageSize: Pair<Int, Int>,
    ratio: Pair<Float, Float>
): Pair<Float, Float> {
    val widthRatio = ratio.first
    val heightRatio = ratio.second
    val spaceX = (screenSize.first - imageSize.first * widthRatio) / 2
    val spaceY = (screenSize.second - imageSize.second * heightRatio) / 2
    return Pair(spaceX, spaceY)
}