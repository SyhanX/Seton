@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class)

package com.example.seton.feature_notes.presentation.note_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.seton.R
import com.example.seton.common.data.NavDestinations
import com.example.seton.common.domain.util.serialize
import com.example.seton.common.presentation.components.CustomAlertDialog
import com.example.seton.feature_notes.presentation.note_list.components.NoteCard
import com.example.seton.feature_notes.presentation.note_list.components.RegularAppBar
import com.example.seton.feature_notes.presentation.note_list.components.SelectionAppBar
import com.example.seton.feature_notes.presentation.note_list.state.NoteCardState

private const val TAG = "note_list_screen"

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    cardTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onFabClick: () -> Unit,
    navController: NavHostController,
) {
    val notes = viewModel.noteListState.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    NoteListContent(
        notes = notes.value.noteList,
        onFabClick = onFabClick,
        cardTransitionScope = cardTransitionScope,
        animatedContentScope = animatedContentScope,
        onLongCardClick = {
            viewModel.onLongNoteClick(it)
        },
        selectedNotes = notes.value.selectedNoteList,
        onCheckedChange = { isChecked ->
            if (!isChecked) {
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
                val jsonColor = notes.value.noteList[id-1].color.serialize()
                navController.navigate(
                    NavDestinations.EditNoteScreen(
                        currentNoteId = id,
                        currentNoteColor = jsonColor
                    )
                )
            }
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
    cardTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onClear: () -> Unit,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onFabClick: () -> Unit,
    onFillDb: () -> Unit,
    onLongCardClick: (Int) -> Unit,
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
                    isChecked = areListsTheSame.value,
                    onDelete = onDelete
                ) {
                    onCheckedChange(it)
                }
            } else {
                RegularAppBar(
                    isGridLayout = isGridLayout.value,
                    onFillDb = onFillDb
                ) {
                    isGridLayout.value = it
                }
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
        DynamicLazyLayout(
            isGridLayout = isGridLayout.value,
            innerPadding = innerPadding,
            items = notes,
            cardTransitionScope = cardTransitionScope,
            animatedContentScope = animatedContentScope,
            onLongCardClick = { onLongCardClick(it) }
        ) { onCardClick(it) }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DynamicLazyLayout(
    isGridLayout: Boolean,
    innerPadding: PaddingValues,
    items: List<NoteCardState>,
    cardTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onLongCardClick: (Int) -> Unit,
    onCardClick: (Int) -> Unit
) {

    SharedTransitionLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
    ) {
        AnimatedContent(
            targetState = isGridLayout,
            label = "transition"
        ) { targetState ->
            if (targetState) {
                NoteGrid(
                    items = items,
                    onLongCardClick = { onLongCardClick(it) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                    cardTransitionScope = cardTransitionScope,
                    animatedContentScope = animatedContentScope
                ) {
                    onCardClick(it)
                }
            } else {
                NoteList(
                    items = items,
                    onLongCardClick = { onLongCardClick(it) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                    cardTransitionScope = cardTransitionScope,
                    animatedContentScope = animatedContentScope
                ) {
                    onCardClick(it)
                }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun NoteGrid(
    items: List<NoteCardState>,
    sharedTransitionScope: SharedTransitionScope,
    cardTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    animatedContentScope: AnimatedContentScope,
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
            with(sharedTransitionScope) {
                NoteCard(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    isCardChecked = note.isChecked,
                    onLongClick = { onLongCardClick(note.id) },
                    animatedContentScope = animatedContentScope,
                    sharedTransitionScope = cardTransitionScope,
                    color = note.color,
                    modifier = Modifier
                        .animateItem()
                        .sharedElement(
                            rememberSharedContentState(key = note.id),
                            animatedVisibilityScope
                        )
                ) { onCardClick(note.id) }
            }
        }
    }
}

@Composable
fun NoteList(
    items: List<NoteCardState>,
    sharedTransitionScope: SharedTransitionScope,
    cardTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    animatedContentScope: AnimatedContentScope,
    onLongCardClick: (Int) -> Unit,
    onCardClick: (Int) -> Unit
) {
    with(sharedTransitionScope) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = items,
                key = { item: NoteCardState -> item.id },
            ) { note ->
                NoteCard(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    isCardChecked = note.isChecked,
                    onLongClick = { onLongCardClick(note.id) },
                    sharedTransitionScope = cardTransitionScope,
                    animatedContentScope = animatedContentScope,
                    color = note.color,
                    modifier = Modifier
                        .animateItem()
                        .sharedElement(
                            rememberSharedContentState(key = note.id),
                            animatedVisibilityScope
                        )
                ) { onCardClick(note.id) }
            }
        }
    }
}