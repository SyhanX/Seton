package com.example.seton.feature_notes.data

data class NoteSharedElementKey(
    val id: Int,
    val value: String,
    val type: NoteTextType
)

enum class NoteTextType {
    Title, Content, Color
}
