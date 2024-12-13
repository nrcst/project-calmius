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
import com.l5.calmius.features.community.presentation.CommunityScreen
import com.l5.calmius.features.community.presentation.CommunityViewModel
import com.l5.calmius.features.community.presentation.SearchScreen
import com.l5.calmius.features.community.presentation.CreatePostScreen
import com.l5.calmius.features.community.presentation.DetailPostScreen
import com.l5.calmius.features.community.presentation.PostCommentScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    journalViewModel: JournalViewModel,
    communityViewModel: CommunityViewModel,
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
            MeditationTrackListScreen(
                type = type,
                onTrackSelected = { track ->
                navController.navigate("preStart/${track.id}")
                },
                onBack = { navController.popBackStack() }
            )
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
                PreStartScreen(
                    track = it,
                    onStartListening = {
                    navController.navigate("playing/${it.id}")
                    },
                    onBack = { navController.popBackStack() }
                )
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
                PlayingScreen(track = it, onFinished = {
                    navController.navigate("finished")
                }, onBack = {
                    navController.popBackStack()
                })
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

        composable(NavigationDestination.Community.route) {
            CommunityScreen(navController, viewModel = communityViewModel)
        }

        // Community
        composable("searchScreen") {
            SearchScreen(query = "", navController = navController, viewModel = communityViewModel)
        }
        composable("createPost") {
            CreatePostScreen(navController = navController, viewModel = communityViewModel)
        }
        composable("detailPost/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            DetailPostScreen(postId = postId, navController = navController, viewModel = communityViewModel)
        }
        composable("postComment/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            PostCommentScreen(postId = postId, navController = navController, viewModel = communityViewModel)
        }
    }
}