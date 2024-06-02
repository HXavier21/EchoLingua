package com.example.echolingua.ui.component

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.CustomRippleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MicButton"

@Composable
fun MicButton(
    initialRecordState: Boolean = false,
    onRecordStart: () -> Unit = {},
    onRecordEnd: () -> Unit = {},
) {
    var isRecording by remember { mutableStateOf(initialRecordState) }
    val animatedCornerSize by animateIntAsState(
        targetValue = if (isRecording) 10 else 50,
        label = ""
    )
    val scaleRatio by animateFloatAsState(
        targetValue = if (isRecording) 0.53f else 0.75f,
        label = ""
    )
    var buttonAngle by remember {
        mutableFloatStateOf(0f)
    }
    val animateButtonAngle by animateFloatAsState(
        targetValue = buttonAngle,
        label = "",
        animationSpec = tween(1000, easing = LinearEasing)
    )
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                buttonAngle = System.currentTimeMillis() % 360f
                while (true) {
                    Log.d(TAG, "MicButton: $buttonAngle")
                    buttonAngle += 80f
                    delay(1000)
                }
            }
        }
        onDispose {
            coroutineScope.cancel()
        }
    }
    Box(contentAlignment = Alignment.Center) {
        LargeFloatingActionButton(
            onClick = {},
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(animatedCornerSize),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .scale(scaleRatio)
                .graphicsLayer(rotationZ = 45f + animateButtonAngle),
        ) {}
        CompositionLocalProvider(LocalRippleTheme provides CustomRippleTheme()) {
            LargeFloatingActionButton(
                onClick = {
                    if (isRecording) {
                        onRecordEnd()
                    } else {
                        onRecordStart()

                    }
                    isRecording = !isRecording
                },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(animatedCornerSize),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .scale(scaleRatio)
                    .graphicsLayer(rotationZ = animateButtonAngle),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {}
            val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
            when (isRecording) {
                false -> Icon(
                    imageVector = Icons.Outlined.MicNone,
                    contentDescription = "Mic",
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                true -> Canvas(modifier = Modifier.matchParentSize()) {
                    drawLine(
                        start = Offset(size.width / 2 - 15, size.height / 2 - 10),
                        end = Offset(size.width / 2 - 15, size.height / 2 + 10),
                        color = onPrimaryColor,
                        strokeWidth = 15f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        start = Offset(size.width / 2 + 15, size.height / 2 - 10),
                        end = Offset(size.width / 2 + 15, size.height / 2 + 10),
                        color = onPrimaryColor,
                        strokeWidth = 15f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
fun MicButtonPreview() {
    MicButton()
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
fun MicButtonDarkPreview() {
    MicButton(initialRecordState = true)
}