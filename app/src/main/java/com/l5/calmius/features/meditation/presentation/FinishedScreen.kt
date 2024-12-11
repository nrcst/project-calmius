package com.l5.calmius.features.meditation.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FinishedScreen(onReturnHome: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "You’ve finished the meditation!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onReturnHome) {
            Text(text = "Return to Home")
        }
    }
}
