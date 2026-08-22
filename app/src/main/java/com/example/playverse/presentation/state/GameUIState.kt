package com.example.playverse.presentation.state

import com.example.playverse.domain.model.Banner
import com.example.playverse.domain.model.Game

sealed class GameUiState {

    data object Loading : GameUiState()

    data class Success(
        val games: List<Game>,
        val popularGames: List<Game> = emptyList(),
        val flashGames: List<Game> = emptyList(),
        val banners: List<Banner> = emptyList()
    ) : GameUiState()

    data class Error(
        val message: String
    ) : GameUiState()
}