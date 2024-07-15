@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.seton.feature_notes.presentation.edit_note

import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.seton.R
import com.example.seton.common.presentation.NoteListRoute
import com.example.seton.feature_notes.data.NoteSharedElementKey
import com.example.seton.feature_notes.data.NoteTextType
import com.example.seton.feature_notes.presentation.components.CustomTextField

private const val TAG = "edit_note_screen"

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    navController: NavHostController
) {
    val context = LocalContext.current
    val note = viewModel.noteState.collectAsState()
    var isTitleBlank by remember { mutableStateOf(true) }
    var isContentBlank by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NoteListRoute) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (note.value.title.isBlank() || note.value.content.isBlank()) {
                                Toast.makeText(
                                    context,
                                    context.getText(R.string.must_fill_all_fields),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.saveNote(
                                    note.value.title,
                                    note.value.content,
                                    note.value.imageFileName
                                )
                                navController.navigate(NoteListRoute)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        with(sharedTransitionScope) {
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                CustomTextField(
                    text = note.value.title,
                    placeholderText = stringResource(R.string.note_title),
                    isSingleLine = true,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    imeAction = ImeAction.Next,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (note.value.title.isNotBlank() || !isTitleBlank) {
                                Modifier.sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(
                                        key = NoteSharedElementKey(
                                            note.value.id,
                                            note.value.title,
                                            NoteTextType.Title
                                        )
                                    ),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            } else Modifier
                        )

                ) {
                    isTitleBlank = it.isBlank()
                    viewModel.saveTitleState(it)
                }
                CustomTextField(
                    text = note.value.content,
                    placeholderText = stringResource(R.string.note_content),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .then(
                            if (note.value.content.isNotBlank() || !isContentBlank) {
                                Modifier.sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(
                                        key = NoteSharedElementKey(
                                            note.value.id,
                                            note.value.content,
                                            NoteTextType.Content
                                        )
                                    ),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            } else Modifier
                        )
                ) {
                    isContentBlank = it.isBlank()
                    viewModel.saveContentState(it)
                }
            }
        }
    }
}