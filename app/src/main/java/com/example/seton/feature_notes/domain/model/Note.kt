package com.example.seton.feature_notes.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.seton.common.presentation.state.ContainerColor

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey
    val noteId: Int? = null,
    val title: String,
    val content: String,
    val imageFileName: String? = null,
    val color: ContainerColor = ContainerColor.Default
)

class InvalidNoteException(message: String): Exception(message)


