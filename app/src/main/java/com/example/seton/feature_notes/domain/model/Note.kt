package com.example.seton.feature_notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.seton.common.data.local.Converters
import com.example.seton.common.presentation.state.ContainerColor
import java.util.Date

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey
    val noteId: Int? = null,
    val title: String,
    val content: String,
    val color: ContainerColor = ContainerColor.Default,
    @TypeConverters(Converters::class)
    val creationDate: Date? = null,
    @TypeConverters(Converters::class)
    val modificationDate: Date? = null,
)

class InvalidNoteException(message: String): Exception(message)


