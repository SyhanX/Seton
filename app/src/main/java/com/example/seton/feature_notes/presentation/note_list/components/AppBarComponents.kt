package com.example.seton.feature_notes.presentation.note_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R

@Composable
fun RegularAppBar(
    onFillDb: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    isGridLayout: Boolean,
) {
    val isMenuExpanded = remember { mutableStateOf(false) }

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
                checked = isGridLayout,
                onCheckedChange = { onCheckedChange(it) }
            ) {
                Icon(
                    painter = painterResource(
                        if (isGridLayout) {
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
                        Text(text = stringResource(R.string.settings))
                    },
                    onClick = { /*TODO*/ },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.ic_settings), null)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.about))
                    },
                    onClick = { /*TODO*/ },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.ic_info), null)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.prepopulate_db)
                        )
                    },
                    onClick = onFillDb,
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.ic_bug), null)
                    }
                )
            }
        }
    )
}

@Composable
fun SelectionAppBar(
    selectedItemCount: Int,
    onClear: () -> Unit,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    isChecked: Boolean,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.selected_items_count, selectedItemCount),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onClear) {
                Icon(imageVector = Icons.Rounded.Clear, null)
            }
        },
        actions = {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) }
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    null
                )
            }
        }
    )
}