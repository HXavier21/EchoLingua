package com.example.echolingua.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun ContentBeforeTranslate(
    onNewTranslateClick: () -> Unit = {}
) {
    Column {
        Text(
            text = "检测语言",
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
        )
        var text by remember {
            mutableStateOf("")
        }
        val clipboardManager = LocalClipboardManager.current
        Column(
            modifier = Modifier
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (text.isNotEmpty()) {
                        onNewTranslateClick()
                    }
                }),
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, end = 30.dp),
                placeholder = {
                    Text(
                        "输入文字",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            )
            ElevatedButton(
                onClick = {
                    val clipboardText = clipboardManager.getText()
                    if (clipboardText != null && clipboardText.length > 0) {
                        text = clipboardText.text
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
            )
            {
                Text(
                    "粘贴"
                )
            }
        }
    }
}