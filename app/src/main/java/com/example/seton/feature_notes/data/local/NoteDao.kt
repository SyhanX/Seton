package com.example.seton.feature_notes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.seton.feature_notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table WHERE noteId = :id")
    suspend fun deleteNoteById(id: Int)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_table WHERE noteId = :id")
    suspend fun getNoteById(id: Int) : Note?

    @Query("SELECT * FROM note_table")
    fun getAllNotes() : Flow<List<Note>>

}