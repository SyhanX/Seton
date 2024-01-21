package com.example.seton.feature_notes.domain.use_case

import android.util.Log
import com.example.seton.feature_notes.data.model.Note
import com.example.seton.feature_notes.data.repository.NoteRepository


private const val TAG = "case: getnotebyid"

class GetNoteById(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int) : Note? {
        Log.d(TAG, "invoke: current note id is: $id")
        return repository.getNoteById(id)
    }
}