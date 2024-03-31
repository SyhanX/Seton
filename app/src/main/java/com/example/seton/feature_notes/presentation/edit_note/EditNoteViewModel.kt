package com.example.seton.feature_notes.presentation.edit_note

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.common.domain.util.StorageManager
import com.example.seton.feature_notes.domain.model.InvalidNoteException
import com.example.seton.feature_notes.domain.model.Note
import com.example.seton.feature_notes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "editnotevm"

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
                            title = note.title,
                            content = note.content,
                            imageFileName = note.imageFileName
                        )
                    }
                }
            }
        }
    }

    fun saveNote(
        title: String,
        content: String,
        imageFileName: String?
    ) {
        viewModelScope.launch {
            try {
                noteUseCases.upsertNote(
                    Note(
                        title = title,
                        content = content,
                        imageFileName = imageFileName ?: _noteState.value.imageFileName,
                        noteId = currentNoteId
                    )
                )
            } catch (e: InvalidNoteException) {
                Log.e(TAG, "Error saving note.")
            }
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch {
            noteUseCases.deleteNoteById(id)
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            noteUseCases.deleteNote(
                Note(
                    title = noteState.value.title,
                    content = noteState.value.content,
                    imageFileName = noteState.value.imageFileName,
                    noteId = currentNoteId
                )
            )
        }
    }

    fun saveBitmapToDevice(context: Context, bitmap: Bitmap?, fileName: String) {
        if (bitmap != null) {
            StorageManager.saveToInternalStorage(
                context = context,
                bitmapImage = bitmap,
                imageFileName = fileName
            )
            Log.i(TAG, "Saved bitmap: $bitmap")
        } else {
            Log.e(TAG, "Couldn't save bitmap because its value is NULL")
        }
    }

    fun getBitmapFromDevice(context: Context, fileName: String?): Bitmap? {
        return if (fileName != null) {
            try {
                Log.i(TAG, "Got bitmap successfully: $fileName")
                StorageManager.getImageFromInternalStorage(
                    context = context,
                    imageFileName = fileName
                )
            } catch (exception: Exception) {
                Log.e(TAG, "Couldn't get bitmap $fileName because it doesn't exist.")
                null
            }
        } else {
            Log.e(TAG, "Couldn't get bitmap because it doesn't exist.")
            null
        }
    }

    fun deleteBitmapFromDevice(context: Context, fileName: String?) {
        if (fileName != null) {
            try {
                StorageManager.deleteImageFromInternalStorage(
                    context = context,
                    imageFileName = fileName
                )
                Log.i(TAG, "Deleted bitmap successfully.")
            } catch (exception: IllegalArgumentException) {
                Log.e(TAG, "Couldn't delete bitmap $fileName because it doesn't exist.")
                return
            }
        } else {
            Log.e(TAG, "Couldn't delete bitmap because it doesn't exist.")
        }
    }
}