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
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.presentation.CommunityViewModel
import com.l5.calmius.features.community.presentation.PostPreviewItem

@Composable
fun SearchScreen(query: String, navController: NavController, viewModel: CommunityViewModel = viewModel()) {
    val searchResults by viewModel.searchPosts(query).collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(searchResults) { post ->
            PostPreviewItem(post = post, onClick = { 
                // Verify post ID is not empty before navigating
                if (post.id.isNotEmpty()) {
                    navController.navigate("detailPost/${post.id}") 
                }
            })
        }
    }
}
