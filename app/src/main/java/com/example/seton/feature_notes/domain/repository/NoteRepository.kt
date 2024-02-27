package com.example.seton.feature_notes.domain.repository

import com.example.seton.feature_notes.domain.model.Note
import com.example.seton.feature_notes.domain.model.NoteWithImages
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getAllImageNotes(): Flow<List<NoteWithImages>>
    suspend fun getNoteById(id: Int) : Note?
    suspend fun getNoteWithImageById(id: Int): Note?
    suspend fun upsertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteAllNotes()
}