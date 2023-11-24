package com.example.seton.domain.use_case

data class NoteUseCases(
    val upsertNote: UpsertNote,
    val deleteNote: DeleteNote,
    val getNoteById: GetNoteById,
    val getAllNotes: GetAllNotes
)