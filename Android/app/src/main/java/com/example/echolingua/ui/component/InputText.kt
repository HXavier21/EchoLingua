package com.example.echolingua.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    onEnterPressed: () -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue()) }

    // 创建 FocusRequester 对象
    val focusRequester = remember { FocusRequester() }

    // 创建 MutableState<Boolean> 对象保存焦点状态
    val isFocused = remember { mutableStateOf(true) }

    // 创建 MutableState<Boolean> 对象保存按钮显示状态
    val isButtonVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 请求焦点
        focusRequester.requestFocus()
    }

    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(MaterialTheme.colorScheme.surface)
        )

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    "输入文字",
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onEnterPressed() // 当按下回车键时触发
                }
            ),
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .fillMaxSize()
                .onFocusChanged {
                    isFocused.value = it.isFocused
                    isButtonVisible.value = it.isFocused // 当焦点状态改变时，更新按钮显示状态
                }
                .focusRequester(focusRequester) // 使用FocusRequester修饰输入框

        )
    }
}