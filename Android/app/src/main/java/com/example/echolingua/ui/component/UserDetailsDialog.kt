package com.example.echolingua.ui.component

import android.content.res.Configuration
import android.view.Gravity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.example.echolingua.R
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun UserDetailsDialog(
    onDismissRequest: () -> Unit = {},
    userName: String = "Aniya",
    userEmail: String = "wakuwaku@spy.com",
    onHistoryClick: () -> Unit = {},
    onDownloadedModelsClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    var accountOptionVisibility by remember {
        mutableStateOf(false)
    }
    val arrowRotation by animateFloatAsState(
        targetValue = if (accountOptionVisibility) 180f else 0f,
        label = "",
        animationSpec = tween(400)
    )
    val interactionSource = remember { MutableInteractionSource() }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.TOP)
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(
                    interactionSource = interactionSource, indication = null
                ) {
                    onDismissRequest()
                }) {}
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ), shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold
                                )
                            ) { append("Echo") }
                            withStyle(
                                SpanStyle(
                                    fontStyle = FontStyle.Italic
                                )
                            ) { append("Lingua") }
                        }, style = MaterialTheme.typography.titleLarge)
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.aniya),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(CircleShape)
                                .size(40.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = userName, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = userEmail, style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = {
                            accountOptionVisibility = !accountOptionVisibility
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expand",
                                modifier = Modifier
                                    .border(
                                        1.dp, MaterialTheme.colorScheme.onSurface, CircleShape
                                    )
                                    .graphicsLayer(rotationZ = arrowRotation)
                            )
                        }
                    }
                    AnimatedVisibility(visible = accountOptionVisibility) {
                        Column {
                            HorizontalDivider()
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.NoAccounts,
                                    contentDescription = "Use without an account",
                                    modifier = Modifier
                                )
                                Text(
                                    text = "Use without an account",
                                    modifier = Modifier.padding(start = 20.dp)
                                )
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = "Add account",
                                    modifier = Modifier
                                )
                                Text(
                                    text = "Add account", modifier = Modifier.padding(start = 20.dp)
                                )
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.ManageAccounts,
                                    contentDescription = "Manage accounts",
                                    modifier = Modifier
                                )
                                Text(
                                    text = "Manage accounts",
                                    modifier = Modifier.padding(start = 20.dp)
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.RecordVoiceOver,
                            contentDescription = "Stored recordings",
                            modifier = Modifier
                        )
                        Text(
                            text = "Audio transcripts stored",
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHistoryClick() }
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.History, contentDescription = "History"
                        )
                        Text(
                            text = "History", modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDownloadedModelsClick() }
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.DownloadDone,
                            contentDescription = "Downloaded models"
                        )
                        Text(
                            text = "Downloaded models", modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDismissRequest()
                        onSettingClick()
                    }
                    .padding(start = 20.dp)
                    .padding(vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Settings, contentDescription = "Settings"
                    )
                    Text(
                        text = "Settings", modifier = Modifier.padding(start = 20.dp)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(start = 20.dp)
                    .padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                        contentDescription = "Help and feedback"
                    )
                    Text(
                        text = "Help and feedback", modifier = Modifier.padding(start = 20.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Privacy Policy",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                            .clickable { }
                            .padding(10.dp))
                    Text(text = "•", style = MaterialTheme.typography.bodySmall)
                    Text("Terms of Service",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                            .clickable { }
                            .padding(10.dp)

                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
fun UserDetailsDialogPreview() {
    EchoLinguaTheme {
        UserDetailsDialog()
    }
}