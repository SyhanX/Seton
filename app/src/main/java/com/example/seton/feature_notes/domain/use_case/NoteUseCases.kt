package com.example.seton.feature_notes.domain.use_case

data class NoteUseCases(
    val saveNote: SaveNote,
    val deleteNote: DeleteNote,
    val deleteAllNotes: DeleteAllNotes,
    val getNoteById: GetNoteById,
    val getAllNotes: GetAllNotes,
    val deleteNoteById: DeleteNoteById
)