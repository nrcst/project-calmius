package com.l5.calmius.Navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.l5.calmius.Navigation.AppNavHost
import com.l5.calmius.feature.journaling.presentation.JournalViewModel

@Composable
fun MainScreen(journalViewModel: JournalViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        AppNavHost(navController = navController, journalViewModel = journalViewModel, modifier = Modifier.padding(it))
    }
}
