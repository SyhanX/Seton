package com.example.seton.domain.use_case

import com.example.seton.domain.repository.NoteRepository

class GetNoteById(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int) = repository.getNoteById(id)
}