package com.example.playverse.presentation.state

import com.example.playverse.domain.model.User

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: User, val message: String) : AuthUiState
    data class Error(val errorMessage: String) : AuthUiState
}
