package com.example.echolingua.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun MainTranslatePageBottomBar(
    modifier: Modifier = Modifier,
    onNavigateToChatPage: () -> Unit = {},
    onNavigateToCameraPage: () -> Unit = {},
    onRecordStart: () -> Unit = {},
    onRecordEnd: () -> Unit = {}
) {
    var iconWidth by remember {
        mutableIntStateOf(30)
    }
    var textWidth1 by remember {
        mutableIntStateOf(50)
    }
    var textWidth2 by remember {
        mutableIntStateOf(50)
    }
    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter
    ) {
        Row(modifier = Modifier.padding(horizontal = 25.dp)) {
            Text(text = "Conversation",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .onGloballyPositioned {
                        textWidth1 = it.size.width
                    }
                    .offset(x = ((iconWidth - textWidth1) / 2).pixelToDp()))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Camera",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .onGloballyPositioned {
                        textWidth2 = it.size.width
                    }
                    .offset(x = ((textWidth2 - iconWidth) / 2).pixelToDp()))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { onNavigateToChatPage() },
                        modifier = Modifier
                            .scale(1.3f)
                            .onGloballyPositioned {
                                iconWidth = it.size.width
                            },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Outlined.Group, "dialogue", modifier = Modifier.padding(10.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { onNavigateToCameraPage() },
                        modifier = Modifier.scale(1.3f),
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Outlined.CameraAlt, "camera", modifier = Modifier.padding(10.dp)
                        )
                    }

                }
            }
            MicButton(
                modifier = Modifier.scale(1.3f),
                onRecordStart = onRecordStart,
                onRecordEnd = onRecordEnd
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainTranslatePageBottomBarPreview() {
    EchoLinguaTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
            MainTranslatePageBottomBar()
        }
    }
}