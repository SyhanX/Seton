package com.example.seton.presentation.edit_note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.domain.model.InvalidNoteException
import com.example.seton.domain.model.Note
import com.example.seton.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "editnotevm"

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = MutableStateFlow(NoteTextFieldState())
    val noteTitle: StateFlow<NoteTextFieldState>
        get() = _noteTitle

    private val _noteContent = MutableStateFlow(NoteTextFieldState())
    val noteContent: StateFlow<NoteTextFieldState>
        get() = _noteContent

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("currentNoteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteById(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.EnterTitle -> {
                _noteTitle.value = noteContent.value.copy(
                    text = event.title
                )
            }

            is EditNoteEvent.EnterContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.content
                )
            }

            is EditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.upsertNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                id = currentNoteId
                            )
                        )
                    } catch (e: InvalidNoteException) {
                        Log.d(TAG, "onEvent: Invalid note")
                    }
                }
            }
        }
    }
}