package com.example.seton.feature_notes.presentation.edit_note.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.seton.common.presentation.state.ContainerColor

@Composable
fun ColorsRow(
    containerColor: ContainerColor,
    onClick: (ContainerColor) -> Unit,
) {
    val colors = enumValues<ContainerColor>().toList()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = colors,
        ) { color ->
            ColorButton(
                isSelected = color == containerColor,
                lightColor = color.lightVariant,
                darkColor = color.darkVariant
            ) {
                onClick(color)
            }
        }
    }
}

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