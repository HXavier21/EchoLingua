package com.example.echolingua.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.echolingua.ui.page.TranslateModelState
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun ModelManageDialog(
    translateModelState: TranslateModelState = TranslateModelState.NOT_DOWNLOADED,
    language: String,
    onDismissRequest: () -> Unit = {},
    onDownloadRequest: () -> Unit = {},
    onRemoveRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(shape = MaterialTheme.shapes.extraLarge) {
            when (translateModelState) {
                TranslateModelState.NOT_DOWNLOADED -> {
                    NotDownloadedModelManageDialog(language, onDismissRequest, onDownloadRequest)
                }

                TranslateModelState.DOWNLOADING -> {
                }

                TranslateModelState.DOWNLOADED -> {
                    DownloadedModelManageDialog(language, onDismissRequest, onRemoveRequest)
                }
            }
        }
    }
}

@Composable
fun NotDownloadedModelManageDialog(
    language: String,
    onDismissRequest: () -> Unit,
    onDownloadRequest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.FileDownload,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = "Download $language?",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "After downloading offline translation model, " +
                    "you can translate text without internet connection.",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Cancel")
                    }
                },
                modifier = Modifier.padding(20.dp)
            ) {
                onDismissRequest()
            }
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Download")
                    }
                },
                modifier = Modifier.padding(20.dp)
            ) {
                onDownloadRequest()
            }
        }
    }
}

@Composable
fun DownloadedModelManageDialog(
    language: String,
    onDismissRequest: () -> Unit,
    onRemoveRequest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = language,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(20.dp)
        )
        Text(
            text = "If you remove the offline translation model, " +
                    "you can no longer translate text without internet connection.",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Cancel")
                    }
                },
                modifier = Modifier.padding(20.dp)
            ) {
                onDismissRequest()
            }
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Remove")
                    }
                },
                modifier = Modifier.padding(20.dp)
            ) {
                onRemoveRequest()
            }
        }
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
fun ModelManageDialogPreview() {
    EchoLinguaTheme {
        ModelManageDialog(TranslateModelState.NOT_DOWNLOADED, "English")
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
fun DarkModelManageDialogPreview() {
    EchoLinguaTheme {
        ModelManageDialog(TranslateModelState.DOWNLOADED, "English")
    }
}