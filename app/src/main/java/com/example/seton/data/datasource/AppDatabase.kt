package com.example.seton.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seton.data.model.Note

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