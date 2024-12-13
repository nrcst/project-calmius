package com.l5.calmius.features.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l5.calmius.features.community.data.Comment
import com.l5.calmius.features.community.data.Post
import com.l5.calmius.features.community.data.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class CommunityViewModel() : ViewModel() {
    private val repository: FirebaseRepository = FirebaseRepository()

    init {
        repository.initializeUsersCollection()
    }

    open val posts: StateFlow<List<Post>> = repository.getPosts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    open val searchHistory: StateFlow<List<String>> = repository.getSearchHistory()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _sortType = MutableStateFlow(SortType.RECENT)
    val sortType: StateFlow<SortType> = _sortType

    fun setSortType(sortType: SortType) {
        _sortType.value = sortType
    }

    fun getSortedPosts(): Flow<List<Post>> {
        return posts.map { postList ->
            when (_sortType.value) {
                SortType.RECENT -> postList.sortedByDescending { it.timestamp }
                SortType.LIKES -> postList.sortedByDescending { it.likes.size }
                SortType.MY_POSTS -> postList.filter { it.userId == getCurrentUserId() }
            }
        }
    }

    enum class SortType {
        RECENT, LIKES, MY_POSTS
    }

    fun createPost(title: String, content: String) {
        viewModelScope.launch {
            if (repository.isUserAuthenticated()) {
                repository.createPost(title, content)
            } else {
                // Handle unauthenticated user scenario
                // e.g., notify the UI to show a login prompt
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            if (repository.isUserAuthenticated()) {
                repository.likePost(postId)
            } else {
                // Handle unauthenticated user scenario
            }
        }
    }

    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            if (repository.isUserAuthenticated()) {
                repository.addComment(postId, content)
            } else {
                // Handle unauthenticated user scenario
            }
        }
    }

    fun saveSearchQuery(query: String) {
        viewModelScope.launch {
            if (repository.isUserAuthenticated()) {
                repository.saveSearchQuery(query)
            } else {
                // Handle unauthenticated user scenario
            }
        }
    }

    fun searchPosts(query: String): Flow<List<Post>> {
        return repository.searchPosts(query)
    }

    fun getPostById(postId: String): Flow<Post?> {
        return repository.getPostById(postId)
    }

    fun getComments(postId: String): Flow<List<Comment>> {
        return repository.getComments(postId)
    }

    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }
}