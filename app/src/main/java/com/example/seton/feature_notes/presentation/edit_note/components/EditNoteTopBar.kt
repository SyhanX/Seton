package com.example.seton.feature_notes.presentation.edit_note.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.seton.R
import com.example.seton.common.presentation.theme.SetonTheme
import com.example.seton.common.presentation.theme.dynamicTextColor

@Composable
fun EditNoteTopBar(
    containerColor: Color,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
) {
    /* This wrapper composable handles smooth color animations */
    Surface(
        color = containerColor
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
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
                TextButton(onClick = onSave) {
                    Text(
                        text = stringResource(R.string.action_save),
                        fontSize = 18.sp,
                        color = dynamicTextColor()
                    )
                }
            },
        )
    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    SetonTheme {
        EditNoteTopBar(
            containerColor = MaterialTheme.colorScheme.background,
            onSave = {},
            onNavigateBack = {}
        )
    }
}