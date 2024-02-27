package com.example.seton.feature_notes.domain.use_case

import com.example.seton.feature_notes.domain.repository.NoteRepository

class DeleteAllNotes(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllNotes()
    }
}