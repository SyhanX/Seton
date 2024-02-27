package com.example.seton.feature_notes.domain.use_case

import com.example.seton.feature_notes.domain.model.NoteWithImages
import com.example.seton.feature_notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllImageNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<NoteWithImages>> {
        return repository.getAllImageNotes()
    }
}