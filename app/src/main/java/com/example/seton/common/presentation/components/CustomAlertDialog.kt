package com.example.seton.common.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.seton.R

@Composable
fun DeleteSelectedNotesDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(
                text = stringResource(R.string.delete_notes)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.ask_delete_selected_notes)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.yes)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel)
                )
            }
        }
    )
}