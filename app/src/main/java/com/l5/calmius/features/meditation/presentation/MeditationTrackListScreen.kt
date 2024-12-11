package com.l5.calmius.features.meditation.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationTrack
import com.l5.calmius.features.meditation.data.MeditationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.l5.calmius.features.meditation.data.DatabaseProvider

@Composable
fun MeditationTrackListScreen(type: MeditationType, onTrackSelected: (MeditationTrack) -> Unit) {
    val context = LocalContext.current
    val tracks = remember { mutableStateOf(listOf<MeditationTrack>()) }
    LaunchedEffect(type) {
        val db = DatabaseProvider.getDatabase(context, CoroutineScope(Dispatchers.IO))
        tracks.value = db.meditationTrackDao().getTracksByType(type)
    }
    LazyColumn {
        items(tracks.value) { track ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onTrackSelected(track) }) {
                Text(text = track.title, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
