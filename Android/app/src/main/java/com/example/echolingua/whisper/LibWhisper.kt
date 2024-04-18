package com.example.echolingua.whisper

import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.util.concurrent.Executors

private const val LOG_TAG = "LibWhisper"
private const val TAG = "LibWhisper"

class WhisperContext private constructor(private var ptr: Long) {
    // Meet Whisper C++ constraint: Don't access from more than one thread at a time.
    private val scope: CoroutineScope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )

    suspend fun transcribeData(
        data: FloatArray,
        progressCallback: (Int) -> Unit = { }
    ): String = withContext(scope.coroutineContext) {
        require(ptr != 0L)
        WhisperLib.fullTranscribe(
            contextPtr = ptr,
            language = "auto",
            progressCallback = object :
                ProgressCallback() {
                override fun onUpdate(progress: Int) {
                    Log.d(TAG, "onUpdate: $progress")
                    progressCallback(progress)
                }
            }, audioData = data
        )
        val textCount = WhisperLib.getTextSegmentCount(ptr)
        return@withContext buildString {
            for (i in 0 until textCount) {
                append(WhisperLib.getTextSegment(ptr, i))
            }
        }
    }

    private suspend fun release() = withContext(scope.coroutineContext) {
        if (ptr != 0L) {
            WhisperLib.freeContext(ptr)
            ptr = 0
        }
    }

    protected fun finalize() {
        runBlocking {
            release()
        }
    }

    companion object {
        fun createContextFromFile(filePath: String): WhisperContext {
            val ptr = WhisperLib.initContext(filePath)
            if (ptr == 0L) {
                throw java.lang.RuntimeException("Couldn't create context with path $filePath")
            }
            return WhisperContext(ptr)
        }
    }
}

private object WhisperLib {
    // JNI methods
    external fun initContext(modelPath: String): Long
    external fun freeContext(contextPtr: Long)
    external fun fullTranscribe(
        contextPtr: Long,
        language: String,
        progressCallback: ProgressCallback? = null,
        audioData: FloatArray
    )

    external fun getTextSegmentCount(contextPtr: Long): Int
    external fun getTextSegment(contextPtr: Long, index: Int): String
}
