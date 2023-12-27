package com.example.seton.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.domain.use_case.NoteUseCases
import com.example.seton.presentation.note_list.state.NoteEvent
import com.example.seton.presentation.note_list.state.NoteItemState
import com.example.seton.presentation.note_list.state.NoteListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState>
        get() = _state

    private var getAllNotesJob: Job? = null

    init {
        getAllNotes()
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.ChangeLayout -> {
                _state.value = state.value.copy(
                    isLinearLayout = !state.value.isLinearLayout
                )
            }
            is NoteEvent.EnableCheckMode -> {
                _state.value = state.value.copy(
                    isCheckMode = !state.value.isCheckMode
                )
            }
            is NoteEvent.SelectNote -> {

            }
        }
    }

    private fun getAllNotes() {
        getAllNotesJob?.cancel()
        getAllNotesJob = noteUseCases.getAllNotes()
            .onEach { noteList ->
                _state.value = state.value.copy(
                    noteList = noteList.map {  note ->
                        NoteItemState(
                            id = note.id?: 0,
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
        _state.value = state.value.copy(
            noteList = state.value.noteList.map {  noteItemState ->
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