package com.example.echolingua.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.Recorder

@Composable
fun FaceToFaceChatItem(
    modifier: Modifier = Modifier,
    onRecordStart: () -> Unit = {},
    onRecordEnd: (String) -> Unit = {},
    language: String = "English",
    text: String = "Tap the mic button to start",
    recorderState: Recorder.RecorderState = Recorder.RecorderState.IDLE,
    onUnavailableClick: () -> Unit = {}
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.large), colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(15.dp)
                        .padding(top = 5.dp)
                        .padding(bottom = 35.dp)
                        .verticalScroll(rememberScrollState())
                )
            }
            Text(
                text = language, color = Color.Gray
            )
        }
        MicButton(
            onRecordStart = onRecordStart,
            onRecordEnd = { onRecordEnd(language) },
            recordState = recorderState,
            onUnavailableClick = onUnavailableClick
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
fun FaceToFaceChatLightItemPreview() {
    EchoLinguaTheme {
        FaceToFaceChatItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .graphicsLayer(rotationZ = 180f)
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
fun FaceToFaceChatItemDarkPreview() {
    EchoLinguaTheme {
        FaceToFaceChatItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
    }
}