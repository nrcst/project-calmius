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
fun PlayingScreen(track: MeditationTrack, onDone: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        mediaPlayer = MediaPlayer.create(context, context.resources.getIdentifier(track.audioPath, "raw", context.packageName))
        mediaPlayer?.setOnCompletionListener {
            onDone()
        }
        onDispose {
            mediaPlayer?.release()
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mediaPlayer?.start()
            while (isPlaying && mediaPlayer?.isPlaying == true) {
                currentPosition = mediaPlayer?.currentPosition?.toFloat() ?: 0f
                delay(1000L)
            }
        } else {
            mediaPlayer?.pause()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = track.title, style = MaterialTheme.typography.headlineSmall)
        Text(text = track.description, style = MaterialTheme.typography.bodyMedium)
        Slider(
            value = currentPosition,
            onValueChange = { newPosition ->
                mediaPlayer?.seekTo(newPosition.toInt())
                currentPosition = newPosition
            },
            valueRange = 0f..(mediaPlayer?.duration?.toFloat() ?: 0f),
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { isPlaying = !isPlaying }) {
                Text(text = if (isPlaying) "Pause" else "Resume")
            }
            Button(onClick = { onDone() }) {
                Text(text = "Done")
            }
        }
    }
}
