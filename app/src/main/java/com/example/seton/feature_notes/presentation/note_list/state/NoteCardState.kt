package com.example.seton.feature_notes.presentation.note_list.state

import com.example.seton.feature_notes.presentation.edit_note.components.SelectedColor

data class NoteCardState(
    val id: Int,
    val title: String,
    val content: String,
    val isChecked: Boolean,
    val color: SelectedColor,
    val onClick: (Int, () -> Unit) -> Unit,
    val onLongClick: (Int) -> Unit
)
