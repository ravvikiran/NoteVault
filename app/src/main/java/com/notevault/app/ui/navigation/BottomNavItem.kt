package com.notevault.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Today", Screen.Today.route, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Notes", Screen.Notes.route, Icons.Filled.StickyNote2, Icons.Outlined.StickyNote2),
    BottomNavItem("Calendar", Screen.Calendar.route, Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    BottomNavItem("Search", Screen.Search.route, Icons.Filled.Search, Icons.Outlined.Search),
    BottomNavItem("Settings", Screen.Settings.route, Icons.Filled.Settings, Icons.Outlined.Settings),
)
