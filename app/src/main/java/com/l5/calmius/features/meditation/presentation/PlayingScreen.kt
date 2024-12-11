package com.l5.calmius.features.meditation.ui

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationTrack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayingScreen(track: MeditationTrack, onFinished: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        mediaPlayer = MediaPlayer.create(context, track.resourceId)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            onFinished()
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = track.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = track.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            if (isPlaying) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
            isPlaying = !isPlaying
        }) {
            Text(text = if (isPlaying) "Pause" else "Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            mediaPlayer?.stop()
            onFinished()
        }) {
            Text(text = "Stop")
        }
    }
}