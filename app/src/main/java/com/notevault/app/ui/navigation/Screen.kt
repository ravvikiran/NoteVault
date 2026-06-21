package com.notevault.app.ui.navigation

sealed class Screen(val route: String) {
    data object Today : Screen("today")
    data object Notes : Screen("notes")
    data object Calendar : Screen("calendar")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
    data object NoteEditor : Screen("note_editor/{noteId}") {
        fun createRoute(noteId: Long) = "note_editor/$noteId"
    }
    data object NewNote : Screen("note_editor/new?folderId={folderId}&noteType={noteType}") {
        fun createRoute(folderId: Long? = null, noteType: String = "text") =
            "note_editor/new?folderId=${folderId ?: -1}&noteType=$noteType"
    }
    data object FolderNotes : Screen("folder/{folderId}") {
        fun createRoute(folderId: Long) = "folder/$folderId"
    }
    data object Trash : Screen("trash")
    data object Favorites : Screen("favorites")
}
