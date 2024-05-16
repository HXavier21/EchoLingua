package com.example.echolingua.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TranslateHistoryDao {
    @Insert
    fun insert(translateHistoryItem: TranslateHistoryItem): Long

    @Query("select * from translatehistoryitem")
    fun getAll(): List<TranslateHistoryItem>

    @Query("select * from translatehistoryitem where sourceText = :sourceText and targetText = :targetText and sourceLanguage = :sourceLanguage and targetLanguage = :targetLanguage ")
    fun checkIfTranslated(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String,
        targetText: String
    ): List<TranslateHistoryItem>

    @Update
    fun update(translateHistoryItem: TranslateHistoryItem)

    @Delete
    fun delete(translateHistoryItem: TranslateHistoryItem)
}