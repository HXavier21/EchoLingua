package com.example.echolingua

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.MediaRecorder
import com.example.echolingua.whisper.WhisperContext
import java.io.File

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        System.loadLibrary("x264")
        System.loadLibrary("app")
        modelsPath = File(filesDir, "models")
        audioPath = File(filesDir, "audio")
        modelsPath.mkdirs()
        audioPath.mkdirs()
        mediaRecorder = MediaRecorder()
    }

    override fun onTerminate() {
        super.onTerminate()
        mediaRecorder.release()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var modelsPath: File
        lateinit var audioPath: File
        lateinit var whisperContext: WhisperContext
        lateinit var mediaRecorder: MediaRecorder
    }

}