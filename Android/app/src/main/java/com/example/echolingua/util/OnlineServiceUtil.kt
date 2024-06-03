package com.example.echolingua.util

import com.example.echolingua.network.HttpUtil
import com.example.echolingua.ui.page.address
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

private const val TAG = "OnlineServiceUtil"

object OnlineServiceUtil {
    fun getTTSService(
        model: String = "Asta",
        text: String,
        language: String,
        onResponseCallback: (Response) -> Unit = {},
        onFailureCallback: (IOException) -> Unit = {}
    ) {
        HttpUtil.sendOkHttpPostRequest(
            "$address/get_tts_service", object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    onFailureCallback(e)
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    onResponseCallback(response)
                }
            }, requestBody = generateRequestBody(
                "model" to model, "text" to text, "text_language" to language
            )
        )
    }
}

private fun generateRequestBody(vararg pairs: Pair<String, String>): okhttp3.RequestBody {
    val json = pairs.joinToString(",", "{", "}") { (key, value) -> "\"$key\":\"$value\"" }
    return json.toRequestBody(null)
}