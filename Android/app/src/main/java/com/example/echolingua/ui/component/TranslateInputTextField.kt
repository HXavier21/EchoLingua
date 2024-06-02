package com.example.echolingua.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.TextProcessUtil.calTextStyle

private const val TAG = "TranslateInputTextField"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TranslateInputTextField(
    modifier: Modifier = Modifier,
    initialText: String = "",
    updateStyleCallback: (TextStyle) -> Unit = {},
    onEnterPressed: (String) -> Unit,
    pasteText: (String) -> Unit = {}
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
    var textWidth by remember { mutableIntStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    val clipboardManager = LocalClipboardManager.current
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(Unit) {
        textFieldValue = TextFieldValue(
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
                .matchParentSize()
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                TextField(value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    placeholder = {
                        Text("Enter text",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    textWidth = it.size.width
                                })
                    },
                    textStyle = calTextStyle(text = textFieldValue.text, maxWidthInPx = textWidth) {
                        updateStyleCallback(it)
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
                    keyboardActions = KeyboardActions(onDone = {
                        onEnterPressed(textFieldValue.text) // 当按下回车键时触发
                    }),
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            bottom = if (textFieldValue.text.isNotEmpty()) 50.dp else 0.dp
                        )
                        .fillMaxWidth()
                        .focusRequester(focusRequester) // 使用FocusRequester修饰输入框
                )
                if (textFieldValue.text.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .padding(end = 20.dp), horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            textFieldValue = TextFieldValue("")
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear, contentDescription = "Clear"
                            )
                        }
                        AnimatedVisibility(visible = !isImeVisible) {
                            IconButton(
                                onClick = {
                                    onEnterPressed(textFieldValue.text)
                                }, colors = IconButtonDefaults.iconButtonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                    contentDescription = "Translate"
                                )
                            }
                        }
                    }
                }
            }
            if (textFieldValue.text.isEmpty()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            clipboardManager.getText()?.let {
                                pasteText(it.text)
                                textFieldValue = TextFieldValue(
                                    text = it.text, selection = TextRange(it.text.length)
                                )
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
    }
}

@Composable
@Preview
fun TranslateInputTextFieldPreview() {
    EchoLinguaTheme {
        TranslateInputTextField(modifier = Modifier.fillMaxSize(), onEnterPressed = {})
    }
}