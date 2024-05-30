package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.echolingua.App
import com.google.mlkit.nl.translate.TranslateLanguage
import com.tencent.mmkv.MMKV
import java.util.Locale

private const val TAG = "LanguageSelectStateHold"

object LanguageSelectStateHolder {
    private var mSourceLanguage = mutableStateOf("")
    val sourceLanguage = mSourceLanguage

    private var mTargetLanguage = mutableStateOf("")
    val targetLanguage = mTargetLanguage

    var isSelecting = mutableStateOf(false)
    var selectMode = mutableStateOf(SelectMode.SOURCE)

    val languageMap =
        TranslateLanguage.getAllLanguages().associateWith { Locale.forLanguageTag(it).displayName }

    init {
        val mmkv = MMKV.defaultMMKV()
        mSourceLanguage.value = mmkv.decodeString("sourceLanguage", "").toString()
        mTargetLanguage.value = mmkv.decodeString("targetLanguage", "").toString()
    }

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

    fun getSourceLanguageDisplayName(
        sourceLanguage: String = mSourceLanguage.value
    ): String {
        return when (sourceLanguage) {
            "" -> {
                "Source"
            }

            "detect" -> {
                "Auto detect"
            }

            else -> {
                languageMap[sourceLanguage] ?: ""
            }
        }
    }

    fun getTargetLanguageDisplayName(
        targetLanguage: String = mTargetLanguage.value
    ): String {
        return when (targetLanguage) {
            "" -> {
                "Target"
            }

            else -> {
                languageMap[targetLanguage] ?: ""
            }
        }
    }

    fun getDisplayNameByKey(languageKey: String): String {
        return languageMap[languageKey] ?: ""
    }

    fun navigateToLanguageSelectPage(selectMode: SelectMode) {
        this.selectMode.value = selectMode
        isSelecting.value = true
    }
}