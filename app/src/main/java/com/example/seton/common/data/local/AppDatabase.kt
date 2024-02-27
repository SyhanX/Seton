package com.example.seton.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seton.feature_notes.data.datasource.NoteDao
import com.example.seton.feature_notes.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}