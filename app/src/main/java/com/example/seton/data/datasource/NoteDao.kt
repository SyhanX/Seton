package com.example.seton.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.seton.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNoteById(id: Int) : Note?

    @Query("SELECT * FROM note_table")
    fun getAllNotes() : Flow<List<Note>>
}