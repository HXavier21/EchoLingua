package com.example.echolingua.ui.page.quickTranslatePages

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuickTranslatePageViewModel : ViewModel() {
    private val mSourceTextFlow = MutableStateFlow("")
    val sourceTextFlow = mSourceTextFlow.asStateFlow()

    private val mTargetTextFlow = MutableStateFlow("")
    val targetTextFlow = mTargetTextFlow.asStateFlow()

    private val mTransformModeFlow = MutableStateFlow(TransformMode.NewTranslation)
    val transformModeFlow = mTransformModeFlow.asStateFlow()

    fun setSourceText(text: String) {
        mSourceTextFlow.value = text
    }

    fun setTargetText(text: String) {
        mTargetTextFlow.value = text
    }

    fun setTransformMode(mode: TransformMode) {
        mTransformModeFlow.value = mode
    }
}