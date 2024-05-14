package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.echolingua.App
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel

private const val TAG = "TranslateModelStateHold"

enum class TranslateModelStatus {
    NOT_DOWNLOADED, DOWNLOADING, DOWNLOADED
}

object TranslateModelStateHolder {
    private val mModelStateMap = mutableStateMapOf<String, TranslateModelStatus>()
    val modelStateMap = mModelStateMap

    fun checkIfModelExists(language: String, callback: (Boolean) -> Unit) {
        val modelManager = RemoteModelManager.getInstance()
        val model = TranslateRemoteModel.Builder(language).build()
        modelManager.isModelDownloaded(model)
            .addOnSuccessListener { isDownloaded ->
                callback(isDownloaded)
            }
            .addOnFailureListener {
                Log.e(TAG, "checkIfModelExists: ${it.stackTraceToString()}")
            }
    }

    fun refreshWholeModelStateMap() {
        for (language in TranslateLanguage.getAllLanguages()) {
            if (mModelStateMap[language] != TranslateModelStatus.DOWNLOADING) {
                checkIfModelExists(language) {
                    if (it) {
                        mModelStateMap[language] = TranslateModelStatus.DOWNLOADED
                    } else {
                        mModelStateMap[language] = TranslateModelStatus.NOT_DOWNLOADED
                    }
                }
            }
        }
    }

    fun downloadModel(language: String) {
        mModelStateMap[language] = TranslateModelStatus.DOWNLOADING
        val modelManager = RemoteModelManager.getInstance()
        val model = TranslateRemoteModel.Builder(language).build()
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        modelManager.download(model, conditions)
            .addOnSuccessListener {
                mModelStateMap[language] = TranslateModelStatus.DOWNLOADED
            }
            .addOnFailureListener {
                Log.e(TAG, "downloadModel: ${it.stackTraceToString()}")
                Toast.makeText(
                    App.context,
                    "$language Model download failed",
                    Toast.LENGTH_SHORT
                ).show()
                mModelStateMap[language] = TranslateModelStatus.NOT_DOWNLOADED
            }
    }
}