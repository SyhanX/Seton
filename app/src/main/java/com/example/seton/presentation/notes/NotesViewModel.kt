package com.example.seton.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.domain.use_case.NoteUseCases
import com.example.seton.domain.util.NoteListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
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
        when(event) {
            is NoteEvent.ChangeLayout -> {
                _state.value = state.value.copy(
                    isLinearLayout = !state.value.isLinearLayout
                )
            }
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                }
            }
        }
    }

    private fun getAllNotes() {
        getAllNotesJob?.cancel()
        getAllNotesJob = noteUseCases.getAllNotes()
            .onEach { notes ->
                _state.value = state.value.copy(
                    noteList = notes
                )
            }.launchIn(viewModelScope)
    }
}