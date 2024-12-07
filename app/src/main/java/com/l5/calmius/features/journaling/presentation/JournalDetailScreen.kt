package com.l5.calmius.feature.journaling.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.l5.calmius.feature.journaling.data.JournalEntity
import coil.compose.AsyncImage

@Composable
fun JournalDetailScreen(
    journal: JournalEntity
) {
    Column {
        // Compose UI elements to display journal details
        Text(journal.title)
        Text(journal.date)
        AsyncImage(
            model = journal.imageUrl,
            contentDescription = "Journal Image"
        )
        Text(journal.story)
        Text(journal.gratitude)
    }
}