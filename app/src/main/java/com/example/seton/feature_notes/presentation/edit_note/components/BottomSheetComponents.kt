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
import androidx.compose.ui.unit.dp
import com.example.seton.R

@Composable
fun AttachmentsBottomSheet(
    containerColor: Color,
    onDismissRequest: () -> Unit,
    selectedColor: SelectedColor,
    onColorClick: (SelectedColor) -> Unit,
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
            ColorsRow(selectedColor) { onColorClick(it) }
            RegularBottomSheetItem(icon = R.drawable.ic_rounded_image, text = R.string.add_image) {

            }
            RegularBottomSheetItem()
            RegularBottomSheetItem()
        }
    }
}

@Composable
fun ActionsBottomSheet(
    containerColor: Color,
    onDismissRequest: () -> Unit,
    onDeleteNote: () -> Unit,
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
            RegularBottomSheetItem(icon = R.drawable.ic_rounded_delete, text = R.string.delete) {
                onDeleteNote()
            }
            RegularBottomSheetItem(
                icon = R.drawable.ic_copy,
                text = R.string.copy
            )
        }
    }
}

@Composable
fun RegularBottomSheetItem(
    @DrawableRes icon: Int = R.drawable.ic_bug,
    @StringRes text: Int = R.string.app_name,
    onClick: () -> Unit = {},
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
        Icon(painter = painterResource(icon), contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(text = stringResource(text))
    }
}