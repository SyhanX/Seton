package com.example.seton.feature_notes.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey
    val noteId: Int? = null,
    val title: String,
    val content: String,
    val imageFileName: String? = null
)

class InvalidNoteException(message: String): Exception(message)


/*
* additional properties that I will never implement
*
* val createdAt:
* val editedAt:
* val color:
*/

