package com.example.seton.feature_notes.presentation.note_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R

@Composable
fun NoteListAppBar(
    onFillDb: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    isGridLayout: Boolean,
) {
    var showActionsMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.note_edit_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
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
                    contentDescription = stringResource(R.string.action_change_layout),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { showActionsMenu = true }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = showActionsMenu,
                onDismissRequest = { showActionsMenu = false },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.action_fill_db),
                            fontSize = 16.sp
                        )
                    },
                    onClick = onFillDb,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_bug),
                            null
                        )
                    }
                )
            }
        }
    )
}