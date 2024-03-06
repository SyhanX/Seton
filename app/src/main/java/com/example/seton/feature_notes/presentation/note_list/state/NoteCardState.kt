package com.example.seton.feature_notes.presentation.note_list.state

data class NoteCardState(
    val id: Int,
    val title: String,
    val content: String,
    val isChecked: Boolean,
    val onClick: (Int, () -> Unit) -> Unit,
    val onLongClick: (Int) -> Unit
)
