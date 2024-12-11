package com.l5.calmius.features.meditation.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationTrack

@Composable
fun PreStartScreen(track: MeditationTrack, onStartListening: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = track.title, style = MaterialTheme.typography.titleSmall)
        Text(text = track.description, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Duration: ${track.duration} minutes", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onStartListening) {
            Text(text = "Start Listening")
        }
    }
}
