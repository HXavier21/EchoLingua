package com.example.echolingua.whisper

abstract class ProgressCallback {
    abstract fun onUpdate(progress: Int)
}