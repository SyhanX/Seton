package com.example.seton.feature_notes.domain.repository

import com.example.seton.feature_notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int) : Note?
    suspend fun upsertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteAllNotes()
}