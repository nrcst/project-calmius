package com.l5.calmius.features.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.data.FirebaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CommunityViewModel(private val repository: FirebaseRepository) : ViewModel() {

    val posts: StateFlow<List<Post>> = repository.getPosts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun createPost(content: String) {
        viewModelScope.launch {
            repository.createPost(content)
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            repository.likePost(postId)
        }
    }

    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            repository.addComment(postId, content)
        }
    }
}