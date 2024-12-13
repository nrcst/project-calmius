package com.l5.calmius.features.community.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.l5.calmius.features.community.data.Comment
import com.l5.calmius.features.community.data.Post
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration

class FirebaseRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUserName(): String {
        return auth.currentUser?.displayName ?: "Anonymous"
    }

    fun createPost(title: String, content: String) {
        val postId = firestore.collection("posts").document().id
        val post = Post(
            id = postId,
            userId = getCurrentUserId() ?: "",
            userName = getCurrentUserName(),
            title = title,
            content = content,
            timestamp = Timestamp.now(),
            keywords = extractKeywords(content, title)
        )
        firestore.collection("posts").document(postId).set(post)
    }

    private fun extractKeywords(content: String, title: String): List<String> {
        val contentKeywords = content.split("\\s+".toRegex()).map { it.trim().lowercase() }
        val titleKeywords = title.split("\\s+".toRegex()).map { it.trim().lowercase() }
        return (contentKeywords + titleKeywords).distinct()
    }

    fun likePost(postId: String) {
        val userId = getCurrentUserId() ?: return
        val postRef = firestore.collection("posts").document(postId)
        postRef.update("likes", FieldValue.arrayUnion(userId))
    }

    fun addComment(postId: String, content: String) {
        val commentId = firestore.collection("posts").document(postId)
            .collection("comments").document().id
        val comment = Comment(
            id = commentId,
            postId = postId,
            userId = getCurrentUserId() ?: "",
            content = content,
            timestamp = Timestamp.now()
        )
        firestore.collection("posts").document(postId)
            .collection("comments").document(commentId).set(comment)
    }

    fun getPosts(): Flow<List<Post>> = callbackFlow {
        if (auth.currentUser == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }

        val registration: ListenerRegistration = firestore.collection("posts")
            .orderBy("__name__")
            .limit(10) // Adjust the limit as needed
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val posts = snapshot?.documents?.mapNotNull { it.toObject(Post::class.java) } ?: emptyList()
                trySend(posts).isSuccess
            }

        awaitClose {
            registration.remove()
        }
    }

    // Search history
    fun saveSearchQuery(query: String) {
        val userId = getCurrentUserId() ?: return
        val historyRef = firestore.collection("users").document(userId).collection("searchHistory")
        historyRef.add(mapOf(
            "query" to query,
            "timestamp" to Timestamp.now()
        ))
    }

    fun getSearchHistory(): Flow<List<String>> = callbackFlow {
        val userId = getCurrentUserId() ?: return@callbackFlow
        val listener = firestore.collection("users").document(userId)
            .collection("searchHistory")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val history = snapshot?.documents?.map { it.getString("query") ?: "" } ?: emptyList()
                trySend(history)
            }
        awaitClose { listener.remove() }
    }

    fun searchPosts(query: String): Flow<List<Post>> = callbackFlow {
        if (auth.currentUser == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }

        val registration: ListenerRegistration = firestore.collection("posts")
            .whereArrayContains("keywords", query.toLowerCase())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val results = snapshot?.documents?.mapNotNull { it.toObject(Post::class.java) } ?: emptyList()
                trySend(results).isSuccess
            }

        awaitClose {
            registration.remove()
        }
    }

    fun getPostById(postId: String): Flow<Post?> = callbackFlow {
        val listener = firestore.collection("posts").document(postId)
            .addSnapshotListener { snapshot, _ ->
                val post = snapshot?.toObject(Post::class.java)
                trySend(post)
            }
        awaitClose { listener.remove() }
    }

    // Get comments for a post
    fun getComments(postId: String): Flow<List<Comment>> = callbackFlow {
        val registration: ListenerRegistration = firestore.collection("posts").document(postId)
            .collection("comments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val comments = snapshot?.documents?.mapNotNull { it.toObject(Comment::class.java) } ?: emptyList()
                trySend(comments).isSuccess
            }

        awaitClose {
            registration.remove()
        }
    }

    fun createUser(displayName: String, email: String) {
        val userId = getCurrentUserId() ?: return
        val user = User(
            id = userId,
            displayName = displayName,
            email = email,
            timestamp = Timestamp.now()
        )
        firestore.collection("users").document(userId).set(user)
    }

    fun registerUser(displayName: String, email: String) {
        // Implement Firebase Authentication registration logic here
        // After successful registration, call createUser
        createUser(displayName, email)
    }

    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun getUser(userId: String): Flow<User?> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObject(User::class.java)
                trySend(user)
            }
        awaitClose { listener.remove() }
    }

    fun initializeUsersCollection() {
        if (auth.currentUser == null) {
            // Anonymous sign-in removed to prevent permission issues
            // Ensure the user is authenticated before calling this method
        } else {
            createUser(getCurrentUserName(), auth.currentUser?.email ?: "")
        }
    }
}