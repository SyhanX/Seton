package com.example.seton.feature_notes.presentation.edit_note

import com.example.seton.common.presentation.state.ContainerColor

data class NoteState(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val imageFileName: String? = null,
    val color: ContainerColor = ContainerColor.Default
)

sealed interface NoteStateType {
    data class Title(val title: String) : NoteStateType
    data class Content(val content: String) : NoteStateType
    data class Color(val color: ContainerColor) : NoteStateType
}