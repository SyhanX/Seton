package com.example.seton.common.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* Light colors*/
val primaryLight = Color(0xFF8E4956)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFFFD9DD)
val onPrimaryContainerLight = Color(0xFF3B0715)
val secondaryLight = Color(0xFF76565A)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFFD9DD)
val onSecondaryContainerLight = Color(0xFF2C1519)
val tertiaryLight = Color(0xFF795831)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFFDDBA)
val onTertiaryContainerLight = Color(0xFF2B1700)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFFFF8F7)
val onBackgroundLight = Color(0xFF22191A)
val surfaceLight = Color(0xFFFFF8F7)
val onSurfaceLight = Color(0xFF22191A)
val surfaceVariantLight = Color(0xFFF3DDDF)
val onSurfaceVariantLight = Color(0xFF524345)
val outlineLight = Color(0xFF847375)
val outlineVariantLight = Color(0xFFD6C2C3)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF382E2F)
val inverseOnSurfaceLight = Color(0xFFFEEDEE)
val inversePrimaryLight = Color(0xFFFFB2BD)
val surfaceDimLight = Color(0xFFE7D6D7)
val surfaceBrightLight = Color(0xFFFFF8F7)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFFFF0F1)
val surfaceContainerLight = Color(0xFFFBEAEB)
val surfaceContainerHighLight = Color(0xFFF6E4E5)
val surfaceContainerHighestLight = Color(0xFFF0DEE0)
/*Dark colors*/
val primaryDark = Color(0xFFFFB2BD)
val onPrimaryDark = Color(0xFF561D29)
val primaryContainerDark = Color(0xFF72333F)
val onPrimaryContainerDark = Color(0xFFFFD9DD)
val secondaryDark = Color(0xFFE5BDC1)
val onSecondaryDark = Color(0xFF43292D)
val secondaryContainerDark = Color(0xFF5C3F43)
val onSecondaryContainerDark = Color(0xFFFFD9DD)
val tertiaryDark = Color(0xFFEABF8F)
val onTertiaryDark = Color(0xFF452B07)
val tertiaryContainerDark = Color(0xFF5F411C)
val onTertiaryContainerDark = Color(0xFFFFDDBA)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF191112)
val onBackgroundDark = Color(0xFFF0DEE0)
val surfaceDark = Color(0xFF191112)
val onSurfaceDark = Color(0xFFF0DEE0)
val surfaceVariantDark = Color(0xFF524345)
val onSurfaceVariantDark = Color(0xFFD6C2C3)
val outlineDark = Color(0xFF9F8C8E)
val outlineVariantDark = Color(0xFF524345)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFF0DEE0)
val inverseOnSurfaceDark = Color(0xFF382E2F)
val inversePrimaryDark = Color(0xFF8E4956)
val surfaceDimDark = Color(0xFF191112)
val surfaceBrightDark = Color(0xFF413738)
val surfaceContainerLowestDark = Color(0xFF140C0D)
val surfaceContainerLowDark = Color(0xFF22191A)
val surfaceContainerDark = Color(0xFF261D1E)
val surfaceContainerHighDark = Color(0xFF312829)
val surfaceContainerHighestDark = Color(0xFF3D3233)

/*Custom colors*/
val defaultColorDark = Color(0xFF43292D)
val redDark = Color(0xFF77172E)
val orangeDark = Color(0xFF692B17)
val yellowDark = Color(0xFF7C4A03)
val greenDark = Color(0xFF264D3B)
val turquoiseDark = Color(0xFF0C625D)
val blueDark = Color(0xFF256377)
val darkBlueDark = Color(0xFF284255)
val purpleDark = Color(0xFF472E5B)
val pinkDark = Color(0xFF6C394F)
val brownDark = Color(0xFF4B443A)
val greyDark = Color(0xFF232427)

val defaultColorLight = Color(0xFFFFFFFF)
val redLight = Color(0xFFFAAFA8)
val orangeLight = Color(0xFFF39F76)
val yellowLight = Color(0xFFFFF8B8)
val greenLight = Color(0xFFE2F6D3)
val turquoiseLight = Color(0xFFB4DDD3)
val blueLight = Color(0xFFD4E4ED)
val darkBlueLight = Color(0xFFAECCDC)
val purpleLight = Color(0xFFD3BFDB)
val pinkLight = Color(0xFFF6E2DD)
val brownLight = Color(0xFFE9E3D4)
val greyLight = Color(0xFFEFEFF1)

@Composable
fun dynamicTextColor(): Color {
    return if (isSystemInDarkTheme()) Color.White else Color.Black
}