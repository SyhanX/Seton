package com.example.seton.feature_notes.presentation.edit_note.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.R
import com.example.seton.common.presentation.theme.SetonTheme

@Composable
fun EditNoteTopBar(
    containerColor: Color,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 18.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
        )
    )
}

@Composable
fun EditNoteBottomBar(
    containerColor: Color = MaterialTheme.colorScheme.background,
    onAttachmentsClick: () -> Unit,
    onActionsClick: () -> Unit,
) {
    val controller = LocalSoftwareKeyboardController.current

    BottomAppBar(
        containerColor = containerColor,
        modifier = Modifier
            .height(64.dp)
    ) {
        IconButton(
            onClick = {
                controller?.hide()
                onAttachmentsClick()
            }
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
        Spacer(Modifier.weight(1f))
        /*Text(
            text = "Last edited: ",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )*/
        IconButton(
            onClick = {
                controller?.hide()
                onActionsClick()
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreVert, null
            )
        }
    }
}


@Preview
@Composable
private fun BottomBarPreview() {
    SetonTheme {
        EditNoteBottomBar(onAttachmentsClick = {}) {}
    }
}
