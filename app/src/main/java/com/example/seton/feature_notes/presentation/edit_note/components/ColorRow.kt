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
import com.example.seton.common.presentation.theme.blueDark
import com.example.seton.common.presentation.theme.blueLight
import com.example.seton.common.presentation.theme.brownDark
import com.example.seton.common.presentation.theme.brownLight
import com.example.seton.common.presentation.theme.darkBlueDark
import com.example.seton.common.presentation.theme.darkBlueLight
import com.example.seton.common.presentation.theme.defaultColorDark
import com.example.seton.common.presentation.theme.defaultColorLight
import com.example.seton.common.presentation.theme.greenDark
import com.example.seton.common.presentation.theme.greenLight
import com.example.seton.common.presentation.theme.greyDark
import com.example.seton.common.presentation.theme.greyLight
import com.example.seton.common.presentation.theme.orangeDark
import com.example.seton.common.presentation.theme.orangeLight
import com.example.seton.common.presentation.theme.pinkDark
import com.example.seton.common.presentation.theme.pinkLight
import com.example.seton.common.presentation.theme.purpleDark
import com.example.seton.common.presentation.theme.purpleLight
import com.example.seton.common.presentation.theme.redDark
import com.example.seton.common.presentation.theme.redLight
import com.example.seton.common.presentation.theme.turquoiseDark
import com.example.seton.common.presentation.theme.turquoiseLight
import com.example.seton.common.presentation.theme.yellowDark
import com.example.seton.common.presentation.theme.yellowLight

@Composable
fun ColorsRow(
    selectedColor: SelectedColor,
    onClick: (SelectedColor) -> Unit,
) {
    val colorList = listOf(
        SelectedColor.Default,
        SelectedColor.Red,
        SelectedColor.Orange,
        SelectedColor.Yellow,
        SelectedColor.Green,
        SelectedColor.Turquoise,
        SelectedColor.Blue,
        SelectedColor.DarkBlue,
        SelectedColor.Purple,
        SelectedColor.Pink,
        SelectedColor.Brown,
        SelectedColor.Grey,
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = colorList,
        ) { color ->
            ColorButton(
                isSelected = color == selectedColor,
                lightColor = color.light,
                darkColor = color.dark
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

enum class SelectedColor(val light: Color, val dark: Color) {
    Default(defaultColorLight, defaultColorDark),
    Red(redLight, redDark),
    Orange(orangeLight, orangeDark),
    Yellow(yellowLight, yellowDark),
    Green(greenLight, greenDark),
    Turquoise(turquoiseLight, turquoiseDark),
    Blue(blueLight, blueDark),
    DarkBlue(darkBlueLight, darkBlueDark),
    Purple(purpleLight, purpleDark),
    Pink(pinkLight, pinkDark),
    Brown(brownLight, brownDark),
    Grey(greyLight, greyDark)
}
