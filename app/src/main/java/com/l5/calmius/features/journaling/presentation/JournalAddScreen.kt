package com.l5.calmius.feature.journaling.presentation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l5.calmius.feature.journaling.data.JournalEntity
import java.time.LocalDate

@Composable
fun JournalAddScreen(
    onSaveJournal: (JournalEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = remember { mutableStateOf("") }
    val story = remember { mutableStateOf("") }
    val gratitude = remember { mutableStateOf("") }
    val imageUrl = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MyImageArea(
            uri = if (imageUrl.value.isNotEmpty()) Uri.parse(imageUrl.value) else null,
            onSetUri = { uri -> imageUrl.value = uri.toString() }
        )

        TextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )


        TextField(
            value = story.value,
            onValueChange = { story.value = it },
            label = { Text("Story") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        TextField(
            value = gratitude.value,
            onValueChange = { gratitude.value = it },
            label = { Text("Gratitude") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = imageUrl.value,
            onValueChange = { imageUrl.value = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
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
                onSaveJournal(newJournal)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}

@SuppressLint("NewApi")
private fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    return currentDate.toString()
}

@Preview(showBackground = true)
@Composable
fun PreviewJournalAddScreen() {
    JournalAddScreen(onSaveJournal = {})
}