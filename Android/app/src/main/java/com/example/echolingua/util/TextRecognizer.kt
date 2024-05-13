package com.example.echolingua.util

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.echolingua.App
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

private const val TAG = "TextRecognizer"

object TextRecognizer {
    // When using Latin script library
    private val latinRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // When using Chinese script library
    private val chineseRecognizer =
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    fun processImage(
        imageFile: Uri,
        language: String,
        refreshRecognizedText: (Text) -> Unit = {}
    ) {
        val image = InputImage.fromFilePath(App.context, imageFile)
        when (language) {
            TranslateLanguage.ENGLISH -> latinRecognizer
            TranslateLanguage.CHINESE -> chineseRecognizer
            else -> null
        }?.let { recognizer ->
            recognizer.process(image)
                .addOnSuccessListener {
//                    for (block in it.textBlocks) {
//                        for (line in block.lines) {
//                            for (element in line.elements) {
//                                Log.d(TAG, "processImage: ${element.text}")
//                            }
//                        }
//                    }
                    refreshRecognizedText(it)
                }
                .addOnFailureListener {
                    Log.e(TAG, "processImage: $it")
                    Toast.makeText(
                        App.context,
                        "Failed to recognize text",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}