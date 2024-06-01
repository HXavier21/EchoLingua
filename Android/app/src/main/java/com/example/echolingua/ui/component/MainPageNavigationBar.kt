package com.example.echolingua.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.echolingua.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageNavigationBar(onNavigateToDataPage: () -> Unit = {}) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Echo")
                        }
                        withStyle(style = SpanStyle()) {
                            append("Lingua")
                        }
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onNavigateToDataPage()
            }) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null
                )
            }
        },
        actions = {
            Image(
                painter = painterResource(R.drawable.aniya),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .border(0.5.dp, Color.Gray, CircleShape)
            )
        }
    )
}