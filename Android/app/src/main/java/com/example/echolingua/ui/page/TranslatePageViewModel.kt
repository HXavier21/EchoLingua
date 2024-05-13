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
    private val mSourceLanguageFlow = MutableStateFlow("")
    val sourceLanguageFlow = mSourceLanguageFlow.asStateFlow()

    private val mTargetLanguageFlow = MutableStateFlow("")
    val targetLanguageFlow = mTargetLanguageFlow.asStateFlow()

    private val mTranslatedTextFlow = MutableStateFlow("")
    val translatedTextFlow = mTranslatedTextFlow.asStateFlow()

    fun getLanguageCodeNameMap(): Map<String, String> {
        return TranslateLanguage.getAllLanguages()
            .associateWith { Locale.forLanguageTag(it).displayName }
    }

    /**
     * Sets the source language for translation.
     * @param language defined in [TranslateLanguage].
     */
    fun setSourceLanguage(language: String) {
        if (language != "detect" && language !in TranslateLanguage.getAllLanguages()) {
            Log.e(TAG, "setSourceLanguage: Invalid language code")
            Toast.makeText(
                App.context,
                "Invalid language code",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        mSourceLanguageFlow.update { language }
    }

    /**
     * Sets the target language for translation.
     * @param language defined in [TranslateLanguage].
     */
    fun setTargetLanguage(language: String) {
        if (language !in TranslateLanguage.getAllLanguages()) {
            Log.e(TAG, "setTargetLanguage: Invalid language code")
            Toast.makeText(
                App.context,
                "Invalid language code",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        mTargetLanguageFlow.update { language }
    }

    fun translate(text: String) {
        if (mSourceLanguageFlow.value == "detect") {
            LanguageIdentification.getClient().identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    Log.d(TAG, "translate: $languageCode")
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(languageCode)
                        .setTargetLanguage(mTargetLanguageFlow.value)
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
                .setSourceLanguage(mSourceLanguageFlow.value)
                .setTargetLanguage(mTargetLanguageFlow.value)
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

    fun swapLanguage() {
        val temp = mSourceLanguageFlow.value
        mSourceLanguageFlow.update { mTargetLanguageFlow.value }
        mTargetLanguageFlow.update { temp }
    }
}