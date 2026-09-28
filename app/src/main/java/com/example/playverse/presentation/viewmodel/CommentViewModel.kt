package com.example.playverse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playverse.domain.model.Comment
import com.example.playverse.domain.model.User
import com.example.playverse.domain.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewModel(
    private val repository: CommentRepository
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadComments(gameId: String) {
        if (gameId.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getCommentsForGame(gameId)
            _comments.value = result.getOrDefault(emptyList())
            _isLoading.value = false
        }
    }

    fun postComment(
        gameId: String,
        content: String,
        rating: Float,
        currentUser: User,
        onSuccess: () -> Unit = {}
    ) {
        if (content.isBlank() || gameId.isBlank()) return
        viewModelScope.launch {
            val result = repository.addComment(gameId, content, rating, currentUser)
            result.onSuccess {
                loadComments(gameId)
                onSuccess()
            }
        }
    }

    fun clearComments() {
        _comments.value = emptyList()
    }
}

class CommentViewModelFactory(
    private val repository: CommentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
