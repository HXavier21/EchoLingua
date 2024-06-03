package com.example.echolingua.util

import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.echolingua.App
import com.example.echolingua.ffmpeg.FFmpegUtil
import com.example.echolingua.media.decodeWaveFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "Recorder"

object Recorder {

    private var mIsTranscribe = mutableStateOf(false)
    val isTranscribe = mIsTranscribe

    private var audioPath: String = ""

    suspend fun startRecording() {
        withContext(Dispatchers.IO) {
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

    suspend fun stopRecording(transcribeCallback: (String) -> Unit = {}) {
        isTranscribe.value = true
        withContext(Dispatchers.IO) {
            App.mediaRecorder.stop()
            App.mediaRecorder.reset()
            File(audioPath).let {
                val storedFile = getStoredFileForRecording(
                    audioPath.split("audio", ".").dropLast(1).last()
                )
                FFmpegUtil.audioToWav(it.path, storedFile.path)
                val waveData = decodeWaveFile(storedFile)

                Log.d(TAG, "stopRecording: transcribe start!")
                App.whisperContext.transcribeData(
                    data = waveData
                ).let { str ->
                    isTranscribe.value = false
                    transcribeCallback(str)
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