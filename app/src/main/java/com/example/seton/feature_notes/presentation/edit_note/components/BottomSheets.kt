package com.example.seton.feature_notes.presentation.edit_note.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R
import com.example.seton.common.presentation.state.ContainerColor
import com.example.seton.common.presentation.theme.dynamicTextColor

@Composable
fun ColorsBottomSheet(
    containerColor: Color,
    onDismissRequest: () -> Unit,
    selectedColor: ContainerColor,
    onColorClick: (ContainerColor) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { },
        containerColor = containerColor,
        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.select_color),
                fontSize = 20.sp,
                color = dynamicTextColor(),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(24.dp))
            ColorsRow(selectedColor) { onColorClick(it) }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun MoreActionsBottomSheet(
    onDismissRequest: () -> Unit,
    onDeleteNote: () -> Unit,
    onCopyNote: () -> Unit,
    onSendNote: () -> Unit,
    containerColor: Color,
    showDeleteNoteOption: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { },
        containerColor = containerColor,
        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            if (showDeleteNoteOption) {
                BottomSheetMenuItem(
                    drawableRes = R.drawable.ic_rounded_delete,
                    textRes = R.string.delete,
                    onClick = onDeleteNote
                )
            }
            BottomSheetMenuItem(
                drawableRes = R.drawable.ic_send,
                textRes = R.string.send,
                onClick = onSendNote
            )
            BottomSheetMenuItem(
                drawableRes = R.drawable.ic_copy,
                textRes = R.string.copy,
                onClick = onCopyNote
            )
        }
    }
}

@Composable
fun BottomSheetMenuItem(
    onClick: () -> Unit,
    @DrawableRes drawableRes: Int,
    @StringRes textRes: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Spacer(Modifier.width(16.dp))
        Icon(painter = painterResource(drawableRes), contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(text = stringResource(textRes))
    }
}