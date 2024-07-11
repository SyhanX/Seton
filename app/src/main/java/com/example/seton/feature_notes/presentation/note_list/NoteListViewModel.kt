package com.example.seton.feature_notes.presentation.note_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun changeLayout() {
        _noteListState.value = noteListState.value.copy(
            isGridLayout = !noteListState.value.isGridLayout
        )
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteUseCases.deleteAllNotes()
        }
    }

    fun checkAllNotes() {
        _noteListState.value.noteList.forEach {
            if (!it.isChecked) {
                checkOrUncheckNote(it.id)
            }
        }
    }

    fun uncheckAllNotes() {
        selectedNotesList.clear()
        _noteListState.value = noteListState.value.copy(
            noteList = noteListState.value.noteList.map { cardState ->
                cardState.copy(isChecked = false)
            },
            selectedNoteList = selectedNotesList
        )
        Log.d(TAG, "SELECTED NOTES: $selectedNotesList")
    }

    private fun checkOrUncheckNote(noteId: Int) {
        _noteListState.value = noteListState.value.copy(
            noteList = noteListState.value.noteList.map { cardState ->
                if (noteId == cardState.id) {
                    cardState.copy(isChecked = !cardState.isChecked).also {
                        if (it.isChecked) {
                            selectedNotesList.add(it.id)
                        } else {
                            selectedNotesList.remove(it.id)
                        }
                    }
                } else cardState
            },
            selectedNoteList = selectedNotesList
        )
        Log.d(TAG, "SELECTED NOTES: $selectedNotesList")
    }


    private fun onLongNoteClick(noteId: Int) {
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
                            isChecked = false,
                            title = note.title,
                            content = note.content,
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
}