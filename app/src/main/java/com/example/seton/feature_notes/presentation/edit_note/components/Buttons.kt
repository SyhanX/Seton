package com.example.seton.feature_notes.presentation.edit_note.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.seton.R

@Composable
fun ColorButton(
    isSelected: Boolean,
    lightColor: Color,
    darkColor: Color,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) {
                    darkColor
                } else lightColor
            ),
            border = if (isSelected) {
                if (isSystemInDarkTheme()) {
                    BorderStroke(2.dp, Color.White)
                } else BorderStroke(2.dp, Color.Black)
            } else null,
            modifier = Modifier.size(40.dp)
        ) {
        }
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_done),
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) {
                    Color.White
                } else Color.Black,
            )
        }
    }
}