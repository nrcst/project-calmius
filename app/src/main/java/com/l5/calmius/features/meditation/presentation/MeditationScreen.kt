package com.l5.calmius.features.meditation.presentation

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationType

@Composable
fun MeditationScreen(onTypeSelected: (MeditationType) -> Unit) {

    Column {
        MeditationType.values().forEach { type ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onTypeSelected(type) }) {
                Text(text = type.name, modifier = Modifier.padding(16.dp))
            }
        }
    }
}