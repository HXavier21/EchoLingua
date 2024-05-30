package com.example.echolingua.ui.page

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.twotone.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.R
import com.example.echolingua.ui.component.TextInput
import com.example.echolingua.ui.theme.EchoLinguaTheme

private const val TAG = "MainTranslatePage"

// 定义页面状态枚举
enum class PageState {
    MAIN_PAGE,
    INPUT_PAGE,
    DISPLAY_PAGE
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainTranslatePage(
    onNavigateToDataPage: () -> Unit = {},
    modifier: Modifier = Modifier,
    currentPage: PageState = PageState.MAIN_PAGE
) {
    var showPage by remember { mutableStateOf(currentPage) }

    fun onNewTranslationClicked() {
        showPage = PageState.INPUT_PAGE
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (showPage) {
                PageState.MAIN_PAGE -> {
                    MainPageNavigationBar(onNavigateToDataPage = onNavigateToDataPage)
                    Box(
                        modifier = modifier.weight(1f)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        )
                        Column(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.extraLarge)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() }, // 设置interactionSource
                                    indication = null, // 设置为 null 禁用涟漪效果
                                    onClick = {
                                        showPage = PageState.INPUT_PAGE
                                    }
                                )
                        ) {
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(
                                text = "   输入文字",
                                fontSize = 33.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        SwitchLanguage()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 24.dp)
                                .padding(top = 10.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(start = 40.dp)
                            ) {
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                ) {
                                    Icon(
                                        Icons.Outlined.Group,
                                        "dialogue",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
                                Text(
                                    text = "对话",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .size(90.dp)
                                    .offset(y = (-10).dp)
                                    .clip(shape = CircleShape)
                                    .background(color = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    Icons.TwoTone.Mic,
                                    "MicPhone",
                                    modifier = Modifier.size(35.dp),
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = 40.dp)
                            ) {
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                ) {
                                    Icon(
                                        Icons.Outlined.CameraAlt,
                                        "camera",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
                                Text(
                                    text = "相机",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }

                PageState.INPUT_PAGE -> {
                    val focusRequester = remember { FocusRequester() }

                    // 添加 BackHandler 监听系统返回按钮事件
                    BackHandler {
                        // 返回主界面
                        showPage = PageState.MAIN_PAGE
                    }
                    TranslateInputNavigationBar {
                        showPage = PageState.MAIN_PAGE
                    }
                    Box(
                        modifier = modifier
                            .weight(1f)
                    ) {
                        Surface(
                            modifier = modifier,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .navigationBarsPadding()
                                    .imePadding()
                            ) {
                                TextInput(
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(focusRequester),
                                    onEnterPressed = {
                                        showPage = PageState.DISPLAY_PAGE
                                    }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                SwitchLanguage(150.dp, 38.dp, RoundedCornerShape(16.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }

                PageState.DISPLAY_PAGE -> {
                    // 添加 BackHandler 监听系统返回按钮事件
                    BackHandler {
                        // 返回主界面
                        showPage = PageState.MAIN_PAGE
                    }
                    Scaffold(
                        bottomBar = {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                ExtendedFloatingActionButton(
                                    onClick = { onNewTranslationClicked() },
                                    icon = { Icon(Icons.Filled.Add, "新翻译") },
                                    text = { Text(text = "新翻译") },
                                    modifier = Modifier
                                        .size(160.dp)
                                        .padding(16.dp)
                                        .padding(vertical = 35.dp)
                                        .align(Alignment.BottomEnd)
                                )
                            }
                        }
                    ) {
                        Surface(
                            modifier = modifier,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                TranslateResultDisplayNavigationBar {
                                    showPage = PageState.MAIN_PAGE
                                }
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
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Icon(
                                                painter = painterResource(id = R.drawable.report),
                                                contentDescription = "report",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.weight(1f))

                                            Icon(
                                                painter = painterResource(id = R.drawable.paste),
                                                contentDescription = "copy",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
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
                                        Text(
                                            text = "Xuan ku",
                                            fontSize = 13.sp,
                                        )
                                        Spacer(modifier = Modifier.size(20.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Icon(
                                                painter = painterResource(id = R.drawable.report),
                                                contentDescription = "report",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.weight(1f))

                                            Icon(
                                                painter = painterResource(id = R.drawable.paste),
                                                contentDescription = "copy",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.weight(0.2f))

                                            Icon(
                                                painter = painterResource(id = R.drawable.round_trip_arrow),
                                                contentDescription = "round_trip",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
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
}

@Composable
fun SwitchLanguage(
    buttonWidth: Dp = 150.dp,
    buttonHeight: Dp = 54.dp,
    shape: CornerBasedShape = RoundedCornerShape(16.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /*TODO*/ },
            shape = shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .size(width = buttonWidth, height = buttonHeight)
        ) {
            Text(
                text = "中文（简体）",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        Icon(
            Icons.Filled.ImportExport,
            "swap language",
            modifier = Modifier
                .graphicsLayer { this.rotationZ = 90f }
                .padding(horizontal = 10.dp)
                .size(30.dp)

        )
        Button(
            onClick = { /*TODO*/ },
            shape = shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .size(width = buttonWidth, height = buttonHeight)
        ) {
            Text(
                text = "英文",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateInputNavigationBar(onIconClick: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            CustomIcon(onIconClick)


        }, actions = {
            Icon(
                painter = painterResource(id = R.drawable.round_trip_arrow),
                contentDescription = "round_trip",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(20.dp)
                    .clip(CircleShape)
            )
            Icon(
                painter = painterResource(id = R.drawable.historyicon),
                contentDescription = "history Icon",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(20.dp)
                    .clip(CircleShape)
            )
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "feedback",
            )
        }
    )
}

@Composable
fun CustomIcon(onIconClick: () -> Unit) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back",
        modifier = Modifier
            .padding(start = 10.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 将涟漪效果设置为 null
            ) {
                onIconClick()
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageNavigationBar(onNavigateToDataPage: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic,fontWeight = FontWeight.SemiBold)) {
                            append("EchoLingua ")
                        }
                        withStyle(style = SpanStyle(fontSize = 20.sp)){
                            append("翻译")
                        }
                    },
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onNavigateToDataPage()
                Log.d(TAG, "MainPageNavigationBar: 你好")
            }) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null
                )
            }
        },
        actions = {
            Image(
                painter = painterResource(R.drawable.aniya),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
                    .clip(CircleShape)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateResultDisplayNavigationBar(onIconClick: () -> Unit) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            CustomIcon(onIconClick)
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.historyicon),
                contentDescription = "history Icon",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(20.dp)
                    .clip(CircleShape)
            )
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 20.dp)
            )
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "feedback",
            )
        }

    )
}


@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    EchoLinguaTheme {
        MainTranslatePage()
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayPagePreview() {
    EchoLinguaTheme {
        MainTranslatePage(
            currentPage = PageState.DISPLAY_PAGE
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputPagePreview() {
    EchoLinguaTheme {
        MainTranslatePage(
            currentPage = PageState.INPUT_PAGE
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DarkModeInterfaceMainPreview() {
    EchoLinguaTheme {
        MainTranslatePage()
    }
}
