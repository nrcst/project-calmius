package com.l5.calmius.feature.journaling.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import com.l5.calmius.features.journaling.data.JournalDatabase
import com.l5.calmius.ui.theme.Typography
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

    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { expanded.value = true },
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
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    Modifier.background(color = Color.Transparent)
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            expanded.value = false
                            navController.navigate("JournalEdit/${journalId}")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            expanded.value = false
                            journal?.let {
                                journalViewModel.deleteJournal(it)
                                navController.popBackStack()
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding) // Apply innerPadding here
                .padding(16.dp)
                .fillMaxHeight() // Ensure the column fills the available height
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFF8FAFC), CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp).align(Alignment.Center)
                    )
                }

                Text(
                    text = journal?.title ?: "No Title",
                    style = Typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = formattedDate,
                style = Typography.bodyLarge,
                fontWeight = Typography.bodySmall.fontWeight,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
            )
            AsyncImage(
                model = journal?.imageUrl,
                contentDescription = "Journal Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Today Stories",
                        style = Typography.bodyLarge,
                        fontWeight = Typography.bodyLarge.fontWeight,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = journal?.story ?: "No Story",
                        style = Typography.bodyLarge,
                        fontWeight = Typography.bodySmall.fontWeight,
                        modifier = Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Justify
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Gratitude",
                        style = Typography.bodyLarge,
                        fontWeight = Typography.bodyLarge.fontWeight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = journal?.gratitude ?: "No Gratitude",
                        style = Typography.bodyLarge,
                        fontWeight = Typography.bodySmall.fontWeight,
                    )
                }
            }
        }
    }
}