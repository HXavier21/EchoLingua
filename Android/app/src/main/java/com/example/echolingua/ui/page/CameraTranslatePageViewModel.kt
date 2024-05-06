package com.example.echolingua.ui.page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

private const val TAG = "CameraTranslatePageView"

class CameraTranslatePageViewModel : ViewModel() {
    private val mImageFileFlow = MutableStateFlow(File(""))
    val imageFileFlow = mImageFileFlow.asStateFlow()


    fun setImageFile(file: File) {
        mImageFileFlow.update { file }
    }



}