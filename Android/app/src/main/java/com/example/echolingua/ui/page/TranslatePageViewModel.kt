package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.echolingua.App
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

    fun translate(text: String) {
        if (LanguageSelectStateHolder.sourceLanguage.value == "detect") {
            LanguageIdentification.getClient().identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    Log.d(TAG, "translate: $languageCode")
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(languageCode)
                        .setTargetLanguage(LanguageSelectStateHolder.targetLanguage.value)
                        .build()
                    val translator = Translation.getClient(options)
                    val conditions = DownloadConditions.Builder()
                        .requireWifi()
                        .build()
                    translator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            translator.translate(text)
                                .addOnSuccessListener { translatedText ->
                                    mTranslatedTextFlow.update { translatedText }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        App.context,
                                        "Translation failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e(TAG, "Translate: $exception")
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                App.context,
                                "Model download failed",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e(TAG, "Download Model: ", exception)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Identify Language: ", exception)
                }
        } else {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(LanguageSelectStateHolder.sourceLanguage.value)
                .setTargetLanguage(LanguageSelectStateHolder.targetLanguage.value)
                .build()
            val translator = Translation.getClient(options)
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnSuccessListener { translatedText ->
                            mTranslatedTextFlow.update { translatedText }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                App.context,
                                "Translation failed",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(TAG, "Translate: $exception")
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        App.context,
                        "Model download failed",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(TAG, "Download Model: ", exception)
                }
        }
    }

}