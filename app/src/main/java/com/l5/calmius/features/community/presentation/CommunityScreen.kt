package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.presentation.CommunityViewModel

@Composable
fun CommunityScreen(viewModel: CommunityViewModel = viewModel()) {
    val posts by viewModel.posts.collectAsState()

    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("What are you thinking about?") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (content.isNotEmpty()) {
                    viewModel.createPost(content)
                    content = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Posting")
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(posts) { post ->
                PostItem(post = post, onLike = { viewModel.likePost(post.id) })
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
                    Text("Likes")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    PostItem(post = Post(
        id = "1",
        content = "This is a sample post.",
        likes = listOf("user1", "user2")
    ), onLike = {  })
}