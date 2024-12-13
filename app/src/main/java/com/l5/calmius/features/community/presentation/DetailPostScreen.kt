package com.l5.calmius.features.community.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.l5.calmius.R
import com.l5.calmius.features.community.data.Comment
import com.l5.calmius.features.community.presentation.CommunityViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DetailPostScreen(postId: String, navController: NavController, viewModel: CommunityViewModel = viewModel()) {
    val post by viewModel.getPostById(postId).collectAsState(initial = null)
    val comments by viewModel.getComments(postId).collectAsState(initial = emptyList())
    var commentLimit by remember { mutableStateOf(3) }
    val currentUser = viewModel.getCurrentUserId()
    var isLiked by remember { mutableStateOf(post?.likes?.contains(currentUser) == true) }
    var likeCount by remember { mutableStateOf(post?.likes?.size ?: 0) }
    var commentCount by remember { mutableStateOf(post?.comments?.size ?: 0) }

    post?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Post",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Start
                    ),
                    fontSize = 35.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F1FC))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${it.userName} - ${getPostDate(it.timestamp)}", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(text = it.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = it.content, style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(onClick = { 
                                viewModel.likePost(postId)
                                isLiked = !isLiked
                                likeCount += if (isLiked) 1 else -1
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Like",
                                    tint = if (isLiked) Color.Red else Color.Gray
                                )
                            }
                            Text(text = "$likeCount")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { navController.navigate("postComment/$postId") }) {
                            Icon(imageVector = Icons.Filled.Create, contentDescription = "Comment")
                            Text(text = "Comment")
                        }
                    }
                }
            }

            Text(
                text = "Comments",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            LazyColumn {
                items(comments.take(commentLimit)) { comment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F1FC)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "${comment.userName} - ${getCommentDate(comment.timestamp)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = comment.content,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                if (comments.size > commentLimit) {
                    item {
                        TextButton(onClick = { 
                            commentLimit += 3 
                            commentCount = comments.size
                        }) {
                            Text("Load More")
                        }
                    }
                }
            }
        }

        LaunchedEffect(comments) {
            commentCount = comments.size
        }
    } ?: run {
        Text("Loading post details...")
    }
}

fun getCommentDate(timestamp: Timestamp): String {
    val commentDate = timestamp.toDate()
    val now = Date()
    
    val diffInMillis = now.time - commentDate.time
    val diffInMinutes = diffInMillis / (60 * 1000)
    val diffInHours = diffInMillis / (60 * 60 * 1000)
    val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)
    
    return when {
        diffInMinutes < 1 -> "Just now"
        diffInMinutes < 60 -> "$diffInMinutes minute(s) ago"
        diffInHours < 24 -> "$diffInHours hour(s) ago"
        diffInDays <= 7 -> "$diffInDays day(s) ago"
        else -> {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            sdf.format(commentDate)
        }
    }
}
