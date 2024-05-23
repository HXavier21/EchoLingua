package com.example.echolingua.ui.page

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.R
import com.example.echolingua.ui.theme.EchoLinguaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TranslateResultDisplayPage(
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
                TranslateResultDisplayNavigationBar()
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
@Composable
fun TranslateResultDisplayNavigationBar(){
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .padding(start = 10.dp)
        )
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
fun GreetingPreview() {
    EchoLinguaTheme {
        TranslateResultDisplayPage()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DarkModeGreetingPreview() {
    EchoLinguaTheme {
        TranslateResultDisplayPage()
    }
}