@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.seton.feature_notes.presentation.edit_note

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.seton.R
import com.example.seton.common.data.NavDestinations
import com.example.seton.common.presentation.components.CustomAlertDialog
import com.example.seton.feature_notes.data.NoteSharedElementKey
import com.example.seton.feature_notes.data.NoteTextType
import com.example.seton.feature_notes.presentation.edit_note.components.ActionsBottomSheet
import com.example.seton.feature_notes.presentation.edit_note.components.AttachmentsBottomSheet
import com.example.seton.feature_notes.presentation.edit_note.components.CustomTextField
import com.example.seton.feature_notes.presentation.edit_note.components.EditNoteBottomBar
import com.example.seton.feature_notes.presentation.edit_note.components.EditNoteTopBar
import com.example.seton.feature_notes.presentation.edit_note.components.SelectedColor

private const val TAG = "edit_note_screen"

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    navController: NavHostController
) {
    val note = viewModel.noteState.collectAsStateWithLifecycle()
    Log.d(TAG, "EditNoteScreen: ${note.value.color}")
    with(sharedTransitionScope) {
        EditNoteContent(
            sharedElementModifier = {
                Modifier.sharedElement(
                    sharedTransitionScope.rememberSharedContentState(it),
                    animatedVisibilityScope = animatedContentScope
                )
            },
            note = note.value,
            navigateUp = { navController.navigateUp() },
            navigateToNoteList = {
                navController.navigate(NavDestinations.NoteListScreen)
            },
            saveNote = {
                viewModel.saveNote(
                    note.value.title,
                    note.value.content,
                    note.value.imageFileName,
                    note.value.color
                )
            },
            deleteNote = {
                viewModel.deleteNote()
            },
            saveState = { type ->
                when (type) {
                    is NoteStateType.Title -> {
                        viewModel.saveTitleState(type.title)
                    }

                    is NoteStateType.Content -> {
                        viewModel.saveContentState(type.content)
                    }

                    is NoteStateType.Color -> {
                        viewModel.saveColorState(type.color)
                    }
                }
            }
        )
    }
}

@Composable
fun EditNoteContent(
    sharedElementModifier: @Composable (NoteSharedElementKey) -> Modifier,
    note: NoteState,
    navigateUp: () -> Unit,
    navigateToNoteList: () -> Unit,
    saveNote: () -> Unit,
    deleteNote: () -> Unit,
    saveState: (NoteStateType) -> Unit,
) {
    val context = LocalContext.current
    var isTitleBlank by remember { mutableStateOf(true) }
    var isContentBlank by remember { mutableStateOf(true) }
    var showAttachmentsBottomSheet by remember { mutableStateOf(false) }
    var showActionsBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            EditNoteTopBar(
                containerColor = getDayNightDynamicBackground(note.color),
                onNavigateBack = navigateUp,
                onSave = {
                    if (note.title.isBlank() || note.content.isBlank()) {
                        Toast.makeText(
                            context,
                            context.getText(R.string.must_fill_all_fields),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        saveNote()
                        navigateToNoteList()
                    }
                }
            )
        },
        bottomBar = {
            EditNoteBottomBar(
                containerColor = getDayNightDynamicBackground(selectedColor = note.color),
                onAttachmentsClick = {
                    showAttachmentsBottomSheet = true
                },
                onActionsClick = {
                    showActionsBottomSheet = true
                }
            )
        }
    ) { padding ->
        if (showAttachmentsBottomSheet) {
            AttachmentsBottomSheet(
                onDismissRequest = { showAttachmentsBottomSheet = false },
                selectedColor = note.color,
                containerColor = getDayNightDynamicBackground(note.color)
            ) { saveState(NoteStateType.Color(it)) }
        }
        if (showActionsBottomSheet) {
            ActionsBottomSheet(
                onDismissRequest = { showActionsBottomSheet = false },
                containerColor = getDayNightDynamicBackground(note.color)
            ) { showDialog = true }
        }
        if (showDialog) {
            CustomAlertDialog(
                title = R.string.confirm_action,
                text = R.string.ask_delete_note,
                onDismiss = { showDialog = false }
            ) {
                navigateUp()
                deleteNote()
            }
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .background(getDayNightDynamicBackground(note.color))
        ) {
            CustomTextField(
                text = note.title,
                placeholderText = stringResource(R.string.note_title),
                isSingleLine = true,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (note.title.isNotBlank() || !isTitleBlank) {
                            sharedElementModifier(
                                NoteSharedElementKey(
                                    note.id,
                                    note.title,
                                    NoteTextType.Title
                                )
                            )
                        } else Modifier
                    )

            ) {
                isTitleBlank = it.isBlank()
                saveState(NoteStateType.Title(it))
            }
            CustomTextField(
                text = note.content,
                placeholderText = stringResource(R.string.note_content),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .then(
                        if (note.content.isNotBlank() || !isContentBlank) {
                            sharedElementModifier(
                                NoteSharedElementKey(
                                    note.id,
                                    note.content,
                                    NoteTextType.Content
                                )
                            )
                        } else Modifier
                    )
            ) {
                isContentBlank = it.isBlank()
                saveState(NoteStateType.Content(it))
            }
        }
    }
}

