package com.example.seton.common.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.seton.common.presentation.components.slideFromBottomAnimation
import com.example.seton.common.presentation.components.slideToBottomAnimation
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
    NavHost(
        navController = navController,
        startDestination = NoteListRoute,
        enterTransition = {  slideFromBottomAnimation() },
        exitTransition = { slideToBottomAnimation() },
        popEnterTransition = { slideFromBottomAnimation() },
        popExitTransition = { slideToBottomAnimation() },
    ) {
        composable<NoteListRoute> {
            NoteListScreen(
                onFabClick = { navController.navigate(EditNoteRoute(-1)) }
            ) { id ->
                Log.d(TAG, "note id: $id ")
                navController.navigate(EditNoteRoute(id))
            }
        }
        composable<EditNoteRoute> {
            val args = it.toRoute<EditNoteRoute>()
            Log.d(TAG, "args: $args")
            EditNoteScreen {
                navController.navigateUp()
            }
        }
    }
}

@Serializable
object NoteListRoute

@Serializable
data class EditNoteRoute(val currentNoteId: Int)

@Serializable
object SettingsRoute