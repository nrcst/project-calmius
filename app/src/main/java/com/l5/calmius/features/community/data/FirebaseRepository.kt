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

class FirebaseRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun createPost(content: String) {
        val postId = firestore.collection("posts").document().id
        val post = Post(
            id = postId,
            userId = getCurrentUserId() ?: "",
            content = content,
            timestamp = System.currentTimeMillis()
        )
        firestore.collection("posts").document(postId).set(post)
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
            timestamp = System.currentTimeMillis()
        )
        firestore.collection("posts").document(postId)
            .collection("comments").document(commentId).set(comment)
    }

    fun getPosts(): Flow<List<Post>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val posts = snapshot?.toObjects(Post::class.java) ?: emptyList()
                trySend(posts)
            }
        awaitClose { listener.remove() }
    }
}