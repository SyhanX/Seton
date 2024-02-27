package com.example.seton.feature_notes.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.seton.feature_notes.domain.model.Note
import com.example.seton.feature_notes.domain.model.NoteWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_table WHERE noteId = :id")
    suspend fun getNoteById(id: Int) : Note?

    @Query("SELECT * FROM note_table WHERE noteId = :id")
    suspend fun getImageNoteById(id: Int) : Note?

    @Query("SELECT * FROM note_table")
    fun getAllNotes() : Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM note_table")
    fun getAllImageNotes() : Flow<List<NoteWithImages>> //maybe a suspend function?
}