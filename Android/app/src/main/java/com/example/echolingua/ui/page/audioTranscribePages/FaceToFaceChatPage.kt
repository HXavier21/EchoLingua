package com.example.echolingua.ui.page.audioTranscribePages

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.FaceToFaceChatItem
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.Translator

@Composable
fun FaceToFaceChatPage(
    onCloseClick: () -> Unit = {}
) {
    var sourceText by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }
    BackHandler {
        onCloseClick()
    }
    LaunchedEffect(Unit) {
        Translator.translateWithAutoDetect(
            "Tap the microphone button to start",
            onSuccessCallback = { sourceText = it },
            sourceLanguage = "en",
            targetLanguage = LanguageSelectStateHolder.sourceLanguage.value
        )
        Translator.translateWithAutoDetect(
            "Tap the microphone button to start",
            onSuccessCallback = { targetText = it },
            sourceLanguage = "en",
            targetLanguage = LanguageSelectStateHolder.targetLanguage.value
        )
    }
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
            FaceToFaceChatItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .graphicsLayer(rotationZ = 180f),
                language = LanguageSelectStateHolder.getTargetLanguageDisplayName(),
                text = targetText
            )
            IconButton(modifier = Modifier.padding(horizontal = 12.dp),
                onClick = { onCloseClick() }) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "Close"

                )
            }
            FaceToFaceChatItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                language = LanguageSelectStateHolder.getSourceLanguageDisplayName(),
                text = sourceText
            )
        }
    }

}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
fun FaceToFaceChatPagePreview() {
    EchoLinguaTheme {
        FaceToFaceChatPage()
    }
}