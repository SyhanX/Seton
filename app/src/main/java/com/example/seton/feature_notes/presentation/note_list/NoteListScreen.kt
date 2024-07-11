package com.example.seton.feature_notes.presentation.note_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
    onFabClick: () -> Unit,
    onCardClick: (Int) -> Unit,
) {
    val notes = viewModel.noteListState.collectAsState().value.noteList
    NoteListContent(notes = notes, onFabClick) { id ->
        onCardClick(id)
    }
}

@Composable
private fun NoteListContent(
    notes: List<NoteCardState>,
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
                        onDismissRequest = { isMenuExpanded.value = false }
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
            items = notes
        ) { onCardClick(it) }
    }
}

@Composable
fun DynamicLazyLayout(
    isGridLayout: Boolean,
    innerPadding: PaddingValues,
    items: List<NoteCardState>,
    onCardClick: (Int) -> Unit
) {
    if (isGridLayout) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(130.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(
                items = items,
                key = { item: NoteCardState -> item.id }
            ) { note ->
                NoteCard(
                    title = note.title,
                    content = note.content
                ) { onCardClick(note.id) }
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(
                items = items,
                key = { item: NoteCardState -> item.id }
            ) { note ->
                NoteCard(
                    title = note.title,
                    content = note.content
                ) { onCardClick(note.id) }
            }
        }
    }
}