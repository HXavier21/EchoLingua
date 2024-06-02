package com.example.echolingua.ui.page.audioTranscribePages

import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolingua.App
import com.example.echolingua.ffmpeg.FFmpegUtil
import com.example.echolingua.media.decodeWaveFile
import com.example.echolingua.network.DownloadStatus
import com.example.echolingua.network.downloadWhisperModel
import com.example.echolingua.whisper.WhisperContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "AudioTranscribePageView"

class AudioTranscribePageViewModel : ViewModel() {
    data class ModelState(
        val model: Model? = null,
        val status: Status = Status.NotDownloaded
    ) {
        sealed interface Status {
            data object NotDownloaded : Status
            data class Downloading(val progress: Float = 0f) : Status
            data object Loaded : Status
        }
    }

    private val mModelStateFlow = MutableStateFlow(ModelState())
    val modelStateFlow = mModelStateFlow.asStateFlow()

    private val mTextFlow = MutableStateFlow("")
    val textFlow = mTextFlow.asStateFlow()

    private val mCanTranscribeFlow = MutableStateFlow(false)
    val canTranscribeFlow = mCanTranscribeFlow.asStateFlow()

    private var audioPath: String = ""

    fun selectModel(model: Model) {
        mModelStateFlow.update { it.copy(model = model) }
    }

    fun loadModelOrDownload() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = mModelStateFlow.value.model ?: return@launch
            val modelName = model.name
            val fileUrl = model.url
            val destinationPath = App.context.filesDir.path + "/models/$modelName.bin"

            val models = App.modelsPath.list()

            if (models == null || !models.contains("$modelName.bin")) {
                downloadWhisperModel(
                    fileUrl = fileUrl,
                    destinationPath = destinationPath,
                ).collect { status ->
                    when (status) {
                        is DownloadStatus.Downloading -> mModelStateFlow.update {
                            it.copy(
                                status = ModelState.Status.Downloading(
                                    status.progress
                                )
                            )
                        }

                        is DownloadStatus.Error -> {
                            mModelStateFlow.update {
                                it.copy(
                                    status = ModelState.Status.NotDownloaded
                                )
                            }
                            status.th.printStackTrace()
                        }

                        is DownloadStatus.Finished -> {
                            loadModel(modelName)
                        }
                    }
                }
            } else {
                loadModel(modelName)
            }
        }
    }

    private suspend fun loadModel(modelName: String) =
        withContext(Dispatchers.IO) {
            runCatching {
                WhisperContext.createContextFromFile(App.modelsPath.path + "/$modelName.bin")
            }
                .onSuccess { ctx ->
                    App.whisperContext = ctx
                    mModelStateFlow.update { it.copy(status = ModelState.Status.Loaded) }
                    mCanTranscribeFlow.update { true }
                }
                .onFailure {
                    mModelStateFlow.update {
                        it.copy(status = ModelState.Status.NotDownloaded)
                    }
                    val modelFile = File(App.modelsPath.path + "/$modelName.bin")
                    if (modelFile.exists()) modelFile.delete()
                }
        }

    fun startRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            val time = System.currentTimeMillis() / 1000L
            val file = getTempFileForRecording(time)
            try {
                with(App.mediaRecorder) {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setAudioChannels(1)
                    setAudioEncodingBitRate(128000)
                    setAudioSamplingRate(48000)
                    setOutputFile(file.path)
                    prepare()
                    start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(App.context, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            audioPath = file.path
        }

    }

    fun stopRecording() {
        viewModelScope.launch(Dispatchers.IO) {
            App.mediaRecorder.stop()
            App.mediaRecorder.reset()
            File(audioPath).let {
                val storedFile =
                    getStoredFileForRecording(
                        audioPath.split("audio", ".").dropLast(1).last()
                    )
                FFmpegUtil.audioToWav(it.path, storedFile.path)
                val waveData = decodeWaveFile(storedFile)

                Log.d(TAG, "stopRecording: transcribe start!")
                App.whisperContext.transcribeData(
                    data = waveData
                ).let { str ->
                    mTextFlow.update { str }
                    Log.d(TAG, "stopRecording: transcribe end! $str")
                }
            }
        }
    }

    private suspend fun getStoredFileForRecording(fileIndex: String) = withContext(Dispatchers.IO) {
        File(App.audioPath, "recording$fileIndex.wav")
    }

    private suspend fun getTempFileForRecording(fileIndex: Long) = withContext(Dispatchers.IO) {
        File.createTempFile("audio$fileIndex", ".wav")
    }
}