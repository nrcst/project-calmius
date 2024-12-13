package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.presentation.CommunityViewModel
import com.l5.calmius.features.community.presentation.PostPreviewItem
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment

@Composable
fun SearchScreen(query: String, navController: NavController, viewModel: CommunityViewModel) {
    var currentQuery by remember { mutableStateOf(query) }
    val searchResults by viewModel.searchPosts(currentQuery).collectAsState(initial = emptyList())
    val searchHistory by viewModel.searchHistory.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = currentQuery,
            onValueChange = { currentQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Optional: Handle additional clicks if needed */ },
            leadingIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            trailingIcon = {
                IconButton(onClick = { currentQuery = "" }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                }
            },
            label = { Text("Search") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.saveSearchQuery(currentQuery)
                    // Trigger search
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (currentQuery.isEmpty()) {
            Text(
                text = "Search History",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            LazyColumn {
                items(searchHistory) { historyItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                currentQuery = historyItem
                                viewModel.saveSearchQuery(historyItem)
                                // Trigger search
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = historyItem)
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Search")
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults) { post -> 
                    PostPreviewItem(
                        post = post,
                        onClick = { navController.navigate("detailPost/${post.id}") },
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
