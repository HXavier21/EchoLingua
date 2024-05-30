package com.example.echolingua.ui.page

import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.echolingua.App
import com.example.echolingua.network.HttpUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val TAG = "BackEndTestPageViewMode"

const val address = "http://frp-fit.top:15501"

class BackEndTestPageViewModel : ViewModel() {
    private val mMessage = MutableStateFlow("")
    val message = mMessage.asStateFlow()

    fun setMessage(message: String) {
        mMessage.value = message
    }

    fun register(email: String, password: String) {
        HttpUtil.sendOkHttpPostRequest(
            "$address/register",
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    setMessage("Register failed")
                    Log.e(TAG, "onFailure: ", e)
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(App.context, "Register failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    setMessage(response.body?.string() ?: "")
                }
            },
            requestBody = generateRequestBody(
                "email" to email,
                "password" to password
            )
        )
    }

    fun login(email: String, password: String) {
        HttpUtil.sendOkHttpPostRequest(
            "$address/login",
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    setMessage("Login failed")
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(App.context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    setMessage(response.body?.string() ?: "")
                }
            },
            requestBody = generateRequestBody(
                "email" to email,
                "password" to password
            )
        )
    }

    fun translate(
        email: String,
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ) {
        HttpUtil.sendOkHttpPostRequest(
            "$address/translate?email=$email",
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    setMessage("Translate failed")
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(App.context, "Translate failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    setMessage(response.body?.string() ?: "")
                }
            },
            requestBody = generateRequestBody(
                "SourceText" to sourceText,
                "Source" to sourceLanguage,
                "Target" to targetLanguage
            )
        )
    }

    fun getTTSService(model: String = "Asta", text: String, language: String) {
        HttpUtil.sendOkHttpPostRequest(
            "$address/get_tts_service",
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    setMessage("TTS failed")
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(App.context, "TTS failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    viewModelScope.launch {
                        val file = File.createTempFile("tempAudio", ".wav")
                        withContext(Dispatchers.IO) {
                            response.body?.bytes().let {
                                file.apply {
                                    writeBytes(it ?: byteArrayOf())
                                    Log.d(TAG, "onResponse: file copied")
                                }
                            }
                        }
                        withContext(Dispatchers.Main) {
                            App.player.apply {
                                setMediaItem(
                                    MediaItem.fromUri(
                                        file.toUri()
                                    )
                                )
                                prepare()
                                play()
                                Log.d(TAG, "onResponse: play over")
                            }
                        }
                    }
                }
            },
            requestBody = generateRequestBody(
                "model" to model,
                "text" to text,
                "text_language" to language
            )
        )
    }

    fun getTTSServiceByGet(model: String = "Asta", text: String, language: String) {
        HttpUtil.sendOkHttpRequest(
            "$address/get_tts_service?model=$model&text=$text&text_language=$language",
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    setMessage("TTS failed")
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(App.context, "TTS failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    viewModelScope.launch {
                        val file = File.createTempFile("tempAudio", ".wav")
                        withContext(Dispatchers.IO) {
                            response.body?.bytes().let {
                                file.apply {
                                    writeBytes(it ?: byteArrayOf())
                                    Log.d(TAG, "onResponse: file copied")
                                }
                            }
                        }
                        withContext(Dispatchers.Main) {
                            App.player.apply {
                                setMediaItem(
                                    MediaItem.fromUri(
                                        file.toUri()
                                    )
                                )
                                prepare()
                                play()
                                Log.d(TAG, "onResponse: play over")
                            }
                        }
                    }
                }
            }
        )

    }
}

private fun generateRequestBody(vararg pairs: Pair<String, String>): okhttp3.RequestBody {
    val json = pairs.joinToString(",", "{", "}") { (key, value) -> "\"$key\":\"$value\"" }
    return json.toRequestBody(null)
}