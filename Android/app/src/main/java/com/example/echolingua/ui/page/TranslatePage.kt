package com.example.echolingua.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.nl.translate.TranslateLanguage

@Preview
@Composable
fun TranslatePage(
    translatePageViewModel: TranslatePageViewModel = viewModel()
) {
    val text by translatePageViewModel.textFlow.collectAsState()
    val sourceLanguage by translatePageViewModel.sourceLanguageFlow.collectAsState()
    val targetLanguage by translatePageViewModel.targetLanguageFlow.collectAsState()
    val translatedText by translatePageViewModel.translatedTextFlow.collectAsState()
    var leftExpanded by remember {
        mutableStateOf(false)
    }
    var rightExpanded by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (sourceLanguage) {
                                "" -> "Source"
                                else -> sourceLanguage
                            }
                        )
                    },
                    onClick = { leftExpanded = true }
                )
                DropdownMenu(
                    expanded = leftExpanded,
                    onDismissRequest = { leftExpanded = false }
                ) {
                    for (language in TranslateLanguage.getAllLanguages()) {
                        DropdownMenuItem(
                            text = { Text(language) },
                            onClick = {
                                translatePageViewModel.setSourceLanguage(language)
                                leftExpanded = false
                            }
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (targetLanguage) {
                                "" -> "Target"
                                else -> targetLanguage
                            }
                        )
                    },
                    onClick = { rightExpanded = true }
                )
                DropdownMenu(
                    expanded = rightExpanded,
                    onDismissRequest = { rightExpanded = false }
                ) {
                    for (language in TranslateLanguage.getAllLanguages()) {
                        DropdownMenuItem(
                            text = { Text(language) },
                            onClick = {
                                translatePageViewModel.setTargetLanguage(language)
                                rightExpanded = false
                            }
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            value = text,
            onValueChange = {
                translatePageViewModel.setText(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        translatePageViewModel.translate()
                    }
                )
            }
        )
        Text(text = translatedText, modifier = Modifier.padding(20.dp))
    }
}