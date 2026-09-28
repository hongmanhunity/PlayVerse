package com.example.playverse.presentation.state

import com.example.playverse.domain.model.Post

sealed interface CommunityUiState {
    object Loading : CommunityUiState
    data class Success(val posts: List<Post>) : CommunityUiState
    data class Error(val message: String) : CommunityUiState
}
