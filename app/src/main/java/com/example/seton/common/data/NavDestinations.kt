package com.example.seton.common.data

import kotlinx.serialization.Serializable

sealed interface NavDestinations {
    @Serializable
    data object NoteListScreen : NavDestinations

    @Serializable
    data class EditNoteScreen(
        val currentNoteId: Int,
        val currentNoteColor: String
    ) : NavDestinations

    @Serializable
    data object SettingsScreen : NavDestinations

    @Serializable
    data object TestScreen : NavDestinations
}