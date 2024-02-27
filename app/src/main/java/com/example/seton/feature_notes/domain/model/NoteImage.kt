package com.example.seton.feature_notes.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_image_table")
data class NoteImage(
    @PrimaryKey
    val imageId: Int? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,
    val parentNoteId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteImage

        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int = image.contentHashCode()
}
