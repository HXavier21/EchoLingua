package com.example.echolingua.ui.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.echolingua.QuickTranslateActivity
import com.example.echolingua.R
import com.example.echolingua.ui.component.ContentAfterTranslate
import com.example.echolingua.ui.component.ContentBeforeTranslate
import com.example.echolingua.ui.component.QuickTranslatePageTopBar
import com.example.echolingua.ui.theme.EchoLinguaTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuickTranslatePage(
    state: Boolean, onClearClick: () -> Unit = {}, onNewTranslateClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    Dialog(
        onDismissRequest = { onDismissRequest() }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .animateContentSize()
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth()
            ) {
                QuickTranslatePageTopBar()
                AnimatedContent(targetState = state, label = "AnimatedContent") {
                    if (it) {
                        ContentAfterTranslate()
                    } else {
                        ContentBeforeTranslate(
                            onNewTranslateClick = onNewTranslateClick
                        )
                    }
                }
            }
            if (state) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    ElevatedButton(
                        onClick = {
                            onClearClick()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    {
                        Text(
                            "清除",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        onClick = {
                            onClearClick()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceTint),
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )
                    {
                        Text(
                            "新翻译",
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun StateChoose() {
    val context = LocalContext.current
    var state by remember { mutableStateOf(true) }
    QuickTranslatePage(state = state,
        onClearClick = { state = false },
        onNewTranslateClick = { state = true },
        onDismissRequest = {
            (context as QuickTranslateActivity).finish()
        })
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun SecondTitlePreview() {
    EchoLinguaTheme {
        StateChoose()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun DarkSecondTitlePreview() {
    EchoLinguaTheme {
        StateChoose()
    }
}


