package com.example.echolingua.util

import android.util.Log
import android.widget.Toast
import com.example.echolingua.App
import com.example.echolingua.ui.page.LanguageSelectStateHolder
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.update

private const val TAG = "Translator"

object Translator {

    fun translateWithAutoDetect(
        text: String,
        onSuccessCallback: (String) -> Unit = {},
    ) {
        if (LanguageSelectStateHolder.sourceLanguage.value == "detect") {
            LanguageIdentification.getClient().identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    translate(text, onSuccessCallback, languageCode)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Identify Language: ", exception)
                }
        } else {
            translate(text, onSuccessCallback)
        }
    }

    private fun translate(
        text: String,
        onSuccessCallback: (String) -> Unit = {},
        languageCode: String = LanguageSelectStateHolder.sourceLanguage.value
    ) {
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
                        Log.d(TAG, "translate: $translatedText")
                        onSuccessCallback(translatedText)
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