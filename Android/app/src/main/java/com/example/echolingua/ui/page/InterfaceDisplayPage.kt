package com.example.echolingua.ui.page

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.R
import com.example.echolingua.ui.theme.EchoLinguaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InterfaceDisplayPage(
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                ExtendedFloatingActionButton(
                    onClick = { /*TODO*/ },
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
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackIcon()
                    Spacer(modifier = Modifier.weight(1f))
                    HistoryIcon()
                    Spacer(modifier = Modifier.weight(0.1f))
                    KeyTranslationsIcon()
                    Spacer(modifier = Modifier.weight(0.1f))
                    FeedbackIcon()
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
                            Report()
                            Spacer(modifier = Modifier.weight(1f))
                            CopyText()
                        }
                    }
                }
                Divider(
                    color = Color(0xFF424758),
                    modifier = Modifier.padding(start = 100.dp, end = 100.dp)
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
                            Report()
                            Spacer(modifier = Modifier.weight(1f))
                            CopyText()
                            Spacer(modifier = Modifier.weight(0.2f))
                            SelectText()
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun SelectText() {
    val colorFilter = if (isSystemInDarkTheme()) {
        ColorFilter.tint(Color.White) // Set color for dark theme
    } else {
        ColorFilter.tint(Color.Black) // Set color for light theme
    }
    Image(
        painterResource(
            id = R.drawable.round_trip_arrow
        ),
        contentDescription = "round_trip",
        colorFilter = colorFilter,
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
    )
}

@Composable
fun CopyText() {
    val colorFilter = if (isSystemInDarkTheme()) {
        ColorFilter.tint(Color.White) // Set color for dark theme
    } else {
        ColorFilter.tint(Color.Black) // Set color for light theme
    }
    Image(
        painterResource(
            id = R.drawable.paste
        ),
        contentDescription = "copy",
        colorFilter = colorFilter,
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
    )
}

@Composable
fun Report() {
    val colorFilter = if (isSystemInDarkTheme()) {
        ColorFilter.tint(Color.White) // Set color for dark theme
    } else {
        ColorFilter.tint(Color.Black) // Set color for light theme
    }
    Image(
        painterResource(id = R.drawable.report),
        contentDescription = "report",
        colorFilter = colorFilter,
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EchoLinguaTheme {
        InterfaceDisplayPage()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DarkModeGreetingPreview() {
    EchoLinguaTheme {
        InterfaceDisplayPage()
    }
}