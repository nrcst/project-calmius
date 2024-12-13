package com.l5.calmius.features.community.data

import com.google.firebase.Timestamp

data class User(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val timestamp: Timestamp = Timestamp.now()
)