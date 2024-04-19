package com.example.echolingua.network

import android.util.Log
import androidx.annotation.CheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

private const val TAG = "DownloadModel"

sealed interface DownloadStatus {
    data class Error(val th: Throwable) : DownloadStatus
    data class Downloading(val progress: Float) : DownloadStatus
    data class Finished(val file: File) : DownloadStatus
}

@CheckResult
fun downloadModel(
    fileUrl: String,
    destinationPath: String,
): Flow<DownloadStatus> = flow {
    val outputFile = File(destinationPath)

    runCatching {
        emit(DownloadStatus.Downloading(0f))

        val url = URL(fileUrl)
        val connection = url.openConnection()
        connection.connect()
        val fileLength = connection.contentLength
        val input = BufferedInputStream(url.openStream())
        val output = FileOutputStream(outputFile)
        val data = ByteArray(1024)
        var totalBytes = 0
        var bytesRead: Int

        while (input.read(data).also { bytesRead = it } != -1) {
            totalBytes += bytesRead
            output.write(data, 0, bytesRead)
            val progress = (totalBytes.toFloat() / fileLength)
            emit(DownloadStatus.Downloading(progress))
        }

        output.flush()
        output.close()
        input.close()
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        emit(DownloadStatus.Error(it))
        if (outputFile.exists()) {
            outputFile.delete()
        }

    }.onSuccess {
        emit(DownloadStatus.Finished(outputFile))
    }
}.flowOn(Dispatchers.IO).distinctUntilChanged()
