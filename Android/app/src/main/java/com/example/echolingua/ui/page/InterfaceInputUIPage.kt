package com.example.echolingua.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.echolingua.R

@Composable
fun Interface2(
    modifier: Modifier = Modifier
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
            NavigationBar2()
            TextInput(
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(10.dp))
            SwitchLanguage()
        }
    }

}


@Composable
fun NavigationBar2() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReturnIcon()
        Spacer(modifier = Modifier.weight(1f))
        LocationMoveIcon()
        Spacer(modifier = Modifier.weight(0.1f))
        HistoryIcon()
        Spacer(modifier = Modifier.weight(0.1f))
        com.example.echolingua.ui.page.FeedbackIcon()
    }
}


@Composable
fun ReturnIcon() {
    Icon(
        Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Return",
        modifier = Modifier
            .padding(start = 10.dp)
    )
}

@Composable
fun LocationMoveIcon() {
    val colorFilter = if (isSystemInDarkTheme()) {
        ColorFilter.tint(Color.White) // Set color for dark theme
    } else {
        ColorFilter.tint(Color.Black) // Set color for light theme
    }
    Image(
        painter = painterResource(id = R.drawable.round_trip_arrow),
        contentDescription = "history Icon",
        colorFilter = colorFilter,
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
    )
}

@Composable
fun HistoryIcon() {
    val colorFilter = if (isSystemInDarkTheme()) {
        ColorFilter.tint(Color.White) // Set color for dark theme
    } else {
        ColorFilter.tint(Color.Black) // Set color for light theme
    }
    Image(
        painter = painterResource(id = R.drawable.historyicon),
        contentDescription = "history Icon",
        colorFilter = colorFilter,
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
    )
}

@Composable
fun FeedbackIcon() {
    Icon(
        Icons.Filled.MoreVert,
        contentDescription = "feedback",
    )
}