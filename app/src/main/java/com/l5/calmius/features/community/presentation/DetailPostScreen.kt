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
import com.l5.calmius.features.community.data.Comment
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.presentation.CommunityViewModel

@Composable
fun DetailPostScreen(postId: String, navController: NavController, viewModel: CommunityViewModel = viewModel()) {
    val post by viewModel.getPostById(postId).collectAsState(initial = null)
    val comments by viewModel.getComments(postId).collectAsState(initial = emptyList())
    var commentLimit by remember { mutableStateOf(3) }

    post?.let {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = "By ${it.userName}")
            Text(text = it.content)
            Row {
                Button(onClick = { 
                    viewModel.likePost(postId) 
                }) {
                    Text("Like")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { 
                    navController.navigate("postComment/$postId") 
                }) {
                    Text("Comment")
                }
            }
            Text(text = "Comments", style = MaterialTheme.typography.headlineSmall)
            LazyColumn {
                items(comments.take(commentLimit)) { comment ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(text = comment.userName, style = MaterialTheme.typography.bodySmall)
                        Text(text = comment.content)
                    }
                }
                if (comments.size > commentLimit) {
                    item {
                        TextButton(onClick = { commentLimit += 3 }) {
                            Text("Load More")
                        }
                    }
                }
            }
        }
    } ?: run {
        Text("Loading post details...")
    }
}
