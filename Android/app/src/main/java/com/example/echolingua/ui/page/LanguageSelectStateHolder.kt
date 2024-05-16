package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.echolingua.App
import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.Locale

private const val TAG = "LanguageSelectStateHold"

object LanguageSelectStateHolder {
    private var mSourceLanguage = mutableStateOf("")
    val sourceLanguage = mSourceLanguage

    private var mTargetLanguage = mutableStateOf("")
    val targetLanguage = mTargetLanguage

    val languageMap =
        TranslateLanguage.getAllLanguages().associateWith { Locale.forLanguageTag(it).displayName }

    /**
     * Sets the language for translation.
     * @param language defined in [TranslateLanguage].
     * @param mode [SelectMode] to specify the language to set.
     */
    fun setLanguage(language: String, mode: SelectMode) {
        if ((language != "detect" && language !in TranslateLanguage.getAllLanguages()) || (language == "detect" && mode != SelectMode.SOURCE)) {
            Log.e(TAG, "setLanguage: Invalid language code")
            Toast.makeText(
                App.context, "Invalid language code", Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (mode == SelectMode.SOURCE) {
            if (language == mTargetLanguage.value) {
                swapLanguage()
                return
            }
            mSourceLanguage.value = language
        } else {
            if (language == mSourceLanguage.value) {
                swapLanguage()
                return
            }
            mTargetLanguage.value = language
        }
    }

    fun swapLanguage() {
        val temp = mSourceLanguage.value
        mSourceLanguage.value = mTargetLanguage.value
        mTargetLanguage.value = temp
    }

    fun getSourceLanguageDisplayName(): String {
        return when (mSourceLanguage.value) {
            "" -> {
                "Source"
            }
            "detect" -> {
                "Auto detect"
            }
            else -> {
                languageMap[mSourceLanguage.value] ?: ""
            }
        }
    }

    fun getTargetLanguageDisplayName(): String {
        return when (mTargetLanguage.value) {
            "" -> {
                "Target"
            }
            else -> {
                languageMap[mTargetLanguage.value] ?: ""
            }
        }
    }
}