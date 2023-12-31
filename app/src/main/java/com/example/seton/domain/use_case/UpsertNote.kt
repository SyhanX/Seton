package com.example.seton.domain.use_case

import com.example.seton.data.model.InvalidNoteException
import com.example.seton.data.model.Note
import com.example.seton.data.repository.NoteRepository

class UpsertNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()) {
            throw InvalidNoteException("The title of the note cannot be empty.")
        }
        if(note.content.isBlank()) {
            throw InvalidNoteException("The content of the note cannot be empty.")
        }
        repository.upsertNote(note)
    }
}