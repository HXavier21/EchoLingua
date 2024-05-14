package com.example.echolingua.database

import androidx.room.Room
import com.example.echolingua.App

object DataRepository {
    private val db = Room.databaseBuilder(
        context = App.context, TranslateHistoryDatabase::class.java, "TranscribeDatabase"
    ).build()

    private val dao = db.translateHistoryDao()

    fun insert(translateHistoryItem: TranslateHistoryItem): Long = dao.insert(translateHistoryItem)

    fun getAll(): List<TranslateHistoryItem> = dao.getAll()

    /**
     * Check if the translation is already in the database
     * @param sourceLanguage the source language
     * @param targetLanguage the target language
     * @param sourceText the source text
     * @param targetText the target text
     * @return a list of TranslateHistoryItem that match the query
     */
    fun checkIfTranslated(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String,
        targetText: String
    ): List<TranslateHistoryItem> =
        dao.checkIfTranslated(sourceLanguage, targetLanguage, sourceText, targetText)

    fun update(translateHistoryItem: TranslateHistoryItem) = dao.update(translateHistoryItem)

    fun delete(translateHistoryItem: TranslateHistoryItem) = dao.delete(translateHistoryItem)
}