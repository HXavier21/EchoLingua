package com.example.echolingua.ui.page

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.LanguageSelectItem
import com.example.echolingua.ui.component.ModelManageDialog
import com.example.echolingua.ui.component.TopSearchBar
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.google.mlkit.nl.translate.TranslateLanguage

enum class SelectMode {
    SOURCE, TARGET
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectPage(
    selectMode: SelectMode = SelectMode.SOURCE,
    currentLanguage: String = "",
    onBackClick: () -> Unit = {},
    onModelStateClick: (String) -> Unit = {},
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
    val languageMap = LanguageSelectStateHolder.languageMap
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    BackHandler {
        if (isSearching) {
            isSearching = false
        } else {
            onBackClick()
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
                    TopSearchBar(searchText = searchText, onValueChange = {
                        searchText = it
                    }, selectMode = selectMode, onBackClick = {
                        isSearching = false
                    })
                }

                false -> {
                    LargeTopAppBar(title = {
                        when (selectMode) {
                            SelectMode.SOURCE -> {
                                Text(
                                    text = "Translate from",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }

                            SelectMode.TARGET -> {
                                Text(
                                    text = "Translate to",
                                    style = MaterialTheme.typography.headlineSmall
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
        }) { innerPadding ->
        if (isSearching && searchText.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                for ((key, name) in languageMap) {
                    if (name.contains(searchText, ignoreCase = true)) {
                        item {
                            LanguageSelectItem(key = key,
                                name = name,
                                selectedLanguage = selectedLanguage,
                                translateModelState = TranslateModelStateHolder.modelStateMap[key]
                                    ?: TranslateModelState.NOT_DOWNLOADED,
                                onItemClick = {
                                    selectedLanguage = key
                                    LanguageSelectStateHolder.setLanguage(
                                        key, selectMode
                                    )
                                    onBackClick()
                                },
                                onModelStateClick = {
                                    onModelStateClick(it)
                                })
                        }
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
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
                                    onBackClick()
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
                        text = "Recent languages",
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
                                translateModelState = TranslateModelStateHolder.modelStateMap[key]
                                    ?: TranslateModelState.NOT_DOWNLOADED,
                                onItemClick = {
                                    selectedLanguage = key
                                    LanguageSelectStateHolder.setLanguage(
                                        key, selectMode
                                    )
                                    onBackClick()
                                },
                                onModelStateClick = {
                                    onModelStateClick(it)
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
                            translateModelState = TranslateModelStateHolder.modelStateMap[key]
                                ?: TranslateModelState.NOT_DOWNLOADED,
                            onItemClick = {
                                selectedLanguage = key
                                LanguageSelectStateHolder.setLanguage(
                                    key, selectMode
                                )
                                onBackClick()
                            },
                            onModelStateClick = {
                                onModelStateClick(it)
                            })
                    }
                }
            }
        }
    }
    val isManagingModel by TranslateModelStateHolder.isManagingModel
    val modelLanguage by TranslateModelStateHolder.currentLanguage
    if (isManagingModel) {
        ModelManageDialog(
            translateModelState = TranslateModelStateHolder.getModelState(modelLanguage),
            language = LanguageSelectStateHolder.getDisplayNameByKey(modelLanguage),
            onDismissRequest = {
                TranslateModelStateHolder.isManagingModel.value = false
            },
            onDownloadRequest = {
                TranslateModelStateHolder.downloadModel(modelLanguage)
                TranslateModelStateHolder.isManagingModel.value = false
            },
            onRemoveRequest = {
                TranslateModelStateHolder.deleteModel(modelLanguage)
                TranslateModelStateHolder.isManagingModel.value = false
            }
        )
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