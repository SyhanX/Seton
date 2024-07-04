package com.example.seton.feature_notes.presentation.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seton.R

private const val TAG = "edit_note_screen"

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val note = viewModel.noteState.collectAsState()
    EditNoteContent(
        title = note.value.title,
        content = note.value.content,
        onNavigateBack = onNavigateBack
    ) {
        viewModel.saveNote(
            title = it.title,
            content = it.content,
            imageFileName = it.imageFileName
        )
    }
}

@Composable
private fun EditNoteContent(
    title: String,
    content: String,
    onNavigateBack: () -> Unit,
    onFabCLick: (NoteState) -> Unit
) {
    val titleState = remember { mutableStateOf("") }
    val contentState = remember { mutableStateOf("") }
    if (title.isNotBlank() && content.isNotBlank()) {
        titleState.value = title
        contentState.value = content
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFabCLick(
                        NoteState(
                            titleState.value,
                            contentState.value
                        )
                    )
                    onNavigateBack()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = stringResource(R.string.save_note),
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
        ) {
            TextField(
                value = titleState.value,
                onValueChange = { titleState.value = it }
            )

            TextField(
                value = contentState.value,
                onValueChange = { contentState.value = it }
            )
        }
    }
}