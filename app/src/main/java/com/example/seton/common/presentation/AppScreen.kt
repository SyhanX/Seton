@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.seton.common.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seton.feature_notes.presentation.edit_note.EditNoteScreen
import com.example.seton.feature_notes.presentation.note_list.NoteListScreen
import kotlinx.serialization.Serializable

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
            startDestination = NoteListRoute,
//            enterTransition = { slideFromBottomAnimation() },
//            exitTransition = { slideToBottomAnimation() },
//            popEnterTransition = { slideFromBottomAnimation() },
//            popExitTransition = { slideToBottomAnimation() },
        ) {
            composable<NoteListRoute> {
                NoteListScreen(
                    onFabClick = { navController.navigate(EditNoteRoute(-1)) },
                    cardTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                ) { id ->
                    navController.navigate(EditNoteRoute(id))
                }
            }
            composable<EditNoteRoute> {
//                val args = it.toRoute<EditNoteRoute>()
                EditNoteScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                ) {
                    navController.navigateUp()
                }
            }
        }
    }
}

//TODO: create a wrapper for nav destinations
@Serializable
object NoteListRoute

@Serializable
data class EditNoteRoute(val currentNoteId: Int)

@Serializable
object SettingsRoute