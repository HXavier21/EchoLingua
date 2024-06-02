package com.example.echolingua.network

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object HttpUtil {
    fun sendOkHttpRequest(address: String, callback: Callback) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(address)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun sendOkHttpPostRequest(address: String, callback: Callback, requestBody: RequestBody) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(address)
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(callback)
    }
}