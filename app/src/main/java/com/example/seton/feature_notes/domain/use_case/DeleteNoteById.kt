package com.example.seton.feature_notes.domain.use_case

import com.example.seton.feature_notes.domain.repository.NoteRepository

class DeleteNoteById(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteNoteById(id)
}