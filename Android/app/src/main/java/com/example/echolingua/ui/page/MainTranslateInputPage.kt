package com.example.echolingua.ui.page

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.R
import com.example.echolingua.ui.component.TextInput
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun MainTranslateInputPage(
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
                Spacer(modifier = Modifier.weight(0.1f))
                Image(
                    painter = painterResource(id = R.drawable.historyicon),
                    contentDescription = "history Icon",
                    colorFilter = colorFilter,
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
            TextInput(
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(10.dp))
            SwitchLanguage()
        }
    }

}


@Preview(showBackground = true)
@Composable
fun InterfaceInputPreview() {
    EchoLinguaTheme {
        MainTranslateInputPage()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DarkModeInterfaceInputPreview() {
    EchoLinguaTheme {
        MainTranslateInputPage()
    }
}