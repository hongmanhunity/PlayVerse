package com.example.playverse.presentation.state

import com.example.playverse.domain.model.Game

sealed class GameDetailUiState {
    data object Idle : GameDetailUiState()
    data object Loading : GameDetailUiState()
    data class Success(val game: Game) : GameDetailUiState()
    data class Error(val message: String) : GameDetailUiState()
}
