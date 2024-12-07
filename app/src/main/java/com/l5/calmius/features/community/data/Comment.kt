package com.l5.calmius.features.community.data

data class Comment(
    val id: String = "",
    val postId: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)