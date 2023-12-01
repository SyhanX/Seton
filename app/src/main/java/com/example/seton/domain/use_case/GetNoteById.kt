package com.example.seton.domain.use_case

import android.util.Log
import com.example.seton.domain.model.Note
import com.example.seton.domain.repository.NoteRepository


private const val TAG = "case: getnotebyid"

class GetNoteById(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int) : Note? {
        Log.d(TAG, "invoke: current note id is: $id")
        return repository.getNoteById(id)
    }
}