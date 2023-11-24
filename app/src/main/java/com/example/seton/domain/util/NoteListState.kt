package com.example.seton.domain.util

import com.example.seton.domain.model.Note

data class NoteListState(
    val noteList: List<Note> = emptyList(),
    val isLinearLayout: Boolean = true
)