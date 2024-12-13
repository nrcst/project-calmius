package com.l5.calmius.features.auth.data

data class State (
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)