package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.echolingua.App
import com.example.echolingua.util.Translator
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import java.util.Locale

private const val TAG = "TranslatePageViewModel"

class TranslatePageViewModel : ViewModel() {
    private val mTranslatedTextFlow = MutableStateFlow("")
    val translatedTextFlow = mTranslatedTextFlow.asStateFlow()

    fun translate(
        text: String = ""
    ) {
        Translator.translateWithAutoDetect(text) { translatedText ->
            mTranslatedTextFlow.update { translatedText }
        }
    }

}