package com.example.seton.domain.use_case

import com.example.seton.domain.model.Note
import com.example.seton.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}