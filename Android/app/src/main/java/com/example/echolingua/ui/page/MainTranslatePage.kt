package com.example.echolingua.ui.page

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.R
import com.example.echolingua.ui.component.TextInput
import com.example.echolingua.ui.theme.EchoLinguaTheme


// 定义页面状态枚举
enum class PageState {
    MAIN_PAGE,
    TRANSLATE_PAGE,
    RESULT_PAGE
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainTranslatePage(modifier: Modifier = Modifier) {
    var showPage by remember { mutableStateOf(PageState.MAIN_PAGE) }

    fun onNewTranslationClicked() {
        showPage = PageState.TRANSLATE_PAGE
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (showPage) {
                PageState.MAIN_PAGE  -> {
                    MainPageNavigationBar()
                    Box(
                        modifier = modifier

                            .weight(1f)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        )
                        Column(
                            modifier= Modifier
                                .clip(shape = MaterialTheme.shapes.extraLarge)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() }, // 设置interactionSource
                                    indication = null, // 设置为 null 禁用涟漪效果
                                    onClick = {
                                        showPage = PageState.TRANSLATE_PAGE
                                    }
                                )
                        ){
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(
                                text = "输入文字",
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SwitchLanguage()
                        Spacer(modifier = Modifier.size(24.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(start = 40.dp)
                            ) {
                                LargeFloatingActionButton(
                                    onClick = { /*TODO*/ },
                                    shape = CircleShape,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .size(50.dp)
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
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            LargeFloatingActionButton(
                                onClick = { /*TODO*/ },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(100.dp)
                                    .offset(y = (-10).dp),
                            ) {
                                Icon(
                                    Icons.TwoTone.Mic,
                                    "MicPhone",
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = 40.dp)
                            ) {
                                LargeFloatingActionButton(
                                    onClick = { /*TODO*/ },
                                    shape = CircleShape,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .size(50.dp)
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
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
                PageState.TRANSLATE_PAGE-> {
                    val focusRequester = remember { FocusRequester() }

                    // 添加 BackHandler 监听系统返回按钮事件
                    BackHandler {
                        // 返回主界面
                        showPage = PageState.MAIN_PAGE
                    }
                    TranslateInputNavigationBar {
                        showPage =  PageState.MAIN_PAGE
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
                                    .statusBarsPadding()
                                    .navigationBarsPadding()
                                    .imePadding()
                            ) {
                                TextInput(
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(focusRequester),
                                    onEnterPressed = {
                                        showPage=  PageState.RESULT_PAGE
                                    }
                                )
                                Spacer(modifier = Modifier.size(10.dp))
                                SwitchLanguage()
                            }
                        }
                    }
                }
                PageState.RESULT_PAGE -> {
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
                                    showPage =  PageState.MAIN_PAGE
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
                                                painter =  painterResource(id = R.drawable.report),
                                                contentDescription = "report",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.weight(1f))

                                            Icon(
                                                painter =  painterResource(id = R.drawable.paste),
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
                                                painter =  painterResource(id = R.drawable.report),
                                                contentDescription = "report",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.weight(1f))

                                            Icon(
                                                painter =  painterResource(id = R.drawable.paste),
                                                contentDescription = "copy",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.weight(0.2f))

                                            Icon(
                                                painter =  painterResource(id = R.drawable.round_trip_arrow),
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
fun SwitchLanguage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /*TODO*/ },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "中文",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(horizontal = 25.dp)
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
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "英文",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(horizontal = 25.dp)
            )
        }
    }
}


@Composable
fun TranslateInputNavigationBar(onIconClick: () -> Unit){
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomIcon(onIconClick)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter =  painterResource(id = R.drawable.round_trip_arrow),
            contentDescription = "round_trip",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Icon(
            painter = painterResource(id = R.drawable.historyicon),
            contentDescription = "history Icon",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "feedback",
        )
    }
}
@Composable
fun CustomIcon(onIconClick: () -> Unit) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back",
        modifier = Modifier
            .padding(start = 10.dp)
            .clickable (
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 将涟漪效果设置为 null
            ){
                onIconClick()
            }
    )
}

@Composable
fun MainPageNavigationBar() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "EchoLingua翻译",
            modifier = Modifier
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.aniya),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(30.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun TranslateResultDisplayNavigationBar(onIconClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomIcon(onIconClick)
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.historyicon),
            contentDescription = "history Icon",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Icon(
            Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "feedback",
        )
    }
}


@Preview(showBackground = true)
@Composable
fun InterfaceMainPreview() {
    EchoLinguaTheme {
        MainTranslatePage()
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
