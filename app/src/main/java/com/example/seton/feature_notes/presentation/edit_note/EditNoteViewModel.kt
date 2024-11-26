package com.example.seton.feature_notes.presentation.edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.common.presentation.state.ContainerColor
import com.example.seton.feature_notes.domain.model.InvalidNoteException
import com.example.seton.feature_notes.domain.model.Note
import com.example.seton.feature_notes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

private const val TAG = "EditNoteViewModel"

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteState = MutableStateFlow(NoteState())
    val noteState = _noteState.asStateFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("currentNoteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteById(noteId)?.also { note ->
                        currentNoteId = note.noteId
                        _noteState.value = noteState.value.copy(
                            id = noteId,
                            title = note.title,
                            content = note.content,
                            color = note.color,
                            creationDate = note.creationDate,
                            modificationDate = note.modificationDate
                        )
                    }
                }
            }
        }
    }

    fun saveTitleState(title: String) {
        _noteState.value = noteState.value.copy(
            title = title
        )
    }

    fun saveContentState(content: String) {
        _noteState.value = noteState.value.copy(
            content = content
        )
    }

    fun saveColorState(color: ContainerColor) {
        _noteState.value = noteState.value.copy(
            color = color
        )
    }

    fun saveCreationDateState() {
        _noteState.value = noteState.value.copy(
            creationDate = Calendar.getInstance().time
        )
    }

    fun saveModificationDateState() {
        _noteState.value = noteState.value.copy(
            modificationDate = Calendar.getInstance().time
        )
    }

    fun saveNote() {
        viewModelScope.launch {
            try {
                noteUseCases.saveNote(
                    Note(
                        title = noteState.value.title,
                        content = noteState.value.content,
                        noteId = currentNoteId,
                        color = noteState.value.color,
                        creationDate = noteState.value.creationDate,
                        modificationDate = noteState.value.modificationDate
                    )
                )
            } catch (e: InvalidNoteException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch {
            noteUseCases.deleteNoteById(id)
        }
    }

    fun deleteNote() {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.deleteNote(
                Note(
                    title = noteState.value.title,
                    content = noteState.value.content,
                    noteId = currentNoteId,
                    creationDate = noteState.value.creationDate,
                    modificationDate = noteState.value.modificationDate
                )
            )
        }
    }
}