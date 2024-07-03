package com.example.seton.feature_notes.presentation.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seton.R

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    EditNoteContent(onNavigateBack) {
        viewModel.saveNote(
            title = it.title,
            content = it.content,
            imageFileName = it.imageFileName
        )
    }
}

@Composable
private fun EditNoteContent(
    onNavigateBack: () -> Unit,
    onFabCLick: (NoteState) -> Unit
) {
    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { /*TODO*/ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFabCLick(
                        NoteState(
                            title.value,
                            content.value
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
                value = title.value,
                onValueChange = { title.value = it }
            )

            TextField(
                value = content.value,
                onValueChange = { content.value = it }
            )
        }
    }
}