package com.l5.calmius.features.community.data

data class Post(
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
)