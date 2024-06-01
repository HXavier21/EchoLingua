package com.example.echolingua.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.ui.component.TranslateResultPageNavigationBar

@Composable
fun TranslateResultPage(
    modifier: Modifier = Modifier,
    onShowPageChange: (PageState) -> Unit = {}
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            BackHandler {
                onShowPageChange(PageState.HOME_PAGE)
            }
            Scaffold(topBar = {
                TranslateResultPageNavigationBar(
                    onBackClick = {
                        onShowPageChange(PageState.HOME_PAGE)
                    }
                )
            }, bottomBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    ExtendedFloatingActionButton(
                        onClick = { onShowPageChange(PageState.INPUT_PAGE) },
                        icon = { Icon(Icons.Filled.Add, "New Translation") },
                        text = { Text(text = "New Translation") },
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(vertical = 35.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }) {
                Surface(
                    modifier = modifier.padding(it), color = MaterialTheme.colorScheme.surface
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                Text(
                                    text = "英语",
                                )
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(
                                    text = "炫酷",
                                    fontSize = 30.sp,
                                )
                                Spacer(modifier = Modifier.size(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                            contentDescription = "report"
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Outlined.ContentCopy,
                                            contentDescription = "copy"
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 100.dp, end = 100.dp),
                            color = Color(0xFF424758)
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                Text(
                                    text = "中文（简体）",
                                )
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(
                                    text = "炫酷",
                                    fontSize = 30.sp,
                                )
                                Spacer(modifier = Modifier.size(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                            contentDescription = "report"
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Outlined.ContentCopy,
                                            contentDescription = "copy"
                                        )
                                    }
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Draw,
                                            contentDescription = "round_trip"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun TranslateResultPagePreview() {
    TranslateResultPage(onShowPageChange = {})
}