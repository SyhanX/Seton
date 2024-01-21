package com.example.seton.feature_notes.domain.use_case

import com.example.seton.feature_notes.data.model.Note
import com.example.seton.feature_notes.data.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}