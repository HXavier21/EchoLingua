package com.example.echolingua.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme

private const val TAG = "TranslateInputTextField"

@Composable
fun TranslateInputTextField(
    modifier: Modifier = Modifier,
    initialText: String = "",
    onEnterPressed: () -> Unit,
    pasteText: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(TextFieldValue()) }

    // 创建 FocusRequester 对象
    val focusRequester = remember { FocusRequester() }

    // 创建 MutableState<Boolean> 对象保存焦点状态
    val isFocused = remember { mutableStateOf(true) }

    // 创建 MutableState<Boolean> 对象保存按钮显示状态
    val isButtonVisible = remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(initialText) {
        text = TextFieldValue(
            text = initialText, selection = TextRange(initialText.length)
        )
        focusRequester.requestFocus()
    }
    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
        Column(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .matchParentSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        "Enter text",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                textStyle = MaterialTheme.typography.headlineLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surface
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    onEnterPressed() // 当按下回车键时触发
                }),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused.value = it.isFocused
                        isButtonVisible.value = it.isFocused // 当焦点状态改变时，更新按钮显示状态
                    }
                    .focusRequester(focusRequester) // 使用FocusRequester修饰输入框

            )
            Button(
                onClick = {
                    clipboardManager.getText()?.let {
                        pasteText(it.text)
                        Log.d(TAG, "TranslateInputTextField: ${it.text}")
                    }
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(20.dp),
                contentPadding = PaddingValues(horizontal = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentPaste,
                    contentDescription = "Paste",
                    modifier = Modifier.scale(0.8f)
                )
                Text(text = "Paste", modifier = Modifier.padding(horizontal = 6.dp))
            }
        }
    }
}

@Composable
@Preview
fun TranslateInputTextFieldPreview() {
    EchoLinguaTheme {
        TranslateInputTextField(modifier = Modifier.fillMaxSize(), onEnterPressed = {})
    }
}