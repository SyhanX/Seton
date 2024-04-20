package com.example.seton.feature_notes.presentation.note_list.state

data class NoteCardState(
    val id: Int,
    val title: String,
    val content: String,
    val isChecked: Boolean,
    val onClick: (Int, () -> Unit) -> Unit,
    val onLongClick: (Int) -> Unit
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteCardState

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (isChecked != other.isChecked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + isChecked.hashCode()
        return result
    }
}
