package com.example.echolingua.ui.page

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
fun QuickTranslatePage() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.onSurface)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp)
        ) {
            LeftIcon()
            Spacer(modifier = Modifier.weight(1f))
            Title()
            Spacer(modifier = Modifier.weight(1f))
            RightIcon()
        }

        Row {
            Tooltip()
            Spacer(modifier = Modifier.weight(1f))
            PasteIcon()
            ReportIcon()
        }
        UndoWord()
        Divider(
            color = Color(0xFF424758),
            modifier = Modifier.padding(start = 100.dp, top = 20.dp, end = 100.dp)
        )
        Row {
            Language()
            Spacer(modifier = Modifier.weight(1f))
            PasteIconCopy()
            ReportIconCopy()
        }
        DoneLanguage()
    }
}

@Composable
fun SecondElevatedCard() {
    SecondElevatedCard()
}

@Composable
fun Title() {
    Column {
        Text(
            text = "EchoLingua翻译",
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Composable
fun Tooltip() {
    Text(
        text = "中文(简体)   - 检测到的语言",
        color = Color.White,
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp)
    )
}

@Composable
fun Language() {
    Text(
        text = "英语",
        color = Color(0xFFb6c5ff),
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp)
    )
}

@Composable
fun UndoWord() {
    Text(
        text = "身体",
        color = Color.White,
        modifier = Modifier
            .padding(top = 10.dp, start = 20.dp)
    )
}

@Composable
fun DoneLanguage() {
    Text(
        text = "human body",
        color = Color(0xFFb6c5ff),
        modifier = Modifier
            .padding(top = 10.dp, start = 20.dp, bottom = 20.dp)
    )
}

@Composable
fun ClearButton() {
    ElevatedButton(
        onClick = { },
        colors = ButtonDefaults.buttonColors(Color(0xFF414758)),
        modifier = Modifier
            .padding(start = 10.dp)
    )
    {
        Text(
            "清除"
        )
    }
}

@Composable
fun NewTranslateButton() {
    ElevatedButton(
        onClick = { },
        colors = ButtonDefaults.buttonColors(Color(0xFFb6c5ff)),
        modifier = Modifier
            .padding(end = 10.dp)
    )
    {
        Text(
            "新翻译",
            color = Color.Black
        )
    }
}

@Composable
fun LeftIcon() {
    Icon(
        painter = painterResource(id = R.drawable.translate),
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .padding(start = 10.dp)
            .size(25.dp)
    )
}

@Composable
fun RightIcon() {
    Icon(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .size(20.dp)

    )

}

@Composable
fun PasteIcon() {
    Icon(
        painter = painterResource(id = R.drawable.paste),
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .padding(end = 20.dp, top = 20.dp)
            .size(25.dp)
    )

}

@Composable
fun PasteIconCopy() {
    Icon(
        painter = painterResource(id = R.drawable.paste),
        contentDescription = null,
        tint = Color(0xFFb6c5ff),
        modifier = Modifier
            .padding(end = 20.dp, top = 20.dp)
            .size(25.dp)
    )

}

@Composable
fun ReportIcon() {
    Icon(
        painter = painterResource(id = R.drawable.report),
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .padding(end = 20.dp, top = 20.dp)
            .size(25.dp)
    )

}

@Composable
fun ReportIconCopy() {
    Icon(
        painter = painterResource(id = R.drawable.report),
        contentDescription = null,
        tint = Color(0xFFb6c5ff),
        modifier = Modifier
            .padding(end = 20.dp, top = 20.dp)
            .size(25.dp)
    )

}


@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    EchoLinguaTheme {
        Column {
            QuickTranslatePage()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ClearButton()
                    Spacer(modifier = Modifier.weight(1f))
                    NewTranslateButton()
                }

        }

    }
}
@Composable
fun ElevatedCard() {
    androidx.compose.material3.ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row ( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp)
        ){
            LeftIcon()
            Spacer(modifier = Modifier.weight(1f))
            Title()
            Spacer(modifier = Modifier.weight(1f))
            RightIcon()
        }
        Tooltip()
        InputBox()
        PasteButton()
    }
}
@Composable
fun InputBox() {
    var text by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = text,
        onValueChange = {text=it},
        modifier = Modifier
            .padding(start = 20.dp, top = 10.dp),
        label = {
            Text(
                "输入文字",
                color= Color.White,
            )
        }
    )
}

@Composable
fun Title(
    modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "EchoLingua翻译",
            color= Color.White,
            modifier = modifier
        )
    }
}
@Composable
fun Tooltip( modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "检测语言",
            color= Color.White,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
        )
    }
}

@Composable
fun PasteButton() {
    ElevatedButton(
        onClick = {  },
        colors = ButtonDefaults.buttonColors(Color(0xFF414758)),
        modifier = Modifier
            .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
    )
    {
        Text(
            "粘贴"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SecondTitlePreview() {
    EchoLinguaTheme {
        Column {
            ElevatedCard()
        }

    }
}


