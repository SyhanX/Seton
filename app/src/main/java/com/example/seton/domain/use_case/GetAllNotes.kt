package com.example.seton.domain.use_case

import com.example.seton.data.model.Note
import com.example.seton.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}