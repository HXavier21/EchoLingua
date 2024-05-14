package com.example.echolingua.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TranslateHistoryItem::class], version = 1, exportSchema = false)
abstract class TranslateHistoryDatabase : RoomDatabase() {
    abstract fun translateHistoryDao(): TranslateHistoryDao
}