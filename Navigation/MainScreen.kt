
package com.l5.calmius.Navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.l5.calmius.Navigation.AppNavHost
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import com.l5.calmius.features.community.presentation.CommunityViewModel
import com.l5.calmius.features.community.data.FirebaseRepository

@Composable
fun MainScreen(
    journalViewModel: JournalViewModel,
    communityViewModel: CommunityViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in listOf(
        "home", "meditation", "community", "journal", "profile"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        AppNavHost(
            navController = navController,
            journalViewModel = journalViewModel,
            communityViewModel = communityViewModel,
            modifier = Modifier.padding(it)
        )
    }
}