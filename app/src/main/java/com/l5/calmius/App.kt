package com.l5.calmius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.presentation.JournalAddScreen
import com.l5.calmius.ui.theme.CalmiusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalmiusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JournalAddScreen(
                        onSaveJournal = { journal ->
                            saveJournal(journal)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun saveJournal(journal: JournalEntity) {
        // Implement the logic to save the journal entry
    }
}

@Preview(showBackground = true)
@Composable
fun JournalAddScreenPreview() {
    CalmiusTheme {
        JournalAddScreen(onSaveJournal = {})
    }
}