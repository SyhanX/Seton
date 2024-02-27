package com.example.seton.feature_notes.presentation.edit_note.state

sealed class EditNoteEvent {
    data class EnterTitle(val title: String): EditNoteEvent()
    data class EnterContent(val content: String): EditNoteEvent()
    data class InsertImage(val byteArray: ByteArray): EditNoteEvent()
    data object SaveNote: EditNoteEvent()
    data object DeleteNote: EditNoteEvent()
}