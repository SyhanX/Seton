package com.example.seton.feature_notes.presentation.note_list.state

data class NoteListState(
    val noteList: List<NoteCardState> = emptyList(),
    val selectedNoteList: Set<Int> = emptySet(),
    val isLinearLayout: Boolean = true
)

