package com.example.seton.feature_notes.presentation.note_list

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

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _noteListState = MutableStateFlow(NoteListState())
    val noteListState = _noteListState.asStateFlow()

    private var getAllNotesJob: Job? = null

    init {
        getAllNotes()
    }

    fun changeLayout() {
        _noteListState.value = noteListState.value.copy(
            isLinearLayout = !noteListState.value.isLinearLayout
        )
    }

    fun deleteAllnotes() {
        viewModelScope.launch {
            noteUseCases.deleteAllNotes()
        }
    }

    fun enableSelectionMode() {
        TODO()
    }

    fun selectNote() {
        TODO()
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
                            onLongClick = { noteId ->
                                onNoteLongClick(noteId)
                            }
                        )
                    }
                )
            }.launchIn(viewModelScope)
    }

    private fun onNoteLongClick(id: Int) {
        _noteListState.value = noteListState.value.copy(
            noteList = noteListState.value.noteList.map { noteItemState ->
                if (id == noteItemState.id) {
                    noteItemState.copy(
                        isChecked = !noteItemState.isChecked
                    )
                } else {
                    noteItemState
                }
            }
        )
    }
}