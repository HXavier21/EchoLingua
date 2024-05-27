package com.example.echolingua.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioTranslateTopBar(
    onBackClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    TopAppBar(title = { }, navigationIcon = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .clickable {
                    onBackClick()
                }
                .padding(5.dp)
        )
    }, actions = {
        Box(modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onChatClick()
            }
            .padding(5.dp),
            contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Rounded.ChatBubble,
                contentDescription = "Chat",
                modifier = Modifier.graphicsLayer(
                    rotationZ = 180f,
                    scaleY = 0.4f,
                    scaleX = 0.7f,
                    translationY = -Icons.Rounded.ChatBubble.defaultHeight.dpToPixel() * 0.2f
                )
            )
            Icon(
                imageVector = Icons.Rounded.ChatBubbleOutline,
                contentDescription = "Chat",
                modifier = Modifier.graphicsLayer(
                    scaleY = 0.4f,
                    scaleX = 0.7f,
                    translationY = Icons.Rounded.ChatBubble.defaultHeight.dpToPixel() * 0.2f

                )
            )
        }
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings",
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .clickable {
                    onSettingClick()
                }
                .padding(5.dp)
        )
    })
}

@Composable
fun Dp.dpToPixel(): Float = with(LocalDensity.current) {
    toPx()
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun AudioTranslateTopBarPreview() {
    EchoLinguaTheme {
        AudioTranslateTopBar()
    }
}
