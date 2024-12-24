package com.example.seton.feature_notes.presentation.note_list.state

import com.example.seton.common.presentation.state.AccentColor

data class NoteCardState(
    val id: Int,
    val title: String,
    val content: String,
    val isSelected: Boolean,
    val color: AccentColor,
    val onClick: (Int, () -> Unit) -> Unit,
    val onLongClick: (Int) -> Unit
)
