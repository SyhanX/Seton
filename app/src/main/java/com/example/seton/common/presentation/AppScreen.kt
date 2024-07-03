package com.example.seton.common.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seton.feature_notes.presentation.edit_note.EditNoteScreen
import com.example.seton.feature_notes.presentation.note_list.NoteListScreen
import kotlinx.serialization.Serializable

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
        startDestination = NoteListRoute
    ) {
        composable<NoteListRoute> {
            NoteListScreen(
                onFabClick = { navController.navigate(EditNoteRoute(-1)) }
            ) { id ->
                navController.navigate(EditNoteRoute(id))
            }
        }
        composable<EditNoteRoute> {
            EditNoteScreen {
                navController.navigateUp()
            }
        }
    }
}

@Serializable
object NoteListRoute

@Serializable
data class EditNoteRoute(val noteId: Int)

@Serializable
object SettingsRoute