package com.l5.calmius.feature.journaling.presentation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.l5.calmius.ui.theme.Blue75
import com.l5.calmius.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(start = 46.dp, end = 46.dp, top = 26.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFF8FAFC), CircleShape)
                        .clickable { showDialog.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp).align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(26.dp))

                Text(
                    text = "Edit Your Stories",
                    style = Typography.displayLarge,
                    fontWeight = Typography.displayMedium.fontWeight,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                )
            }

            MyImageArea(
                uri = if (imageUrl.isNotEmpty()) Uri.parse(imageUrl) else null,
                onSetUri = { uri -> imageUrl = uri.toString() }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Blue75, RoundedCornerShape(15.dp))
                    .padding(16.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    placeholder = {
                        Text(text = "Title",
                            color = Color.Black,
                            style = Typography.bodyMedium)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

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
                            navController.navigate("journal")
                        },
                        modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue400),

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
                        navController.navigate("journal")
                    },
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue400)
                ) { Text("Submit") }
            }
        }
    }
}