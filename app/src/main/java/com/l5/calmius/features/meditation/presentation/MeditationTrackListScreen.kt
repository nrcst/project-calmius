package com.l5.calmius.features.meditation.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationTrack
import com.l5.calmius.features.meditation.data.MeditationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.l5.calmius.features.meditation.data.DatabaseProvider

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationTrackListScreen(
    type: MeditationType,
    onTrackSelected: (MeditationTrack) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tracks = remember { mutableStateOf(listOf<MeditationTrack>()) }

    LaunchedEffect(type) {
        val db = DatabaseProvider.getDatabase(context, CoroutineScope(Dispatchers.IO))
        tracks.value = db.meditationTrackDao().getTracksByType(type)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${type.name.lowercase().replaceFirstChar { it.uppercase() }} Tracks"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(tracks.value) { track ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onTrackSelected(track) }
                ) {
                    Text(
                        text = track.title,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}