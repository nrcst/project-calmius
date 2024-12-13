package com.l5.calmius.features.meditation.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.l5.calmius.R
import com.l5.calmius.features.meditation.data.MeditationTrack
import com.l5.calmius.features.meditation.data.MeditationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.l5.calmius.features.meditation.data.DatabaseProvider
import com.l5.calmius.ui.theme.MeditationColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.sp
import com.l5.calmius.ui.theme.Blue400


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationTrackListScreen(
    type: MeditationType,
    onTrackSelected: (MeditationTrack) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tracks = remember { mutableStateOf(listOf<MeditationTrack>()) }
    val backgroundColor = MeditationColors.getColorByType(type)

    LaunchedEffect(type) {
        val db = DatabaseProvider.getDatabase(context, CoroutineScope(Dispatchers.IO))
        tracks.value = db.meditationTrackDao().getTracksByType(type)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${type.name.lowercase().replaceFirstChar { it.uppercase() }} Tracks",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tracks.value) { track ->
                MeditationTrackCard(
                    track = track,
                    backgroundColor = backgroundColor,
                    onClick = { onTrackSelected(track) }
                )
            }
        }
    }
}

@Composable
private fun MeditationTrackCard(
    track: MeditationTrack,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = track.title,
                fontSize = 20.sp,
                color = Blue400,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = track.brief,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${track.duration} mins",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        fontSize = 15.sp,
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.play_circle),
                    contentDescription = "Play",
                    modifier = Modifier.size(38.dp),
                    tint = Blue400
                )
            }
        }
    }
}