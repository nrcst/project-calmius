package com.l5.calmius.feature.journaling.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.l5.calmius.feature.journaling.data.JournalEntity

@Composable
fun JournalListScreen(
    journals: List<JournalEntity>,
    onJournalClick: (Long) -> Unit
) {
    LazyColumn {
        items(journals) { journal ->
            JournalListItem(
                journal = journal,
                onClick = { onJournalClick(journal.id.toLong()) }
            )
        }
    }
}

@Composable
fun JournalListItem(
    journal: JournalEntity,
    onClick: () -> Unit
) {
    // Compose UI elements to display journal data
}