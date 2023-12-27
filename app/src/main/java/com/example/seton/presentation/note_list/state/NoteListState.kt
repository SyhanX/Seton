package com.example.seton.presentation.note_list.state

data class NoteListState(
    val noteList: List<NoteItemState> = emptyList(),
    val isLinearLayout: Boolean = true,
    val isCheckMode: Boolean = false
)

