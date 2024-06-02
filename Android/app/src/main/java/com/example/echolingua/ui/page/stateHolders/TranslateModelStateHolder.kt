package com.example.echolingua.ui.page.stateHolders

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

enum class TranslateModelState {
    NOT_DOWNLOADED, DOWNLOADING, DOWNLOADED
}

object TranslateModelStateHolder {
    private val mModelStateMap = mutableStateMapOf<String, TranslateModelState>()
    val modelStateMap = mModelStateMap

    var currentLanguage = mutableStateOf("")
    var isManagingModel = mutableStateOf(false)

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
            if (mModelStateMap[language] != TranslateModelState.DOWNLOADING) {
                checkIfModelExists(language) {
                    if (it) {
                        mModelStateMap[language] = TranslateModelState.DOWNLOADED
                    } else {
                        mModelStateMap[language] = TranslateModelState.NOT_DOWNLOADED
                    }
                }
            }
        }
    }

    fun downloadModel(language: String) {
        mModelStateMap[language] = TranslateModelState.DOWNLOADING
        val modelManager = RemoteModelManager.getInstance()
        val model = TranslateRemoteModel.Builder(language).build()
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        modelManager.download(model, conditions)
            .addOnSuccessListener {
                mModelStateMap[language] = TranslateModelState.DOWNLOADED
            }
            .addOnFailureListener {
                Log.e(TAG, "downloadModel: ${it.stackTraceToString()}")
                Toast.makeText(
                    App.context,
                    "$language Model download failed",
                    Toast.LENGTH_SHORT
                ).show()
                mModelStateMap[language] = TranslateModelState.NOT_DOWNLOADED
            }
    }

    fun deleteModel(language: String) {
        val modelManager = RemoteModelManager.getInstance()
        val model = TranslateRemoteModel.Builder(language).build()
        modelManager.deleteDownloadedModel(model)
            .addOnSuccessListener {
                Log.d(TAG, "deleteModel: $language Model deleted")
                mModelStateMap[language] = TranslateModelState.NOT_DOWNLOADED
            }
            .addOnFailureListener {
                Log.e(TAG, "deleteModel: ${it.stackTraceToString()}")
                Toast.makeText(
                    App.context,
                    "$language Model delete failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun getModelState(language: String): TranslateModelState {
        return mModelStateMap[language] ?: TranslateModelState.NOT_DOWNLOADED
    }
}