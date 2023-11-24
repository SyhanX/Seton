package com.example.seton.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    val title: String,
    val content: String,
    @PrimaryKey
    val id: Int? = null
)


class InvalidNoteException(message: String): Exception(message)


/*
* additional properties that I will never implement
*
* val createdAt:
* val editedAt:
* val color:
*/

