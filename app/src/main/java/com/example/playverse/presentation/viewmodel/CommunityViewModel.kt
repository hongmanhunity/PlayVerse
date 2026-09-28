package com.example.playverse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playverse.domain.model.NotificationItem
import com.example.playverse.domain.model.Post
import com.example.playverse.domain.repository.CommunityRepository
import com.example.playverse.presentation.state.CommunityUiState
import com.example.playverse.presentation.state.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val repository: CommunityRepository
) : ViewModel() {

    private val _postsState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val postsState = _postsState.asStateFlow()

    private val _notificationsState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val notificationsState = _notificationsState.asStateFlow()

    init {
        loadPosts()
        loadNotifications()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _postsState.value = CommunityUiState.Loading
            val result = repository.getPosts()
            result.onSuccess { posts ->
                _postsState.value = CommunityUiState.Success(posts)
            }.onFailure { err ->
                _postsState.value = CommunityUiState.Error(err.message ?: "Không thể tải bài viết")
            }
        }
    }

    fun createPost(token: String, title: String, content: String, gameTitle: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.createPost(token, title, content, gameTitle)
            result.onSuccess {
                loadPosts()
                onComplete(true)
            }.onFailure {
                onComplete(false)
            }
        }
    }

    fun toggleLike(token: String, postId: String) {
        viewModelScope.launch {
            val result = repository.toggleLike(token, postId)
            if (result.isSuccess) {
                loadPosts()
            }
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _notificationsState.value = NotificationUiState.Loading
            val result = repository.getNotifications()
            result.onSuccess { notifications ->
                _notificationsState.value = NotificationUiState.Success(notifications)
            }.onFailure { err ->
                _notificationsState.value = NotificationUiState.Error(err.message ?: "Không thể tải thông báo")
            }
        }
    }
}

class CommunityViewModelFactory(
    private val repository: CommunityRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommunityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
