package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CreatePostScreen(navController: NavController, viewModel: CommunityViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showDiscardDialog by remember { mutableStateOf(false) }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard Post") },
            text = { Text("Are you sure you want to discard your post?") },
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
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    viewModel.createPost(title, content)
                    navController.popBackStack()
                } else {
                    // Optionally show a message to the user
                }
            }) {
                Text("Post")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (title.isNotEmpty() || content.isNotEmpty()) {
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