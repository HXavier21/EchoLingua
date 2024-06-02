package com.example.echolingua.ui.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme

private const val TAG = "ContentBeforeTranslate"

enum class TransformMode {
    Clear, NewTranslation
}

@Composable
fun ContentBeforeTranslate(
    initialText: String = "",
    transformMode: TransformMode = TransformMode.NewTranslation,
    sourceLanguage: String = "Auto detect",
    onTranslateClick: (String) -> Unit = {},
    onSourceLanguageClick: () -> Unit = {}
) {
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialText, selection = TextRange(initialText.length)
            )
        )
    }
    val clipboardManager = LocalClipboardManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val surfaceColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(Unit) {
        if (transformMode == TransformMode.NewTranslation) {
            focusRequester.requestFocus()
        }
    }
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp)
        ) {
            Text(text = sourceLanguage,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .clickable(
                        interactionSource = interactionSource, indication = null
                    ) {
                        onSourceLanguageClick()
                    })
            Box {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            min = 65.dp, max = 260.dp
                        )
                        .focusRequester(focusRequester)
                        .animateContentSize(),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (text.text.isNotEmpty()) {
                            onTranslateClick(text.text)
                        }
                    }),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                ) { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (text.text.isEmpty()) {
                            Text(
                                "Enter text", style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            )
                        }
                        innerTextField()
                    }
                }
                Column(modifier = Modifier.matchParentSize()) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                    ) {
                        val brush = Brush.verticalGradient(
                            colors = listOf(
                                surfaceColor, Color.Transparent
                            )
                        )
                        drawRect(
                            brush = brush, size = this.size
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                    ) {
                        val brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, surfaceColor
                            )
                        )
                        drawRect(
                            brush = brush, size = this.size
                        )
                    }
                }
            }
            AnimatedContent(targetState = text.text.isNotEmpty(), label = "") {
                if (it) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { onTranslateClick(text.text) },
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.East,
                                contentDescription = "Translate",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    ) {
                        Button(
                            onClick = {
                                val clipboardText = clipboardManager.getText()
                                if (!clipboardText.isNullOrEmpty()) {
                                    text = TextFieldValue(
                                        text = clipboardText.text,
                                        selection = TextRange(clipboardText.text.length)
                                    )
                                }
                            }, colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                "Paste"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ContentBeforeTranslatePreview() {
    EchoLinguaTheme {
        ContentBeforeTranslate(
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ContentBeforeTranslateDarkPreview() {
    EchoLinguaTheme {
        ContentBeforeTranslate(
            initialText = "Hello, World!"
        )
    }
}