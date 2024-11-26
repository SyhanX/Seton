package com.example.seton.feature_notes.presentation.edit_note

import com.example.seton.common.presentation.state.ContainerColor
import java.util.Date

data class NoteState(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val color: ContainerColor = ContainerColor.Default,
    val creationDate: Date? = null,
    val modificationDate: Date? = null,
)

sealed interface NoteStateType {
    data class Title(val title: String) : NoteStateType
    data class Content(val content: String) : NoteStateType
    data class Color(val color: ContainerColor) : NoteStateType
}