package com.example.seton.feature_notes.presentation.edit_note

data class NoteState(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val imageFileName: String? = null
)