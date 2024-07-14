@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class)

package com.example.seton.feature_notes.presentation.note_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seton.R
import com.example.seton.feature_notes.presentation.note_list.components.NoteCard
import com.example.seton.feature_notes.presentation.note_list.state.NoteCardState

private const val TAG = "note_list_screen"

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    cardTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onFabClick: () -> Unit,
    onCardClick: (Int) -> Unit,
) {
    val notes = viewModel.noteListState.collectAsState().value.noteList

    NoteListContent(
        notes = notes,
        onFabClick = onFabClick,
        cardTransitionScope = cardTransitionScope,
        animatedContentScope = animatedContentScope
    ) { id ->
        onCardClick(id)
    }
}

@Composable
private fun NoteListContent(
    notes: List<NoteCardState>,
    cardTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onFabClick: () -> Unit,
    onCardClick: (Int) -> Unit,
) {
    val isGridLayout = remember { mutableStateOf(true) }
    val isMenuExpanded = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.notes),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconToggleButton(
                        checked = isGridLayout.value,
                        onCheckedChange = { isGridLayout.value = it }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isGridLayout.value) {
                                    R.drawable.ic_view_agenda
                                } else R.drawable.ic_dashboard
                            ),
                            contentDescription = stringResource(R.string.change_layout),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { isMenuExpanded.value = true }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded.value,
                        onDismissRequest = { isMenuExpanded.value = false },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.fragment_settings))
                            },
                            onClick = { /*TODO*/ },
                            leadingIcon = {
                                Icon(painter = painterResource(R.drawable.ic_settings), null)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.fragment_about))
                            },
                            onClick = { /*TODO*/ },
                            leadingIcon = {
                                Icon(painter = painterResource(R.drawable.ic_info), null)
                            }
                        )
                    }
                }
            )
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
            animatedContentScope = animatedContentScope
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
                    animatedContentScope = animatedContentScope,
                    sharedTransitionScope = cardTransitionScope,
                    modifier = Modifier
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
                    animatedContentScope = animatedContentScope,
                    sharedTransitionScope = cardTransitionScope,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = note.id),
                            animatedVisibilityScope
                        )
                ) { onCardClick(note.id) }
            }
        }
    }
}