package com.l5.calmius.Navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.l5.calmius.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationDestination.Home,
        NavigationDestination.Meditation,
        NavigationDestination.Community,
        NavigationDestination.Journal,
        NavigationDestination.Profile
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { destination ->
            NavigationBarItem(
                icon = { Icon(destination.icon, contentDescription = destination.route) },
                label = { Text(destination.route) },
                selected = currentDestination?.route == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
