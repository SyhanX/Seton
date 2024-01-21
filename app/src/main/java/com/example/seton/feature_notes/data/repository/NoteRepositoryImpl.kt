package com.example.seton.feature_notes.data.repository

import com.example.seton.feature_notes.data.datasource.NoteDao
import com.example.seton.feature_notes.data.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = dao.getAllNotes()
    override suspend fun getNoteById(id: Int): Note? = dao.getNoteById(id)
    override suspend fun upsertNote(note: Note) = dao.upsertNote(note)
    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)
}