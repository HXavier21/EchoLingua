package com.example.echolingua.ui.page.stateHolders

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.echolingua.App
import com.example.echolingua.network.DownloadStatus
import com.example.echolingua.network.downloadWhisperModel
import com.example.echolingua.whisper.WhisperContext
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "WhisperModelStateHolder"

object WhisperModelStateHolder {
    data class Model(val name: String, val url: String, val fileSizeInMB: Int = 0)

    private val tinyModel = Model(
        name = "ggml-tiny",
        url = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-tiny.bin",
        fileSizeInMB = 75
    )
    private val baseModel = Model(
        name = "ggml-base",
        url = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-base.bin",
        fileSizeInMB = 142
    )
    private val smallModel = Model(
        name = "ggml-small",
        url = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-small.bin",
        fileSizeInMB = 466
    )
    private val distillSmallModel = Model(
        name = "ggml-distil-small",
        url = "https://huggingface.co/distil-whisper/distil-small.en/resolve/main/ggml-distil-small.en.bin",
        fileSizeInMB = 336
    )

    val ModelList = listOf(
        tinyModel, baseModel, smallModel, distillSmallModel
    )

    enum class ModelStatus {
        NotDownloaded, Downloading, Downloaded, Loaded
    }

    private var mModelStateMap = mutableStateMapOf<Model, ModelStatus>()
    val modelStateMap = mModelStateMap

    private var mCanTranscribe = mutableStateOf(false)
    val canTranscribe = mCanTranscribe

    private var mDownloadProgress = mutableFloatStateOf(0f)
    val downloadProgress = mDownloadProgress

    init {
        checkModelsState()
    }

    private fun checkModelsState() {
        val models = App.modelsPath.list()
        for (model in ModelList) {
            if (models != null && models.contains("${model.name}.bin")) {
                mModelStateMap[model] = ModelStatus.Downloaded
            } else {
                mModelStateMap[model] = ModelStatus.NotDownloaded
            }
        }
    }

    suspend fun downloadModel(model: Model) {
        withContext(Dispatchers.IO) {
            mModelStateMap[model] = ModelStatus.Downloading
            downloadWhisperModel(
                fileUrl = model.url, destinationPath = App.modelsPath.path + "/${model.name}.bin"
            ).collect { status ->
                when (status) {
                    is DownloadStatus.Downloading -> {
                        mDownloadProgress.floatValue = status.progress
                        mModelStateMap[model] = ModelStatus.Downloading
                    }

                    is DownloadStatus.Error -> {
                        mModelStateMap[model] = ModelStatus.NotDownloaded
                        status.th.printStackTrace()
                    }

                    is DownloadStatus.Finished -> {
                        mModelStateMap[model] = ModelStatus.Downloaded
                    }
                }
            }
        }
    }

    suspend fun loadModel(modelToLoad: Model) = withContext(Dispatchers.IO) {
        runCatching {
            for (model in ModelList) {
                if (mModelStateMap[model] == ModelStatus.Loaded) {
                    mModelStateMap[model] = ModelStatus.Downloaded
                }
                mModelStateMap[modelToLoad] = ModelStatus.Loaded
            }
            WhisperContext.createContextFromFile(App.modelsPath.path + "/${modelToLoad.name}.bin")
        }.onSuccess { ctx ->
            App.whisperContext = ctx
            mCanTranscribe.value = true
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    App.context, "${modelToLoad.name} - " + "Model loaded", Toast.LENGTH_SHORT
                ).show()
            }
            val mmkv = MMKV.defaultMMKV()
            mmkv.encode("selectedModel", modelToLoad.name)
        }.onFailure {
            Log.e(TAG, "loadModel: ", it)
            mModelStateMap[modelToLoad] = ModelStatus.Downloaded
        }
    }

    suspend fun deleteModel(model: Model) = withContext(Dispatchers.IO) {
        val file = App.modelsPath.resolve("${model.name}.bin")
        if (file.exists()) {
            file.delete()
            mModelStateMap[model] = ModelStatus.NotDownloaded
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    App.context, "${model.name} - Model deleted", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}