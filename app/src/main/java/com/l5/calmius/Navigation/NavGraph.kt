package com.l5.calmius.Navigation

import android.app.Activity.RESULT_OK
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.finalpapb.Auth.presentation.LandingScreen
import com.google.android.gms.auth.api.identity.Identity
import com.l5.calmius.feature.journaling.presentation.JournalDetailScreen
import com.l5.calmius.feature.journaling.presentation.JournalEditScreen
import com.l5.calmius.features.auth.data.AuthViewModel
import com.l5.calmius.features.auth.data.GoogleAuthUiClient
import com.l5.calmius.features.auth.presentation.HomeScreen
import com.l5.calmius.features.auth.presentation.SignInScreen
import com.l5.calmius.features.auth.presentation.SignUpScreen
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
import com.l5.calmius.features.community.presentation.SearchBarAndHistoryScreen
import com.l5.calmius.features.community.presentation.SearchScreen
import com.l5.calmius.features.community.presentation.CreatePostScreen
import com.l5.calmius.features.community.presentation.DetailPostScreen
import com.l5.calmius.features.community.presentation.PostCommentScreen
import com.l5.calmius.features.profile.presentation.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    journalViewModel: JournalViewModel,
    communityViewModel: CommunityViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val googleAuthUiClient = GoogleAuthUiClient(
        context = context.applicationContext,
        oneTapClient = Identity.getSignInClient(context.applicationContext)
    )
    NavHost(
        navController = navController,
        startDestination = "landingScreen",
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

        composable("searchBarAndHistory") {
            SearchBarAndHistoryScreen(navController, viewModel = communityViewModel)
        }

        // Community
        composable("searchScreen/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchScreen(query = query, navController = navController, viewModel = communityViewModel)
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

        // Auth
        composable("landingScreen") {
            LandingScreen(navController, authViewModel)
        }
        composable("signIn") {
            val viewModel = viewModel<AuthViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if(googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate("profile")
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        context.applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate("profile")
                    viewModel.resetState()
                }
            }

            SignInScreen(
                navController = navController,
                authViewModel = authViewModel,
                state = state,
                googleSignIn = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
        composable("signUp") {
            val viewModel = viewModel<AuthViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if(googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate("profile")
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        context.applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate("profile")
                    viewModel.resetState()
                }
            }
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel,
                state = state,
                googleSignIn = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
        composable("homeScreen") {
            HomeScreen(navController, authViewModel)
        }
        composable("profile") {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                navController = navController,
                signOut = {
                    coroutineScope.launch {
                        try {
                            if (googleAuthUiClient.isGoogleSignIn()) {
                                googleAuthUiClient.signOut()
                            } else {
                                authViewModel.signOut()
                            }
                            navController.navigate("landingScreen") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                context.applicationContext,
                                "Error during sign-out: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }

    }
}