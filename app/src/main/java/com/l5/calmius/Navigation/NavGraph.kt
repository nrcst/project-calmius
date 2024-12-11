package com.l5.calmius.Navigation

import com.l5.calmius.features.meditation.presentation.MeditationScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.l5.calmius.feature.journaling.presentation.JournalAddScreen
import com.l5.calmius.feature.journaling.presentation.JournalListScreen
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import androidx.compose.ui.Modifier
import com.l5.calmius.feature.journaling.presentation.JournalDetailScreen
import com.l5.calmius.feature.journaling.presentation.JournalEditScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    journalViewModel: JournalViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Meditation.route,
        modifier = modifier
    ) {
        composable(Screen.Meditation.route) {
            MeditationScreen()
        }
        composable("JournalList") {
            JournalListScreen(navController, modifier, journalViewModel)
        }
        composable("JournalList") {
            JournalListScreen(navController, modifier, journalViewModel)
        }
        composable("JournalAdd") {
            JournalAddScreen(navController)
        }
        composable("JournalDetail/{journalId}") { backStackEntry ->
            val journalId = backStackEntry.arguments?.getString("journalId")?.toLong() ?: 0L
            JournalDetailScreen(navController, journalId)
        }
        composable("JournalEdit/{journalId}") { backStackEntry ->
            val journalId = backStackEntry.arguments?.getString("journalId")?.toLong() ?: 0L
            JournalEditScreen(navController, journalId)
        }
    }
}