package com.l5.calmius.feature.journaling.presentation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import com.l5.calmius.ui.theme.Blue400
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun JournalEditScreen(
    navController: NavController,
    journalId: Long,
    modifier: Modifier = Modifier,
    viewModel: JournalViewModel
) {
    val journals by viewModel.journals.collectAsState()

    // Local states
    var title by remember { mutableStateOf("") }
    var story by remember { mutableStateOf("") }
    var gratitude by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    // Load the journal when the screen is displayed
    LaunchedEffect(journalId) {
        viewModel.getJournalById(journalId) { journal ->
            journal?.let {
                title = it.title
                story = it.story
                gratitude = it.gratitude
                imageUrl = it.imageUrl ?: ""
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Cancel") },
            text = { Text("Are you sure you want to cancel?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    navController.popBackStack()
                }) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) { Text("No") }
            }
        )
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Button(
                onClick = { showDialog.value = true },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.Black
                ),
                modifier = Modifier.padding(8.dp).size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Edit Your Stories",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
            )
        }

        MyImageArea(
            uri = if (imageUrl.isNotEmpty()) Uri.parse(imageUrl) else null,
            onSetUri = { uri -> imageUrl = uri.toString() }
        )

        CustomTextField(
            value = title,
            onValueChange = { title = it },
            label = "Title"
        )

        CustomTextField(
            value = story,
            onValueChange = { story = it },
            label = "How was your day?",
            maxLines = 10
        )

        CustomTextField(
            value = gratitude,
            onValueChange = { gratitude = it },
            label = "What are you grateful today?",
            maxLines = 10
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val journal = journals.find { it.id == journalId }
            val journalViewModel = viewModel

            if (journal != null) {
                Button(
                    onClick = {
                        journalViewModel.deleteJournal(journal)
                        navController.navigate("JournalList")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Blue400)
                ) { Text("Delete") }
            }

            Button(
                onClick = {
                    val updatedJournal = journal?.copy(
                        title = title,
                        story = story,
                        gratitude = gratitude,
                        imageUrl = imageUrl
                    ) ?: JournalEntity(
                        date = getCurrentDate(),
                        title = title,
                        story = story,
                        gratitude = gratitude,
                        imageUrl = imageUrl
                    )
                    journalViewModel.saveJournal(updatedJournal)
                    navController.navigate("JournalList")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Blue400)
            ) { Text("Submit") }
        }
    }
}