package com.l5.calmius.features.meditation.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationTrack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayingScreen(track: MeditationTrack, onFinished: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(0) }
    val duration = track.durationMillis

    DisposableEffect(Unit) {
        mediaPlayer = MediaPlayer.create(context, track.resourceId).apply {
            setOnCompletionListener {
                onFinished()
            }
            start()
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer?.currentPosition ?: 0
            delay(1000L)
        }
    }

    fun formatTime(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = track.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onFinished) {
                        Text(text = "Done")
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = track.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(currentPosition), style = MaterialTheme.typography.bodyMedium)
                Text(text = "-${formatTime(duration - currentPosition)}", style = MaterialTheme.typography.bodyMedium)
            }

            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { newPosition ->
                    mediaPlayer?.seekTo(newPosition.toInt())
                    currentPosition = newPosition.toInt()
                },
                valueRange = 0f..duration.toFloat()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f))

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

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = {
                    mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) + 15000)
                }) {
                    Text(text = "Forward 15 seconds")
                }
            }
        }
    }
}
