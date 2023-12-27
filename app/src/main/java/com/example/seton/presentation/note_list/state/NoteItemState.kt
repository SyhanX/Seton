package com.example.seton.presentation.note_list.state

data class NoteItemState(
    val id: Int,
    val title: String,
    val content: String,
    val isChecked: Boolean,
    val onLongClick: (Int) -> Unit
)
