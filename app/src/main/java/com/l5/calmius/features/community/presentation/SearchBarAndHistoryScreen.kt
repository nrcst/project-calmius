package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchBarAndHistoryScreen(navController: NavController, viewModel: CommunityViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchHistory by viewModel.searchHistory.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            viewModel.saveSearchQuery(query)
            navController.navigate("searchScreen/$query")
        }) {
            Text("Search")
        }
        Text("Search History", style = MaterialTheme.typography.headlineSmall)
        LazyColumn {
            items(searchHistory) { historyItem ->
                Text(text = historyItem, modifier = Modifier.clickable {
                    navController.navigate("searchScreen/$historyItem")
                })
            }
        }
    }
}
