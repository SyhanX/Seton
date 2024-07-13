package com.example.seton.common.presentation.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

fun slideFromBottomAnimation(): EnterTransition {
    return slideInVertically() { it / 2 }
}

fun slideToBottomAnimation(): ExitTransition {
    return slideOutVertically() { it / 2 } + fadeOut()
}

fun slideFromLeftAnimation(): EnterTransition {
    return slideInHorizontally()
}

fun slideToRightAnimation(): ExitTransition {
    return slideOutHorizontally() + fadeOut()
}