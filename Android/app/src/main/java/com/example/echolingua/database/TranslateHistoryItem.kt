package com.example.echolingua.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TranslateHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val sourceLanguage: String,
    val targetLanguage: String,
    val sourceText: String,
    val targetText: String,
    val time: String
)

