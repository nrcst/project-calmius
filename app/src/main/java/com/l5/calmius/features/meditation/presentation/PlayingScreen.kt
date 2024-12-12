package com.l5.calmius.features.meditation.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l5.calmius.R
import com.l5.calmius.features.meditation.data.MeditationTrack
import com.l5.calmius.ui.theme.Blue400
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.l5.calmius.ui.theme.MeditationColors


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayingScreen(track: MeditationTrack, onFinished: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(0) }
    val duration = track.durationMillis
    val backgroundColor = MeditationColors.getColorByType(track.meditationType)

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
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = onFinished,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .graphicsLayer(

                            ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Done",
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.meditation_asset1),
                    contentDescription = "Meditation Illustration",
                    modifier = Modifier
                        .size(340.dp)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(155.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(vertical = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    ) {
                        Column {

                            Text(
                                text = track.title,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                            )

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
                                valueRange = 0f..duration.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.Black,
                                    activeTrackColor = Color.Black,
                                    inactiveTrackColor = Color(0xFFF1EDE9)
                                )
                            )


                            Spacer(modifier = Modifier.height(1.dp))
                        }

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(80.dp))

                                IconButton(
                                    onClick = {
                                        if (isPlaying) {
                                            mediaPlayer?.pause()
                                        } else {
                                            mediaPlayer?.start()
                                        }
                                        isPlaying = !isPlaying
                                    },
                                    modifier = Modifier.size(92.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = if (isPlaying) R.drawable.pause_circle else R.drawable.play_circle),
                                        contentDescription = if (isPlaying) "Pause" else "Play",
                                        modifier = Modifier.size(74.dp),
                                        tint = Blue400
                                    )
                                }


                                Spacer(modifier = Modifier.width(22.dp))


                                IconButton(
                                    onClick = {
                                        mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) + 10000)
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.forward_10),
                                        contentDescription = "Forward 10 seconds",
                                        modifier = Modifier.size(48.dp),
                                        tint = Blue400
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
