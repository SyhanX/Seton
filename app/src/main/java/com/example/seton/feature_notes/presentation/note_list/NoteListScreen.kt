package com.example.seton.feature_notes.presentation.note_list

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.seton.R
import com.example.seton.common.data.NavDestinations
import com.example.seton.common.presentation.components.CustomAlertDialog
import com.example.seton.feature_notes.presentation.note_list.components.NoteCard
import com.example.seton.feature_notes.presentation.note_list.components.RegularAppBar
import com.example.seton.feature_notes.presentation.note_list.components.SelectionAppBar
import com.example.seton.feature_notes.presentation.note_list.state.NoteCardState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val TAG = "note_list_screen"

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    onFabClick: () -> Unit,
    navController: NavHostController,
) {
    val notes = viewModel.noteListState.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    NoteListContent(
        notes = notes.value.noteList,
        onFabClick = onFabClick,
        onLongCardClick = {
            viewModel.onLongNoteClick(it)
        },
        selectedNotes = notes.value.selectedNoteList,
        selectOrDeselectAll = { isEverythingSelected ->
            if (isEverythingSelected) {
                viewModel.uncheckAllNotes()
            } else {
                viewModel.checkAllNotes()
            }
        },
        onClear = { viewModel.uncheckAllNotes() },
        onDelete = {
            openAlertDialog.value = true
        },
        onFillDb = {
            viewModel.fillDatabase()
        },
        onCardClick = { id ->
            if (notes.value.selectedNoteList.isNotEmpty()) {
                viewModel.onLongNoteClick(id)
            } else {
                val jsonColor = notes.value.noteList.find { note ->
                    note.id == id
                }!!.color.let {
                    Json.encodeToString(it)
                }

                navController.navigate(
                    NavDestinations.EditNoteScreen(
                        currentNoteId = id,
                        currentNoteColor = jsonColor
                    )
                )
            }
        },
        onSend = {
            val firstSelectedNote = notes.value.noteList.find {
                it.isSelected
            } as NoteCardState
            sendNote(
                title = firstSelectedNote.title,
                content = firstSelectedNote.content,
                context = context
            )
        }
    )

    if (openAlertDialog.value) {
        CustomAlertDialog(
            title = R.string.delete_notes,
            text = R.string.ask_delete_selected_notes,
            onDismiss = { openAlertDialog.value = false }
        ) {
            openAlertDialog.value = false
            notes.value.selectedNoteList.forEach { noteId ->
                viewModel.deleteNoteById(noteId)
            }
            viewModel.uncheckAllNotes()
        }
    }
}

@Composable
private fun NoteListContent(
    notes: List<NoteCardState>,
    selectedNotes: Set<Int>,
    onClear: () -> Unit,
    onDelete: () -> Unit,
    selectOrDeselectAll: (Boolean) -> Unit,
    onFabClick: () -> Unit,
    onFillDb: () -> Unit,
    onLongCardClick: (Int) -> Unit,
    onSend: () -> Unit,
    onCardClick: (Int) -> Unit,
) {
    val isGridLayout = rememberSaveable { mutableStateOf(true) }
    val areListsTheSame = rememberSaveable { mutableStateOf(false) }
    areListsTheSame.value = notes.size == selectedNotes.size

    Scaffold(
        topBar = {
            if (selectedNotes.isNotEmpty()) {
                SelectionAppBar(
                    selectedItemCount = selectedNotes.size,
                    onClear = onClear,
                    isEverythingSelected = areListsTheSame.value,
                    onDelete = onDelete,
                    selectOrDeselectAll = {
                        selectOrDeselectAll(it)
                    },
                    onSend = {
                        onSend()
                    }
                )
            } else {
                RegularAppBar(
                    isGridLayout = isGridLayout.value,
                    onFillDb = onFillDb,
                    onCheckedChange = {
                        isGridLayout.value = it
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.create_note)
                )
            }
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.no_notes),
                    fontSize = 20.sp
                )
            }
        } else {
            DynamicLazyLayout(
                isGridLayout = isGridLayout.value,
                innerPadding = innerPadding,
                items = notes,
                onLongCardClick = { onLongCardClick(it) },
                onCardClick = { onCardClick(it) }
            )
        }
    }
}

@Composable
fun DynamicLazyLayout(
    isGridLayout: Boolean,
    innerPadding: PaddingValues,
    items: List<NoteCardState>,
    onLongCardClick: (Int) -> Unit,
    onCardClick: (Int) -> Unit
) {
    AnimatedContent(
        targetState = isGridLayout,
        label = "transition",
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
    ) { targetState ->
        if (targetState) {
            NoteGrid(
                items = items,
                onLongCardClick = { onLongCardClick(it) },
            ) {
                onCardClick(it)
            }
        } else {
            NoteList(
                items = items,
                onLongCardClick = { onLongCardClick(it) },
            ) {
                onCardClick(it)
            }
        }
    }
    Spacer(Modifier.height(32.dp))
}

@Composable
fun NoteGrid(
    items: List<NoteCardState>,
    onLongCardClick: (Int) -> Unit,
    onCardClick: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(130.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
    ) {
        items(
            items = items,
            key = { item: NoteCardState -> item.id }
        ) { note ->
            NoteCard(
                onClick = {
                    onCardClick(note.id)
                },
                title = note.title,
                content = note.content,
                isCardSelected = note.isSelected,
                onLongClick = { onLongCardClick(note.id) },
                color = note.color,
                modifier = Modifier
                    .animateItem()

            )
        }
    }
}

@Composable
fun NoteList(
    items: List<NoteCardState>,
    onLongCardClick: (Int) -> Unit,
    onCardClick: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = items,
            key = { item: NoteCardState -> item.id },
        ) { note ->
            NoteCard(
                title = note.title,
                content = note.content,
                isCardSelected = note.isSelected,
                onLongClick = { onLongCardClick(note.id) },
                color = note.color,
                modifier = Modifier
                    .animateItem()
            ) { onCardClick(note.id) }
        }
    }
}

private fun sendNote(
    title: String,
    content: String,
    context: Context
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            """
            $title
            $content
        """.trimIndent()
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(
        sendIntent,
        null
    )

    context.startActivity(shareIntent)
}