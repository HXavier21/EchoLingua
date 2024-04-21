package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.echolingua.App
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

private const val TAG = "TranslatePageViewModel"

class TranslatePageViewModel : ViewModel() {
    private val mTextFlow = MutableStateFlow("")
    val textFlow = mTextFlow.asStateFlow()

    private val mSourceLanguageFlow = MutableStateFlow("")
    val sourceLanguageFlow = mSourceLanguageFlow.asStateFlow()

    private val mTargetLanguageFlow = MutableStateFlow("")
    val targetLanguageFlow = mTargetLanguageFlow.asStateFlow()

    private val mTranslatedTextFlow = MutableStateFlow("")
    val translatedTextFlow = mTranslatedTextFlow.asStateFlow()

    fun setText(text: String) {
        mTextFlow.update { text }
    }

    fun getLanguageCodeNameMap(): Map<String,String> {
        return TranslateLanguage.getAllLanguages()
            .associateWith { Locale.forLanguageTag(it).displayName }
    }

    /**
     * Sets the source language for translation.
     * @param language defined in [TranslateLanguage].
     */
    fun setSourceLanguage(language: String) {
        mSourceLanguageFlow.update { language }
    }

    /**
     * Sets the target language for translation.
     * @param language defined in [TranslateLanguage].
     */
    fun setTargetLanguage(language: String) {
        mTargetLanguageFlow.update { language }
    }

    fun translate() {
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
                translator.translate(mTextFlow.value)
                    .addOnSuccessListener { translatedText ->
                        mTranslatedTextFlow.update { translatedText }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            App.context,
                            "Translation failed: $exception",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "translate: $exception")
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    App.context,
                    "Model download failed",
                    Toast.LENGTH_LONG
                ).show()
                Log.e(TAG, "translate: ", exception)
            }
    }
}