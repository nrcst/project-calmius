package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.data.Comment
import com.l5.calmius.features.community.data.FirebaseRepository
import com.l5.calmius.ui.theme.CalmiusTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.Timestamp

@Composable
fun CommunityScreen(navController: NavController, viewModel: CommunityViewModel) {
    val posts by viewModel.posts.collectAsState()

    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Search button
        Button(onClick = { navController.navigate("searchBarAndHistory") }) {
            Text("Search")
        }

        // Sorting buttons
        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp)) {
            // ...sorting buttons...
        }

        // Guidelines box
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
            Text("Community Guidelines: ...")
        }

        // Create post button
        Button(onClick = { navController.navigate("createPost") }) {
            Text("Create Post")
        }

        // List of posts
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(posts) { post ->
                PostPreviewItem(post = post, onClick = { navController.navigate("detailPost/${post.id}") })
            }
        }
    }
}

@Composable
fun PostPreviewItem(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = post.title)
            Text(text = post.content.take(100) + "...")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "By ${post.userName}")
                Row {
                    Text(text = "${post.likes.size} Likes")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${post.comments.size} Comments")
                }
            }
        }
    }
}

@Composable
fun PostItem(post: Post, onLike: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = post.content)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${post.likes.size} Likes")
                Button(onClick = onLike) {
                    Text("Like")
                }
            }
        }
    }
}
