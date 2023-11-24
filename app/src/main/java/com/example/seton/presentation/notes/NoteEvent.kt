package com.example.seton.presentation.notes

import com.example.seton.domain.model.Note

sealed class NoteEvent {
    data class DeleteNote(val note: Note): NoteEvent()
    data object ChangeLayout: NoteEvent()
}