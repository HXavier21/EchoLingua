package com.example.echoling

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.echolingua.ui.page.QuickTranslatePage
import com.example.echolingua.ui.page.TranslatePageViewModel
import com.example.echolingua.ui.theme.EchoLinguaTheme

class QuickTranslateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action == Intent.ACTION_PROCESS_TEXT || intent?.action == Intent.ACTION_SEND) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val text = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
                if (text != null) {

                }
            }
        }

        enableEdgeToEdge()
        setContent {
            EchoLinguaTheme {
                QuickTranslatePage()
            }
        }

    }
}