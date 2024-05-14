package com.example.echolingua.ui.page

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.LanguageSelectItem
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlin.math.truncate

enum class SelectMode {
    SOURCE, TARGET
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectPage(
    selectMode: SelectMode = SelectMode.SOURCE,
    currentLanguage: String = "",
    onBackClick: () -> Unit = {},
) {
    var selectedLanguage by remember {
        mutableStateOf(currentLanguage)
    }
    var isSearching by remember {
        mutableStateOf(false)
    }
    var searchText by remember {
        mutableStateOf("")
    }
    val languageMap = LanguageSelectStateHolder.getLanguageCodeNameMap()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    if (isSearching) {
        BackHandler {
            isSearching = false
        }
    }
    LaunchedEffect(key1 = currentLanguage) {
        selectedLanguage = when (selectMode) {
            SelectMode.SOURCE -> LanguageSelectStateHolder.sourceLanguage.value
            SelectMode.TARGET -> LanguageSelectStateHolder.targetLanguage.value
        }
        TranslateModelStateHolder.refreshWholeModelStateMap()
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            when (isSearching) {
                true -> {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                    TopAppBar(
                        title = {
                            BasicTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize

                                ),
                                singleLine = true,
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                decorationBox = { innerTextField ->
                                    Column(modifier = Modifier.wrapContentSize()) {
                                        Box(
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .offset(y = 2.dp)
                                        ) {
                                            if (searchText.isEmpty()) {
                                                Row(
                                                    modifier = Modifier.wrapContentSize(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Search,
                                                        contentDescription = null,
                                                        tint = Color.Gray,
                                                        modifier = Modifier
                                                            .offset(y = 1.dp)
                                                            .padding(horizontal = 5.dp)
                                                    )
                                                    Text(
                                                        text = "Source Language...",
                                                        color = Color.Gray,
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                            }
                                            innerTextField()
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Canvas(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            drawLine(
                                                color = Color.Gray,
                                                start = Offset(0f, size.height + 20),
                                                end = Offset(size.width, size.height + 20),
                                                strokeWidth = 1.5f
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        navigationIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        onBackClick()
                                    })
                        },
                        actions = {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                modifier = Modifier.padding(10.dp)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                    )
                }

                false -> {
                    LargeTopAppBar(title = {
                        when (selectMode) {
                            SelectMode.SOURCE -> {
                                Text(
                                    text = "Source Language",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            SelectMode.TARGET -> {
                                Text(
                                    text = "Target Language",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }, navigationIcon = {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    onBackClick()
                                })
                    }, actions = {
                        Icon(imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    isSearching = true
                                })
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            modifier = Modifier.padding(10.dp)
                        )
                    }, scrollBehavior = scrollBehavior
                    )
                }
            }
        }
    ) {
        if (isSearching && searchText.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(it)) {
                for ((key, name) in languageMap) {
                    if (name.contains(searchText, ignoreCase = true)) {
                        item {
                            LanguageSelectItem(
                                key = key,
                                name = name,
                                selectedLanguage = selectedLanguage,
                                translateModelStatus = TranslateModelStateHolder.modelStateMap[key]
                                    ?: TranslateModelStatus.NOT_DOWNLOADED,
                                onItemClick = {
                                    selectedLanguage = key
                                    LanguageSelectStateHolder.setLanguage(
                                        key, selectMode
                                    )
                                    onBackClick()
                                },
                                onDownloadClick = {
                                    TranslateModelStateHolder.downloadModel(key)
                                }
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(it)) {
                item {
                    if (selectMode == SelectMode.SOURCE) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(
                                    if (selectedLanguage == "detect") {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .clickable {
                                    selectedLanguage = "detect"
                                    LanguageSelectStateHolder.setLanguage(
                                        "detect", selectMode
                                    )
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selectedLanguage == "detect") {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 15.dp)
                                )
                            }
                            Text(
                                text = "Auto detect",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(vertical = 5.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(end = 10.dp)
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = "Currently selected languages",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                for ((key, name) in languageMap) {
                    if (key == TranslateLanguage.CHINESE || key == TranslateLanguage.ENGLISH) {
                        item {
                            var isModelDownloaded by remember {
                                mutableStateOf(false)
                            }
                            LaunchedEffect(key1 = key) {
                                TranslateModelStateHolder.checkIfModelExists(key) {
                                    isModelDownloaded = it
                                }
                            }
                            LanguageSelectItem(key = key,
                                name = name,
                                selectedLanguage = selectedLanguage,
                                translateModelStatus = TranslateModelStateHolder.modelStateMap[key]
                                    ?: TranslateModelStatus.NOT_DOWNLOADED,
                                onItemClick = {
                                    selectedLanguage = key
                                    LanguageSelectStateHolder.setLanguage(
                                        key, selectMode
                                    )
                                },
                                onDownloadClick = {
                                    TranslateModelStateHolder.downloadModel(key)
                                })
                        }
                    }
                }
                item {
                    Text(
                        text = "All languages",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(20.dp)
                            .padding(top = 10.dp)
                    )
                }
                for ((key, name) in languageMap) {
                    item {
                        var isModelDownloaded by remember {
                            mutableStateOf(false)
                        }
                        LaunchedEffect(key1 = key) {
                            TranslateModelStateHolder.checkIfModelExists(key) {
                                isModelDownloaded = it
                            }
                        }
                        LanguageSelectItem(key = key,
                            name = name,
                            selectedLanguage = selectedLanguage,
                            translateModelStatus = TranslateModelStateHolder.modelStateMap[key]
                                ?: TranslateModelStatus.NOT_DOWNLOADED,
                            onItemClick = {
                                selectedLanguage = key
                                LanguageSelectStateHolder.setLanguage(
                                    key, selectMode
                                )
                            },
                            onDownloadClick = {
                                TranslateModelStateHolder.downloadModel(key)
                            })
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun LanguageSelectPageDarkPreview() {
    EchoLinguaTheme {
        LanguageSelectPage(currentLanguage = TranslateLanguage.CHINESE)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun LanguageSelectPageLightPreview() {
    EchoLinguaTheme {
        LanguageSelectPage(
            selectMode = SelectMode.TARGET, currentLanguage = TranslateLanguage.CHINESE
        )
    }
}