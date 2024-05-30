package com.example.echolingua.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.page.SelectMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    searchText: String,
    onValueChange: (String) -> Unit,
    selectMode: SelectMode,
    onBackClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    TopAppBar(
        title = {
            BasicTextField(value = searchText,
                onValueChange = { onValueChange(it) },
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
                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (searchText.isEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier
                                            .offset(y = 1.dp)
                                            .padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = if (selectMode == SelectMode.SOURCE) "Translate from"
                                        else "Translate to",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        modifier = Modifier
                                            .offset(y = 1.dp)
                                            .padding(horizontal = 5.dp)
                                            .clickable {
                                                onValueChange("")
                                            }
                                    )
                                }
                            }
                            innerTextField()
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Canvas(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height + 20),
                                end = Offset(size.width, size.height + 20),
                                strokeWidth = 1.5f
                            )
                        }
                    }
                })
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