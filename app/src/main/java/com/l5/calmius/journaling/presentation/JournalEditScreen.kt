package com.l5.calmius.feature.journaling.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.l5.calmius.feature.journaling.data.JournalEntity

@Composable
fun JournalEditScreen(
    journal: JournalEntity?,
    onSaveJournal: (JournalEntity) -> Unit,
    onDeleteJournal: (JournalEntity) -> Unit
) {
    val title = remember { mutableStateOf(journal?.title ?: "") }
    val story = remember { mutableStateOf(journal?.story ?: "") }
    val gratitude = remember { mutableStateOf(journal?.gratitude ?: "") }
    val imageUrl = remember { mutableStateOf(journal?.imageUrl ?: "") }

    Column {
        // Compose UI elements for editing journal data
        TextField(value = title.value, onValueChange = { title.value = it })
        TextField(value = story.value, onValueChange = { story.value = it })
        TextField(value = gratitude.value, onValueChange = { gratitude.value = it })
        TextField(value = imageUrl.value, onValueChange = { imageUrl.value = it })

        Button(onClick = {
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
            onSaveJournal(updatedJournal)
        }) {
            Text("Save")
        }

        if (journal != null) {
            Button(onClick = {
                onDeleteJournal(journal)
            }) {
                Text("Delete")
            }
        }
    }
}