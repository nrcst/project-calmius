package com.l5.calmius.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationDestination(val route: String, val icon: ImageVector) {
    object Home : NavigationDestination("home", Icons.Default.Home)
    object Meditation : NavigationDestination("meditation", Icons.Default.Favorite)
    object Community : NavigationDestination("community", Icons.Default.Search)
    object Journal : NavigationDestination("journal", Icons.Default.Edit)
    object Profile : NavigationDestination("profile", Icons.Default.AccountCircle)
}