package com.l5.calmius.features.meditation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.l5.calmius.features.meditation.data.MeditationType
import com.l5.calmius.ui.theme.Blue500
import com.l5.calmius.ui.theme.MeditationColors

@Composable
fun MeditationScreen(onTypeSelected: (MeditationType) -> Unit) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Text(
            text = "Good Morning!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Let's start the day with a meditation",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F1FC)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Become as calm\nas the wind blow.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color(0xFF4A6572)
                    )
                )

                Text(
                    text = "Drift peacefully, like a\ncalm gentle breeze.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "7 min",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = Color(0xFF4A6572),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Text(
            text = "Choose by your mood",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MeditationTypeItem(MeditationType.MOOD, onTypeSelected)
                MeditationTypeItem(MeditationType.MEMORIES, onTypeSelected)
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MeditationTypeItem(MeditationType.CALM, onTypeSelected)
                MeditationTypeItem(MeditationType.ENERGIC, onTypeSelected)
            }
        }
    }
}

@Composable
private fun MeditationTypeItem(
    type: MeditationType,
    onTypeSelected: (MeditationType) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = MeditationColors.getColorByType(type),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onTypeSelected(type) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val icon = when (type) {
                MeditationType.MOOD -> Icons.Outlined.Favorite
                MeditationType.CALM -> Icons.Outlined.ThumbUp
                MeditationType.MEMORIES -> Icons.Outlined.Star
                MeditationType.ENERGIC -> Icons.Outlined.Build
            }

            Icon(
                imageVector = icon,
                contentDescription = type.name,
                modifier = Modifier.size(24.dp),
                tint = Color.Black.copy(alpha = 0.7f)
            )

            Text(
                text = type.name.lowercase()
                    .replaceFirstChar { it.uppercase() },
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
            )
        }
    }
}