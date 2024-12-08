package com.l5.calmius.feature.journaling.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import com.l5.calmius.features.journaling.data.JournalDatabase
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun JournalDetailScreen(
    navController: NavController,
    journalId: Long,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val journalViewModel = JournalViewModel(
        JournalRepository(
            journalDao = JournalDatabase.getDatabase(context).journalDao()
        )
    )
    val journalState = produceState<JournalEntity?>(initialValue = null, journalId) {
        journalViewModel.getJournalById(journalId) { journal ->
            value = journal
        }
    }
    val journal = journalState.value
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val formattedDate = journal?.date?.let {
        dateFormat.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)!!)
    } ?: "Unknown Date"

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("JournalEdit/${journalId}")
                },
                shape = CircleShape,
                containerColor = Color.Gray,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp) // Set the size to make it circular
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = journal?.title ?: "No Title",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AsyncImage(
                model = journal?.imageUrl,
                contentDescription = "Journal Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
            )
            Box() {
                Column {
                    Text(text = "Today Stories", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(
                        text = journal?.story ?: "No Story",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            Box() {
                Column {
                    Text(text = "Gratitude", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(
                        text = journal?.gratitude ?: "No Gratitude",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}