package com.example.echolingua.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun CameraTranslateTopBar(
    isCaptured: Boolean = false,
    onBackClick: () -> Unit = {},
    onTorchClick: (Boolean) -> Unit = {},
    onCloseClick: () -> Unit = {}
) {
    var isTorchOn by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
        ) {
            val brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.8f),
                    Color.Black.copy(alpha = 0.5f),
                    Color.Transparent
                )
            )
            drawRect(
                brush = brush, size = this.size
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Icon(imageVector = if (isCaptured) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource, indication = null
                    ) {
                        if (isCaptured) {
                            onCloseClick()
                        } else {
                            onBackClick()
                        }
                    }
                    .padding(10.dp),
                tint = Color.White)
            if (!isCaptured) {
                Icon(imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Torch",
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            onTorchClick(!isTorchOn)
                            isTorchOn = !isTorchOn
                        }
                        .padding(10.dp),
                    tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(interactionSource = interactionSource, indication = null) { },
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White, fontWeight = FontWeight.Black
                        )
                    ) {
                        append("Echo")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.White
                        )
                    ) {
                        append("Lingua")
                    }
                },
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleLarge,
                fontStyle = FontStyle.Italic
            )
        }
    }
}