package com.example.seton.feature_notes.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithImages(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "parentNoteId"
    )
    val images: List<NoteImage>
)
