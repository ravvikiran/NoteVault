package com.notevault.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.notevault.app.ui.components.FolderSidebar
import com.notevault.app.ui.navigation.NoteVaultNavHost
import com.notevault.app.ui.navigation.Screen
import com.notevault.app.ui.navigation.bottomNavItems
import com.notevault.app.ui.screens.notes.NotesViewModel
import com.notevault.app.ui.theme.NoteVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteVaultTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NoteVaultMainScreen()
                }
            }
        }
    }
}

@Composable
fun NoteVaultMainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: NotesViewModel = hiltViewModel()

    val folders by viewModel.folders.collectAsState()
    val selectedFolderId by viewModel.selectedFolderId.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FolderSidebar(
                folders = folders,
                selectedFolderId = selectedFolderId,
                noteCountProvider = { folderId ->
                    kotlinx.coroutines.flow.flowOf(0)
                },
                onFolderSelected = { folderId ->
                    viewModel.selectFolder(folderId)
                    scope.launch { drawerState.close() }
                    if (folderId != null) {
                        navController.navigate(Screen.FolderNotes.createRoute(folderId)) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onAllNotesSelected = {
                    viewModel.selectFolder(null)
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Notes.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onFavoritesSelected = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Favorites.route) {
                        launchSingleTop = true
                    }
                },
                onTrashSelected = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Trash.route) {
                        launchSingleTop = true
                    }
                },
                onNewFolder = {
                    // TODO: Show new folder dialog
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Hide bottom bar on editor screen
                val showBottomBar = currentDestination?.route?.startsWith("note_editor") != true

                if (showBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true

                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NoteVaultNavHost(
                navController = navController,
                onFolderSidebarRequest = {
                    scope.launch { drawerState.open() }
                }
            )
        }
    }
}
