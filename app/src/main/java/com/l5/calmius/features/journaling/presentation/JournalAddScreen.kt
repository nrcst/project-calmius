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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import com.l5.calmius.features.journaling.data.JournalDatabase
import com.l5.calmius.ui.theme.Blue400
import com.l5.calmius.ui.theme.Blue75
import com.l5.calmius.ui.theme.Typography
import java.time.LocalDate
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
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
    val isFormValid = remember { mutableStateOf(false) }

    // Update form validity whenever any field changes
    fun updateFormValidity() {
        isFormValid.value = title.value.isNotEmpty() && story.value.isNotEmpty() && gratitude.value.isNotEmpty()
    }

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

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(start = 46.dp, end = 46.dp, top = 26.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Write Your Stories",
                            style = Typography.displayLarge,
                            fontWeight = Typography.displayMedium.fontWeight
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Today",
                            style = Typography.displayLarge,
                            fontWeight = Typography.displayMedium.fontWeight
                        )
                    }
                }
            }
            MyImageArea(
                uri = if (imageUrl.value.isNotEmpty()) Uri.parse(imageUrl.value) else null,
                onSetUri = { uri -> imageUrl.value = uri.toString() }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Blue75, RoundedCornerShape(15.dp))
                    .padding(16.dp)
            ) {
                TextField(
                    value = title.value,
                    onValueChange = {
                        title.value = it
                        updateFormValidity()
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
                value = story.value,
                onValueChange = {
                    story.value = it
                    updateFormValidity()
                },
                label = "How was your day?",
                maxLines = 10
            )

            CustomTextField(
                value = gratitude.value,
                onValueChange = {
                    gratitude.value = it
                    updateFormValidity()
                },
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
                colors = ButtonDefaults.buttonColors(containerColor = Blue400),
                modifier = Modifier.align(Alignment.CenterHorizontally).width(200.dp),
                enabled = isFormValid.value
            ) {
                Text("Submit")
            }
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
            .height(150.dp)
            .background(Blue75, RoundedCornerShape(15.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            placeholder = {
                Text(text = label,
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
}

@SuppressLint("NewApi")
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    return currentDate.toString()
}