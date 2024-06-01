package com.example.echolingua.ui.page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainTranslatePageViewModel : ViewModel() {
    private val mSourceTextFlow = MutableStateFlow("")
    val sourceTextFlow = mSourceTextFlow.asStateFlow()

    private val mTargetTextFlow = MutableStateFlow("")
    val targetTextFlow = mTargetTextFlow.asStateFlow()

    fun setSourceText(text: String) {
        mSourceTextFlow.update { text }
    }

    fun setTargetText(text: String) {
        mTargetTextFlow.update { text }
    }
}