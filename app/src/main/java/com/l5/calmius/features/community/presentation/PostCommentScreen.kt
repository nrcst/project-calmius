package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PostCommentScreen(postId: String, navController: NavController, viewModel: CommunityViewModel = viewModel()) {
    var content by remember { mutableStateOf("") }
    var showDiscardDialog by remember { mutableStateOf(false) }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard Comment") },
            text = { Text("Are you sure you want to discard your comment?") },
            confirmButton = {
                TextButton(onClick = { 
                    showDiscardDialog = false
                    navController.popBackStack()
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        Row {
            Button(onClick = {
                if (content.isNotEmpty()) {
                    viewModel.addComment(postId, content)
                    navController.popBackStack()
                } else {
                    // Optionally show a message to the user
                }
            }) {
                Text("Post")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (content.isNotEmpty()) {
                    showDiscardDialog = true
                } else {
                    navController.popBackStack()
                }
            }) {
                Text("Cancel")
            }
        }
    }
}