@Composable
fun getDayNightDynamicBackground(selectedColor: SelectedColor): Color {
    return if (isSystemInDarkTheme()) {
        getDarkDynamicBackground(color = selectedColor)
    } else {
        getLightDynamicBackground(color = selectedColor)
    }
}

@Composable
fun getDarkDynamicBackground(color: SelectedColor): Color {
    val darkBackgroundColor by animateColorAsState(
        targetValue = when (color) {
            SelectedColor.Default -> SelectedColor.Default.dark
            SelectedColor.Red -> SelectedColor.Red.dark
            SelectedColor.Orange -> SelectedColor.Orange.dark
            SelectedColor.Yellow -> SelectedColor.Yellow.dark
            SelectedColor.Green -> SelectedColor.Green.dark
            SelectedColor.Turquoise -> SelectedColor.Turquoise.dark
            SelectedColor.Blue -> SelectedColor.Blue.dark
            SelectedColor.DarkBlue -> SelectedColor.DarkBlue.dark
            SelectedColor.Purple -> SelectedColor.Purple.dark
            SelectedColor.Pink -> SelectedColor.Pink.dark
            SelectedColor.Brown -> SelectedColor.Brown.dark
            SelectedColor.Grey -> SelectedColor.Grey.dark
        },
        label = "darkBackground",
        animationSpec = tween()
    )
    return darkBackgroundColor
}

@Composable
fun getLightDynamicBackground(color: SelectedColor): Color {
    val lightBackgroundColor by animateColorAsState(
        targetValue = when (color) {
            SelectedColor.Default -> SelectedColor.Default.light
            SelectedColor.Red -> SelectedColor.Red.light
            SelectedColor.Orange -> SelectedColor.Orange.light
            SelectedColor.Yellow -> SelectedColor.Yellow.light
            SelectedColor.Green -> SelectedColor.Green.light
            SelectedColor.Turquoise -> SelectedColor.Turquoise.light
            SelectedColor.Blue -> SelectedColor.Blue.light
            SelectedColor.DarkBlue -> SelectedColor.DarkBlue.light
            SelectedColor.Purple -> SelectedColor.Purple.light
            SelectedColor.Pink -> SelectedColor.Pink.light
            SelectedColor.Brown -> SelectedColor.Brown.light
            SelectedColor.Grey -> SelectedColor.Grey.light
        },
        label = "lightBackground"
    )
    return lightBackgroundColor
}

@Preview
@Composable
private fun EditNotePreview() {
    EditNoteContent(
        note = NoteState(
            id = 0,
            title = "Lorem ipsum",
            content = "Dolor sit amet",
            color = SelectedColor.DarkBlue
        ),
        navigateUp = { /*TODO*/ },
        navigateToNoteList = { /*TODO*/ },
        saveNote = { /*TODO*/ },
        deleteNote = { /*TODO*/ },
        sharedElementModifier = { Modifier },
        saveState = { }
    )
}