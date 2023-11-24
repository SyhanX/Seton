package com.example.seton.domain.repository

import com.example.seton.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int) : Note?
    suspend fun upsertNote(note: Note)
    suspend fun deleteNote(note: Note)
}