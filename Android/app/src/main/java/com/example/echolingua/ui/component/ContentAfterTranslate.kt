package com.example.echolingua.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.R

@Preview
@Composable
fun ContentAfterTranslate() {
    Column {
        val clipboardManger = LocalClipboardManager.current
        val onCloneClick: () -> Unit = {
            val textToCopy = "hello"
            clipboardManger.setText(AnnotatedString(textToCopy))
        }
        Row(
            modifier = Modifier
        ) {
            Text(
                text = "中文(简体)   - 检测到的语言",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.paste),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = 20.dp, top = 30.dp)
                    .size(25.dp)
                    .clickable {
                        onCloneClick()
                    }
            )

            Icon(
                painter = painterResource(id = R.drawable.report),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = 20.dp, top = 30.dp)
                    .size(25.dp)
            )
        }
        Text(
            text = "身体",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(
                start = 100.dp,
                top = 20.dp,
                end = 100.dp
            ),
            color = Color.DarkGray
        )
        Row(
            modifier = Modifier
        ) {
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
                    .clickable {
                        onCloneClick()
                    }
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
                .padding(top = 20.dp, start = 20.dp, bottom = 20.dp)
        )

    }
}