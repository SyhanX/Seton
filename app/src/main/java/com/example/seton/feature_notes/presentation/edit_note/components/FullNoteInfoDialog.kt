package com.example.seton.feature_notes.presentation.edit_note.components

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.seton.R
import com.example.seton.common.presentation.theme.SetonTheme
import com.example.seton.common.presentation.theme.dynamicTextColor
import java.util.Date

@Composable
fun FullNoteInfoDialog(
    onDismissRequest: () -> Unit,
    color: Color,
    creationDate: Date?,
    modificationDate: Date?,
) {

    val formattedCreationDate = creationDate?.let {
        DateFormat.getDateTimeInstance().format(it)
    } ?: ""

    val formattedModificationDate = modificationDate?.let {
        DateFormat.getDateTimeInstance().format(it)
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color
            ),
            shape = RoundedCornerShape(15)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.note_info),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(Modifier.weight(1f))
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.creation_date),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = formattedCreationDate,
                        fontSize = 18.sp
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = if (formattedModificationDate == null) {
                        Modifier.alpha(0f)
                    } else Modifier
                ) {
                    Text(
                        text = stringResource(R.string.modification_date),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = formattedModificationDate ?: "",
                        fontSize = 18.sp
                    )
                }
                Row {
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = stringResource(R.string.action_close),
                            fontSize = 18.sp,
                            color = dynamicTextColor()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FullDialogPreview() {
    SetonTheme {
        FullNoteInfoDialog(
            onDismissRequest = {},
            creationDate = Date(),
            modificationDate = Date(),
            color = Color.White
        )
    }
}