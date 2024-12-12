package com.l5.calmius.features.meditation.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l5.calmius.features.meditation.data.MeditationTrack
import com.l5.calmius.ui.theme.MeditationColors
import com.l5.calmius.R
import com.l5.calmius.ui.theme.Blue400
import okhttp3.internal.wait
import java.time.format.TextStyle


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreStartScreen(
    track: MeditationTrack,
    onStartListening: () -> Unit,
    onBack: () -> Unit
) {
    val backgroundColor = MeditationColors.getColorByType(track.meditationType)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        }
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

                Spacer(modifier = Modifier.height(61.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = track.title,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                            )
                            Text(
                                text = track.description,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.Gray
                                ),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(21.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = "Meditation Illustration",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = " ${track.duration} minutes",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        Button(
                            onClick = onStartListening,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Blue400)
                        ) {
                            Text(text = "Start Listening", fontSize = 15.sp)
                        }

                    }
                }
            }
        }
    }
}
