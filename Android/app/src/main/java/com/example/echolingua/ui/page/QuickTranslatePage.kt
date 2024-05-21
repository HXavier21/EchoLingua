package com.example.echolingua.ui.page

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.R
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun QuickTranslatePage(state: Number, onClearClick:()->Unit={},onNewTranslateClick:()->Unit={}) {
    Column {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onSurface)
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.translate),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(25.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "EchoLingua翻译",
                    textAlign = TextAlign.Center,
                    color =  MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint =  MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .size(20.dp)
                )
            }
            if (state == 1) {
                Row {
                    Text(
                        text = "中文(简体)   - 检测到的语言",
                        color =  MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(top = 20.dp, start = 20.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.paste),
                        contentDescription = null,
                        tint =  MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(end = 20.dp, top = 20.dp)
                            .size(25.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.report),
                        contentDescription = null,
                        tint =  MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(end = 20.dp, top = 20.dp)
                            .size(25.dp)
                    )
                }
                Text(
                    text = "身体",
                    color =  MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 20.dp)
                )
                Divider(
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 100.dp, top = 20.dp, end = 100.dp)
                )
                Row {
                    Text(
                        text = "英语",
                        color = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .padding(top = 20.dp, start = 20.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.paste),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .padding(end = 20.dp, top = 20.dp)
                            .size(25.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.report),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .padding(end = 20.dp, top = 20.dp)
                            .size(25.dp)
                    )
                }
                Text(
                    text = "human body",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 20.dp, bottom = 20.dp)
                )
            }else{
                Text(
                    text = "检测语言",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp)
                )
                var text by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp),
                    label = {
                        Text(
                            "输入文字",
                            color =  MaterialTheme.colorScheme.onSurface,
                        )
                    }
                )
                ElevatedButton(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                )
                {
                    Text(
                        "粘贴"
                    )
                }
            }
        }
        if (state == 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onSurface)
            ) {
                ElevatedButton(
                    onClick = {
                      onClearClick()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
                {
                    Text(
                        "清除"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ElevatedButton(
                    onClick = {
                        onNewTranslateClick()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                {
                    Text(
                        "新翻译",
                        color =  MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}



@Composable
fun StateChoose() {
    var state by remember { mutableStateOf(1) }
    QuickTranslatePage(state = state, onClearClick = {state=0}, onNewTranslateClick = {state=0})
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun SecondTitlePreview() {
    EchoLinguaTheme {
        Column {
            StateChoose()
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun DarkSecondTitlePreview() {
    EchoLinguaTheme {
        Column {
            StateChoose()
        }

    }
}


