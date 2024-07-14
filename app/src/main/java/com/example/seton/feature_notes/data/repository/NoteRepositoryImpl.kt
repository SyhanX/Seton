package com.example.seton.feature_notes.data.repository

import com.example.seton.feature_notes.data.local.NoteDao
import com.example.seton.feature_notes.domain.model.Note
import com.example.seton.feature_notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = dao.getAllNotes()
    override suspend fun getNoteById(id: Int): Note? = dao.getNoteById(id)
    override suspend fun upsertNote(note: Note) = dao.upsertNote(note)
    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)
    override suspend fun deleteNoteById(id: Int) = dao.deleteNoteById(id)
    override suspend fun deleteAllNotes() = dao.deleteAllNotes()
}