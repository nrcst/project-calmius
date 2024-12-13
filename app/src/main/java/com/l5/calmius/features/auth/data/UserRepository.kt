package com.l5.calmius.features.auth.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun saveUser(
        userId: String,
        fullName: String,
        phoneNum: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = mapOf(
            "id" to userId,
            "name" to fullName,
            "phone" to phoneNum,
            "email" to email
        )

        firestore.collection("users")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure("Failed to save user: ${exception.message}")
            }
    }

    fun getUser(userId: String, onSuccess: (Map<String, Any?>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data ?: emptyMap())
                } else {
                    onFailure("User not found")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "An error occurred while fetching user data")
            }
    }
}