package com.example.seton.presentation.note_list.state

sealed class NoteEvent {
    data object EnableCheckMode: NoteEvent()
    data class SelectNote(val noteId: Int): NoteEvent()
    data object ChangeLayout: NoteEvent()
}