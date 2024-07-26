@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.seton.common.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seton.common.data.NavDestinations
import com.example.seton.feature_notes.presentation.edit_note.EditNoteScreen
import com.example.seton.feature_notes.presentation.note_list.NoteListScreen

private const val TAG = "app_screen"

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    AppContent(navController)
}

@Composable
private fun AppContent(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = NavDestinations.NoteListScreen,
        ) {
            composable<NavDestinations.NoteListScreen> {
                NoteListScreen(
                    onFabClick = { navController.navigate(NavDestinations.EditNoteScreen(-1)) },
                    cardTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    navController = navController
                )
            }
            composable<NavDestinations.EditNoteScreen> {
                EditNoteScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    navController = navController
                )
            }
        }
    }
}