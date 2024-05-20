package com.example.echolingua.ui.page

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.twotone.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.R
import com.example.echolingua.ui.component.TextInput
import com.example.echolingua.ui.theme.EchoLinguaTheme


@Composable
fun KeyTranslationsIcon() {
    Icon(
        Icons.Filled.Star,
        contentDescription = null,
        modifier = Modifier
            .padding(start = 10.dp)
    )
}

@Composable
fun AccountImage() {
    Image(
        painter = painterResource(R.drawable.aniya),
        contentDescription = null,
        modifier = Modifier
            .padding(end = 10.dp)
            .size(30.dp)
            .clip(CircleShape)
    )
}

@Composable
fun MainTranslatePage(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KeyTranslationsIcon()
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "EchoLingua翻译",
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(1f))
                AccountImage()
            }
            TextInput(
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SwitchLanguage()
                Spacer(modifier = Modifier.size(24.dp))
                TranslationAssistance()
            }
        }
    }
}

@Composable
fun SwitchLanguage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SourceLanguageBotton()
        Icon(
            Icons.Filled.ImportExport,
            "swap language",
            modifier = Modifier
                .graphicsLayer { this.rotationZ = 90f }
                .padding(horizontal = 10.dp)

        )
        TargetLanguageButton()
    }
}

@Composable
fun TranslationAssistance() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        DialogueButton()
        Spacer(modifier = Modifier.weight(1f))
        MicButton()
        Spacer(modifier = Modifier.weight(1f))
        CameraButton()
    }
}

@Composable
fun SourceLanguageBotton() {
    Button(
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "中文",
            modifier = Modifier
                .padding(8.dp)
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun TargetLanguageButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "英文",
            modifier = Modifier
                .padding(8.dp)
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun MicButton() {
    LargeFloatingActionButton(
        onClick = { /*TODO*/ },
        shape = CircleShape,
        modifier = Modifier
            .size(80.dp)
            .offset(y = (-10).dp),
    ) {
        Icon(
            Icons.TwoTone.Mic,
            "MicPhone",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun DialogueButton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 40.dp)
    ) {
        LargeFloatingActionButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                Icons.Outlined.Group,
                "dialogue",
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Text(
            text = "对话",
            fontSize = 10.sp,
            modifier = Modifier
        )
    }
}

@Composable
fun CameraButton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 40.dp)
    ) {
        LargeFloatingActionButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                Icons.Outlined.CameraAlt,
                "camera",
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Text(
            text = "相机",
            fontSize = 10.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterfaceMainPreview() {
    EchoLinguaTheme {
        MainTranslatePage()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DarkModeInterfaceMainPreview() {
    EchoLinguaTheme {
        MainTranslatePage()
    }
}
