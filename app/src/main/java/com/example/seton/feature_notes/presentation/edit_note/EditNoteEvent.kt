package com.example.seton.feature_notes.presentation.edit_note

sealed class EditNoteEvent {
    data class EnterTitle(val title: String): EditNoteEvent()
    data class EnterContent(val content: String): EditNoteEvent()
    data object SaveNote: EditNoteEvent()
    data object DeleteNote: EditNoteEvent()
}