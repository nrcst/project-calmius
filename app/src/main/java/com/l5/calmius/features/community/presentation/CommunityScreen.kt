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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import com.l5.calmius.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.text.style.TextOverflow
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

@Composable
fun CommunityScreen(navController: NavController, viewModel: CommunityViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Community",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Start
                ),
                fontSize = 35.sp
            )
            IconButton(onClick = { navController.navigate("searchScreen") }) { 
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }

        Text(
            text = "Connect and share with others",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontSize = 16.sp
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Blue400),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Community Guidelines",
                    fontSize = 20.sp,
                    color = White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Be respectful and follow the rules.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = White
                    ),
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("createPost") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Create Post")
                }
            }
        }

        val sortType by viewModel.sortType.collectAsState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SortButton(
                text = "Recent",
                isActive = sortType == CommunityViewModel.SortType.RECENT,
                onClick = { viewModel.setSortType(CommunityViewModel.SortType.RECENT) }
            )
            SortButton(
                text = "Likes",
                isActive = sortType == CommunityViewModel.SortType.LIKES,
                onClick = { viewModel.setSortType(CommunityViewModel.SortType.LIKES) }
            )
            SortButton(
                text = "My Posts",
                isActive = sortType == CommunityViewModel.SortType.MY_POSTS,
                onClick = { viewModel.setSortType(CommunityViewModel.SortType.MY_POSTS) }
            )
        }

        val posts by viewModel.getSortedPosts().collectAsState(initial = emptyList())
        var postLimit by remember { mutableStateOf(5) }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(posts.take(postLimit)) { post ->
                PostPreviewItem(
                    post = post,
                    onClick = { navController.navigate("detailPost/${post.id}") },
                    navController = navController,
                    viewModel = viewModel
                )
            }
            if (posts.size > postLimit) {
                item {
                    TextButton(onClick = { postLimit += 5 }) {
                        Text("Load More")
                    }
                }
            }
        }
    }
}

@Composable
fun PostPreviewItem(
    post: Post,
    onClick: () -> Unit,
    navController: NavController,
    viewModel: CommunityViewModel
) {
    var isLiked by remember { mutableStateOf(post.likes.contains(viewModel.getCurrentUserId())) }
    var likeCount by remember { mutableStateOf(post.likes.size) }
    var commentCount by remember { mutableStateOf(post.comments.size) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Blue75)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${post.userName} - ${getPostDate(post.timestamp)}", style = MaterialTheme.typography.bodySmall)
            }
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Text(text = post.content, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
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
                        viewModel.likePost(post.id)
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
                Button(onClick = { navController.navigate("postComment/${post.id}") }) {
                    Icon(imageVector = Icons.Filled.Create, contentDescription = "Comment")
                    Text(text = "Comment")
                }
            }
        }
    }
    LaunchedEffect(post.comments) {
        commentCount = post.comments.size
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

@Composable
fun SortButton(text: String, isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Blue400 else Color.Transparent,
            contentColor = if (isActive) White else Blue400
        ),
        border = if (!isActive) BorderStroke(1.dp, Blue400) else null,
        shape = RoundedCornerShape(50)
    ) {
        Text(text)
    }
}

fun getPostDate(timestamp: Timestamp): String {
    val postDate = timestamp.toDate()
    val now = Date()
    
    val diffInMillis = now.time - postDate.time
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
            sdf.format(postDate)
        }
    }
}