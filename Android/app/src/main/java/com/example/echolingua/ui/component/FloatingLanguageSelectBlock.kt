package com.example.echolingua.ui.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun FloatingLanguageSelectBlock(
    modifier: Modifier = Modifier,
    isExpandedState: Boolean = false,
    showOriginalText: Boolean = false,
    onSwitchClick: () -> Unit = {},
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    onSourceLanguageClick: () -> Unit = {},
    onTargetLanguageClick: () -> Unit = {},
    onSwapIconClick: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(isExpandedState) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = "",
        animationSpec = tween(400)
    )
    var width by remember { mutableIntStateOf(0) }
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(23.dp)),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .onGloballyPositioned {
                    width = it.size.width
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {
                        onSourceLanguageClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sourceLanguage,
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(horizontal = 5.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = Icons.Default.ImportExport, contentDescription = "Swap Language",
                modifier = Modifier
                    .graphicsLayer(rotationZ = 90f)
                    .clickable {
                        onSwapIconClick()
                    }
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {
                        onTargetLanguageClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = targetLanguage,
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(horizontal = 5.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            val interactionSource = remember { MutableInteractionSource() }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand menu",
                modifier = Modifier
                    .scale(1.2f)
                    .padding(start = 5.dp, end = 10.dp)
                    .graphicsLayer(rotationZ = arrowRotation)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isExpanded = !isExpanded
                    }
            )
        }
        AnimatedVisibility(
            visible = isExpanded
        ) {
            Row(
                modifier = Modifier.width(width.pixelToDp()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show original text",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = showOriginalText,
                    onCheckedChange = { onSwitchClick() },
                    modifier = Modifier.padding(horizontal = 10.dp),
                    colors = SwitchDefaults.colors().copy(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun DarkFloatingLanguageSelectBlockPreview() {
    EchoLinguaTheme {
        FloatingLanguageSelectBlock()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun LightFloatingLanguageSelectBlockPreview() {
    EchoLinguaTheme {
        FloatingLanguageSelectBlock(isExpandedState = true)
    }
}