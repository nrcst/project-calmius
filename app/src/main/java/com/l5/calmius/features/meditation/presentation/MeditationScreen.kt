package com.l5.calmius.features.meditation.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l5.calmius.features.meditation.data.MeditationType
import com.l5.calmius.ui.theme.Blue400
import com.l5.calmius.ui.theme.Blue500
import com.l5.calmius.ui.theme.MeditationColors
import com.l5.calmius.R


@Composable
fun MeditationScreen(onTypeSelected: (MeditationType) -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Good Morning!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp,
                textAlign = TextAlign.Start
            ),
            fontSize = 35.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = "Let's start the day with a meditation",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontSize = 16.sp
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F1FC)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Become as calm\nas the wind blow.",
                        fontSize = 20.sp,
                        color = Blue400,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Drift peacefully, like a\ncalm gentle breeze.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        ),
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.meditation_asset1),
                    contentDescription = "Meditation Asset",
                    modifier = Modifier
                        .size(120.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "7 mins",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.play_circle),
                    contentDescription = "Play",
                    tint = Color(0xFF4A6572),
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        Text(
            text = "Choose by your mood",
            style = MaterialTheme.typography.labelLarge,
            fontSize = 16.sp,
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
                MeditationType.MOOD -> R.drawable.mood
                MeditationType.CALM -> R.drawable.spa
                MeditationType.MEMORIES -> R.drawable.extension
                MeditationType.ENERGIC -> R.drawable.bolt
            }

            Icon(
                painter = painterResource(id = icon),
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