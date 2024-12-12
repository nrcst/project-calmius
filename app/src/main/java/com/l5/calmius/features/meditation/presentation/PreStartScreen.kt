package com.l5.calmius.features.meditation.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationTrack

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreStartScreen(
    track: MeditationTrack,
    onStartListening: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Prepare to Listen") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = track.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Duration: ${track.duration} minutes",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = onStartListening,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Start Listening")
            }
        }
    }
}
