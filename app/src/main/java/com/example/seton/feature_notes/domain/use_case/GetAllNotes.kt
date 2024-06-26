package com.example.seton.feature_notes.domain.use_case

import com.example.seton.feature_notes.domain.model.Note
import com.example.seton.feature_notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}