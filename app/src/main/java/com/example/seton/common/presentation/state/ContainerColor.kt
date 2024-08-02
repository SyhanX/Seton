package com.example.seton.common.presentation.state

import androidx.compose.ui.graphics.Color
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

enum class ContainerColor(
    val lightVariant: Color,
    val darkVariant: Color
) {
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