package com.l5.calmius.Navigation

import com.l5.calmius.features.meditation.presentation.MeditationScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.l5.calmius.feature.journaling.presentation.JournalAddScreen
import com.l5.calmius.feature.journaling.presentation.JournalListScreen
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import com.l5.calmius.feature.journaling.presentation.JournalDetailScreen
import com.l5.calmius.feature.journaling.presentation.JournalEditScreen
import com.l5.calmius.features.meditation.data.DatabaseProvider
import com.l5.calmius.features.meditation.data.MeditationTrack
import com.l5.calmius.features.meditation.data.MeditationType
import com.l5.calmius.features.meditation.presentation.FinishedScreen
import com.l5.calmius.features.meditation.presentation.MeditationTrackListScreen
import com.l5.calmius.features.meditation.presentation.PreStartScreen
import com.l5.calmius.features.meditation.ui.PlayingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.l5.calmius.Navigation.*


@Composable
fun AppNavHost(
    navController: NavHostController,
    journalViewModel: JournalViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "meditation",
        modifier = modifier
    ) {
        // Meditation
        composable("meditation") {
            MeditationScreen { type ->
                navController.navigate("meditationTrackList/${type.name}")
            }
        }
        composable("meditationTrackList/{type}") { backStackEntry ->
            val type = MeditationType.valueOf(backStackEntry.arguments?.getString("type") ?: "")
            MeditationTrackListScreen(type) { track ->
                navController.navigate("preStart/${track.id}")
            }
        }
        composable("preStart/{trackId}") { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId")?.toInt() ?: 0
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            var track by remember { mutableStateOf<MeditationTrack?>(null) }

            LaunchedEffect(trackId) {
                scope.launch {
                    val db = DatabaseProvider.getDatabase(context, CoroutineScope(Dispatchers.IO))
                    track = db.meditationTrackDao().getTrackById(trackId)
                }
            }

            track?.let {
                PreStartScreen(track = it) {
                    navController.navigate("playing/${it.id}")
                }
            }
        }
        composable("playing/{trackId}") { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId")?.toInt() ?: 0
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            var track by remember { mutableStateOf<MeditationTrack?>(null) }

            LaunchedEffect(trackId) {
                scope.launch {
                    val db = DatabaseProvider.getDatabase(context, CoroutineScope(Dispatchers.IO))
                    track = db.meditationTrackDao().getTrackById(trackId)
                }
            }

            track?.let {
                PlayingScreen(track = it) {
                    navController.navigate("finished")
                }
            }
        }
        composable("finished") {
            FinishedScreen {
                navController.navigate("meditation") {
                    popUpTo("meditation") {
                        inclusive = true
                    }
                }
            }
        }

        // Journaling
        composable("journal") {
            JournalListScreen(navController, modifier, journalViewModel)
        }
        composable("JournalAdd") {
            JournalAddScreen(navController)
        }
        composable("JournalDetail/{journalId}") { backStackEntry ->
            val journalId = backStackEntry.arguments?.getString("journalId")?.toLong() ?: 0L
            JournalDetailScreen(navController, journalId, modifier)
        }
        composable("JournalEdit/{journalId}") { backStackEntry ->
            val journalId = backStackEntry.arguments?.getString("journalId")?.toLong() ?: 0L
            JournalEditScreen(navController, journalId, modifier, journalViewModel)
        }
    }
}