package com.l5.calmius.feature.journaling.presentation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import com.l5.calmius.features.journaling.data.JournalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun JournalEditScreen(
    navController: NavController,
    journalId: Long,
    modifier: Modifier = Modifier
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
    val title = remember { mutableStateOf(journal?.title ?: "") }
    val story = remember { mutableStateOf(journal?.story ?: "") }
    val gratitude = remember { mutableStateOf(journal?.gratitude ?: "") }
    val imageUrl = remember { mutableStateOf(journal?.imageUrl ?: "") }
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Cancel") },
            text = { Text("Are you sure you want to cancel?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    navController.popBackStack()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Button(
                onClick = {
                    showDialog.value = true
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Edit Your Stories",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Today",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        MyImageArea(
            uri = if (imageUrl.value.isNotEmpty()) Uri.parse(imageUrl.value) else null,
            onSetUri = { uri -> imageUrl.value = uri.toString() }
        )

        CustomTextField(
            value = title.value,
            onValueChange = { newValue: String -> title.value = newValue },
            label = "Title"
        )

        CustomTextField(
            value = story.value,
            onValueChange = { newValue: String -> story.value = newValue },
            label = "How was your day?",
            maxLines = 10
        )

        CustomTextField(
            value = gratitude.value,
            onValueChange = { newValue: String -> gratitude.value = newValue },
            label = "What are you grateful today?",
            maxLines = 10
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (journal != null) {
                Button(
                    onClick = {
                        journalViewModel.deleteJournal(journal)
                        navController.navigate("JournalList")
                    }
                ) {
                    Text("Delete")
                }
            }

            Button(
                onClick = {
                    val updatedJournal = journal?.copy(
                        title = title.value,
                        story = story.value,
                        gratitude = gratitude.value,
                        imageUrl = imageUrl.value
                    ) ?: JournalEntity(
                        date = getCurrentDate(),
                        title = title.value,
                        story = story.value,
                        gratitude = gratitude.value,
                        imageUrl = imageUrl.value
                    )
                    journalViewModel.viewModelScope.launch(Dispatchers.IO) {
                        journalViewModel.saveJournal(updatedJournal)
                        withContext(Dispatchers.Main) {
                            navController.navigate("JournalList")
                        }
                    }
                }
            ) {
                Text("Submit")
            }
        }
    }
}