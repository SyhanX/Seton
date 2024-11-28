package com.example.seton.feature_notes.presentation.edit_note

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import com.example.seton.common.presentation.state.ContainerColor
import com.example.seton.feature_notes.presentation.edit_note.components.ColorsBottomSheet
import com.example.seton.feature_notes.presentation.edit_note.components.EditNoteBottomBar
import com.example.seton.feature_notes.presentation.edit_note.components.EditNoteTextField
import com.example.seton.feature_notes.presentation.edit_note.components.EditNoteTopBar
import com.example.seton.feature_notes.presentation.edit_note.components.FullNoteInfoDialog
import com.example.seton.feature_notes.presentation.edit_note.components.MoreActionsBottomSheet

private const val TAG = "EditNoteScreen"

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    navController: NavHostController,
    noteColor: ContainerColor
) {
    val note = viewModel.noteState.collectAsStateWithLifecycle()

    EditNoteContent(
        note = note.value,
        noteColor = noteColor,
        navigateUp = { navController.navigateUp() },
        navigateToNoteList = { navController.navigate(NavDestinations.NoteListScreen) },
        saveNote = {
            if (note.value.creationDate == null) {
                viewModel.saveCreationDateState()
            } else {
                viewModel.saveModificationDateState()
            }
            viewModel.saveNote()
        },
        deleteNote = { viewModel.deleteNote() },
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

@Composable
fun EditNoteContent(
    note: NoteState,
    noteColor: ContainerColor,
    navigateUp: () -> Unit,
    navigateToNoteList: () -> Unit,
    saveNote: () -> Unit,
    deleteNote: () -> Unit,
    saveState: (NoteStateType) -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var isTitleBlank by remember { mutableStateOf(true) }
    var isContentBlank by remember { mutableStateOf(true) }

    var showAttachmentsBottomSheet by remember { mutableStateOf(false) }
    var showActionsBottomSheet by remember { mutableStateOf(false) }
    var showDeleteNoteDialog by remember { mutableStateOf(false) }
    var showFullDateDialog by remember { mutableStateOf(false) }
    var containerColor by remember { mutableStateOf<ContainerColor>(noteColor) }

    val color by animateColorAsState(
        targetValue = if (isSystemInDarkTheme()) {
            containerColor.darkVariant
        } else containerColor.lightVariant,
        label = "containerColor"
    )

    Scaffold(
        topBar = {
            EditNoteTopBar(
                containerColor = color,
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
                containerColor = color,
                onColorSelectorClick = {
                    showAttachmentsBottomSheet = true
                },
                onMoreActionsClick = {
                    showActionsBottomSheet = true
                },
                onShowDateClick = { showFullDateDialog = true },
                modificationDate = note.modificationDate ?: note.creationDate
            )
        }
    ) { padding ->
        if (showAttachmentsBottomSheet) {
            ColorsBottomSheet(
                onDismissRequest = { showAttachmentsBottomSheet = false },
                selectedColor = note.color,
                containerColor = color,
                onColorClick = {
                    saveState(NoteStateType.Color(it))
                    containerColor = it
                }
            )
        }
        if (showActionsBottomSheet) {
            MoreActionsBottomSheet(
                containerColor = color,
                onDismissRequest = { showActionsBottomSheet = false },
                onDeleteNote = { showDeleteNoteDialog = true },
                onCopyNote = {
                    clipboardManager.setText(
                        AnnotatedString("${note.title}\n\n${note.content}")
                    )
                    Toast.makeText(
                        context,
                        context.getText(R.string.copied_to_clipboard),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        if (showDeleteNoteDialog) {
            CustomAlertDialog(
                title = R.string.confirm_action,
                text = R.string.ask_delete_note,
                onDismiss = { showDeleteNoteDialog = false }
            ) {
                navigateUp()
                deleteNote()
            }
        }
        if (showFullDateDialog) {
            FullNoteInfoDialog(
                onDismissRequest = { showFullDateDialog = false },
                color = color,
                creationDate = note.creationDate,
                modificationDate = note.modificationDate
            )
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .background(color)
        ) {
            EditNoteTextField(
                text = note.title,
                placeholderText = stringResource(R.string.note_title),
                isSingleLine = true,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                isTitleBlank = it.isBlank()
                saveState(NoteStateType.Title(it))
            }
            EditNoteTextField(
                text = note.content,
                placeholderText = stringResource(R.string.note_content),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                isContentBlank = it.isBlank()
                saveState(NoteStateType.Content(it))
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun EditNotePreview() {
    EditNoteContent(
        note = NoteState(
            id = 0,
            title = "Lorem ipsum",
            content = "Dolor sit amet",
            color = ContainerColor.Red
        ),
        noteColor = ContainerColor.Red,
        navigateUp = { /*TODO*/ },
        navigateToNoteList = { /*TODO*/ },
        saveNote = { /*TODO*/ },
        deleteNote = { /*TODO*/ },
        saveState = { }
    )
}