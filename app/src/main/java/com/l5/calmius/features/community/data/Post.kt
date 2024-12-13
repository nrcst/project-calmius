package com.l5.calmius.features.community.data

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val keywords: List<String> = emptyList()
)