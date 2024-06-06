package com.example.echolingua

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.echolingua.ui.page.quickTranslatePages.QuickTranslatePage
import com.example.echolingua.ui.theme.EchoLinguaTheme

private const val TAG = "QuickTranslateActivity"

class QuickTranslateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val text = with(intent) {
                getStringExtra(Intent.EXTRA_PROCESS_TEXT) ?: getStringExtra(Intent.ACTION_SEND)
            }
            EchoLinguaTheme {
                QuickTranslatePage(
                    initialText = text ?: ""
                )
            }
        }

    }
}