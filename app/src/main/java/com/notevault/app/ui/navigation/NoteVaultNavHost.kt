package com.notevault.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.notevault.app.ui.screens.editor.NoteEditorScreen
import com.notevault.app.ui.screens.notes.NotesScreen
import com.notevault.app.ui.screens.today.TodayScreen

@Composable
fun NoteVaultNavHost(
    navController: NavHostController,
    onFolderSidebarRequest: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Today.route,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Screen.Today.route) {
            TodayScreen(
                onNewNote = {
                    navController.navigate(Screen.NewNote.createRoute())
                }
            )
        }

        composable(Screen.Notes.route) {
            NotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Screen.NoteEditor.createRoute(noteId))
                },
                onNewNote = { folderId ->
                    navController.navigate(Screen.NewNote.createRoute(folderId))
                },
                onFolderSidebarRequest = onFolderSidebarRequest
            )
        }

        composable(Screen.Calendar.route) {
            // Placeholder — will be implemented
            TodayScreen(onNewNote = {})
        }

        composable(Screen.Search.route) {
            // Placeholder — will be implemented
            TodayScreen(onNewNote = {})
        }

        composable(Screen.Settings.route) {
            // Placeholder — will be implemented
            TodayScreen(onNewNote = {})
        }

        composable(
            route = "note_editor/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
            NoteEditorScreen(
                noteId = noteId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "note_editor/new?folderId={folderId}&noteType={noteType}",
            arguments = listOf(
                navArgument("folderId") { type = NavType.LongType; defaultValue = -1L },
                navArgument("noteType") { type = NavType.StringType; defaultValue = "text" }
            )
        ) { backStackEntry ->
            val folderId = backStackEntry.arguments?.getLong("folderId")?.takeIf { it != -1L }
            val noteType = backStackEntry.arguments?.getString("noteType") ?: "text"
            NoteEditorScreen(
                noteId = null,
                folderId = folderId,
                noteType = noteType,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "folder/{folderId}",
            arguments = listOf(navArgument("folderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val folderId = backStackEntry.arguments?.getLong("folderId") ?: return@composable
            NotesScreen(
                selectedFolderId = folderId,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.NoteEditor.createRoute(noteId))
                },
                onNewNote = { navController.navigate(Screen.NewNote.createRoute(folderId)) },
                onFolderSidebarRequest = onFolderSidebarRequest
            )
        }
    }
}
