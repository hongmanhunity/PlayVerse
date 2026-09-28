package com.example.playverse.presentation.state

import com.example.playverse.domain.model.NotificationItem

sealed interface NotificationUiState {
    object Loading : NotificationUiState
    data class Success(val notifications: List<NotificationItem>) : NotificationUiState
    data class Error(val message: String) : NotificationUiState
}
