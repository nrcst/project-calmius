package com.l5.calmius.feature.journaling.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import com.l5.calmius.features.journaling.data.JournalDatabase
import java.time.LocalDate
import kotlin.toString

@Composable
fun JournalAddScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val journalViewModel = JournalViewModel(
        JournalRepository(
            journalDao = JournalDatabase.getDatabase(context).journalDao()
        )
    )
    val title = remember { mutableStateOf("") }
    val story = remember { mutableStateOf("") }
    val gratitude = remember { mutableStateOf("") }
    val imageUrl = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Cancel") },
            text = { Text("Are you sure you want to cancel?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    navHostController.popBackStack()
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
                        text = "Write Your Stories",
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
            onValueChange = { title.value = it },
            label = "Title"
        )

        CustomTextField(
            value = story.value,
            onValueChange = { story.value = it },
            label = "How was your day?",
            maxLines = 10
        )

        CustomTextField(
            value = gratitude.value,
            onValueChange = { gratitude.value = it },
            label = "What are you grateful today?",
            maxLines = 10
        )

        Button(
            onClick = {
                val newJournal = JournalEntity(
                    date = getCurrentDate(),
                    title = title.value,
                    story = story.value,
                    gratitude = gratitude.value,
                    imageUrl = imageUrl.value
                )
                journalViewModel.viewModelScope.launch(Dispatchers.IO) {
                    journalViewModel.saveJournal(newJournal)
                    withContext(Dispatchers.Main) {
                        navHostController.popBackStack()
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Cyan, RoundedCornerShape(15.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            placeholder = {
                Text(text = label, color = Color.Gray)
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@SuppressLint("NewApi")
internal fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    return currentDate.toString()
}