package com.example.seton.feature_notes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.feature_notes.domain.model.fakeNotes
import com.example.seton.feature_notes.domain.use_case.NoteUseCases
import com.example.seton.feature_notes.presentation.note_list.state.NoteCardState
import com.example.seton.feature_notes.presentation.note_list.state.NoteListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NoteListVM"

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _noteListState = MutableStateFlow(NoteListState())
    val noteListState = _noteListState.asStateFlow()

    private var getAllNotesJob: Job? = null

    private val selectedNotesList = mutableSetOf<Int>()

    init {
        getAllNotes()
    }

    fun checkAllNotes() {
        _noteListState.value.noteList.forEach {
            if (!it.isSelected) {
                checkOrUncheckNote(it.id)
            }
        }
    }

    fun uncheckAllNotes() {
        selectedNotesList.clear()
        _noteListState.value = noteListState.value.copy(
            noteList = noteListState.value.noteList.map { cardState ->
                cardState.copy(isSelected = false)
            },
            selectedNoteList = selectedNotesList
        )
    }

    private fun checkOrUncheckNote(noteId: Int) {
        _noteListState.value = noteListState.value.copy(
            noteList = noteListState.value.noteList.map { cardState ->
                if (noteId == cardState.id) {
                    cardState.copy(isSelected = !cardState.isSelected).also {
                        if (it.isSelected) {
                            selectedNotesList.add(it.id)
                        } else {
                            selectedNotesList.remove(it.id)
                        }
                    }
                } else cardState
            },
            selectedNoteList = selectedNotesList
        )
    }


    fun onLongNoteClick(noteId: Int) {
        checkOrUncheckNote(noteId)
    }

    private fun getAllNotes() {
        getAllNotesJob?.cancel()
        getAllNotesJob = noteUseCases.getAllNotes()
            .onEach { noteList ->
                _noteListState.value = noteListState.value.copy(
                    noteList = noteList.map { note ->
                        NoteCardState(
                            id = note.noteId ?: 0,
                            isSelected = false,
                            title = note.title,
                            content = note.content,
                            color = note.color,
                            onClick = { id, customAction ->
                                if (selectedNotesList.isNotEmpty()) {
                                    checkOrUncheckNote(id)
                                } else {
                                    customAction()
                                }
                            },
                            onLongClick = { id ->
                                onLongNoteClick(id)
                            }
                        )
                    }
                )
            }.launchIn(viewModelScope)
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch {
            noteUseCases.deleteNoteById(id)
        }
    }

    fun fillDatabase() {
        viewModelScope.launch {
            fakeNotes.shuffled().forEach {
                noteUseCases.saveNote(it)
            }
        }
    }
}