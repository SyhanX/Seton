package com.example.seton.common.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.seton.R

@Composable
fun CustomAlertDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    @StringRes positiveText: Int = R.string.yes,
    @StringRes negativeText: Int = R.string.cancel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(title)
            )
        },
        text = {
            Text(
                text = stringResource(text),
                fontSize = 18.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(positiveText),
                    fontSize = 16.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(negativeText),
                    fontSize = 16.sp
                )
            }
        }
    )
}