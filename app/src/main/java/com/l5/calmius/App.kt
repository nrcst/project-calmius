package com.l5.calmius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.l5.calmius.ui.theme.CalmiusTheme
import androidx.compose.ui.Modifier
import com.l5.calmius.feature.journaling.data.JournalRepository
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import com.l5.calmius.feature.journaling.presentation.JournalViewModelFactory
import com.l5.calmius.features.journaling.data.JournalDatabase
import com.l5.calmius.Navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmiusTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val journalRepository = JournalRepository(
                    journalDao = JournalDatabase.getDatabase(context).journalDao()
                )
                val journalViewModel: JournalViewModel by viewModels {
                    JournalViewModelFactory(journalRepository)
                }
                AppNavHost(navController = navController, journalViewModel = journalViewModel, modifier = Modifier)
            }
        }
    }
}