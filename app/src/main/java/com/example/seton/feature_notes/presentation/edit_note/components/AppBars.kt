package com.example.seton.feature_notes.presentation.edit_note.components

import android.icu.text.DateFormat
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.seton.R
import com.example.seton.common.presentation.theme.SetonTheme
import com.example.seton.common.presentation.theme.dynamicTextColor
import java.util.Date

private const val TAG = "EditNoteAppBarComponent"

@Composable
fun EditNoteTopBar(
    containerColor: Color,
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor
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
            TextButton(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 18.sp,
                    color = dynamicTextColor()
                )
            }
        },
    )
}

@Composable
fun EditNoteBottomBar(
    onColorSelectorClick: () -> Unit,
    onMoreActionsClick: () -> Unit,
    onShowDateClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    modificationDate: Date?,
) {
    val controller = LocalSoftwareKeyboardController.current
    val formattedShortDate = modificationDate?.let { date ->
        val isModificationDateToday = DateUtils.isToday(modificationDate.time)
        DateFormat
            .getInstanceForSkeleton(
                if (isModificationDateToday) {
                    DateFormat.HOUR24_MINUTE
                } else DateFormat.ABBR_MONTH_DAY
            )
            .format(date)
    }

    BottomAppBar(
        containerColor = containerColor,
        modifier = Modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    controller?.hide()
                    onColorSelectorClick()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_color),
                    contentDescription = null
                )
            }
            if (modificationDate != null && formattedShortDate != null) {
                TextButton(
                    onClick = onShowDateClick
                ) {
                    Text(
                        text = stringResource(R.string.note_edited_at, formattedShortDate),
                        fontSize = 18.sp,
                        color = dynamicTextColor()
                    )
                }
            }
            IconButton(
                onClick = {
                    controller?.hide()
                    onMoreActionsClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            }
        }
    }
}


@Preview
@Composable
private fun BottomBarPreview() {
    SetonTheme {
        EditNoteBottomBar(
            onColorSelectorClick = {},
            onMoreActionsClick = {},
            onShowDateClick = {},
            modificationDate = Date(),
        )
    }
}
