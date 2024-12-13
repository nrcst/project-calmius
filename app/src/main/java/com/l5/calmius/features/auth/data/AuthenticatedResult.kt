package com.l5.calmius.features.auth.data

data class AuthenticatedResult (
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val phoneNumber: String?,
    val email: String?,
    val password: String?,
    val profilePictureUrl: String?
)