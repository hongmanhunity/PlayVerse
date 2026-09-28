package com.example.playverse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playverse.data.repository.BannerRepositoryImpl
import com.example.playverse.data.repository.GameRepositoryImpl
import com.example.playverse.domain.repository.BannerRepository
import com.example.playverse.presentation.state.GameDetailUiState
import com.example.playverse.presentation.state.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val repositoryImpl: GameRepositoryImpl,
    private val bannerRepository: BannerRepository = BannerRepositoryImpl()
): ViewModel() {
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Idle)
    val detailUiState = _detailUiState.asStateFlow()

    init {
        getGames()
    }

    fun getGames() {
        viewModelScope.launch {
            _uiState.value = GameUiState.Loading
            try {
                val games = repositoryImpl.getGames()
                val popular = try { repositoryImpl.getPopularGames() } catch (e: Exception) { emptyList() }
                val flash = try { repositoryImpl.getFlashGames() } catch (e: Exception) { emptyList() }
                val banners = bannerRepository.getBanners().getOrDefault(emptyList())

                _uiState.value = GameUiState.Success(
                    games = games,
                    popularGames = if (popular.isNotEmpty()) popular else games.take(8),
                    flashGames = if (flash.isNotEmpty()) flash else games.takeLast(4),
                    banners = banners
                )
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error(e.message ?: "Lỗi kết nối API!")
            }
        }
    }

    fun getGameById(id: String) {
        viewModelScope.launch {
            _detailUiState.value = GameDetailUiState.Loading
            try {
                val game = repositoryImpl.getGameById(id)
                if (game != null) {
                    _detailUiState.value = GameDetailUiState.Success(game)
                } else {
                    _detailUiState.value = GameDetailUiState.Error("Không tìm thấy game này!")
                }
            } catch (e: Exception) {
                _detailUiState.value = GameDetailUiState.Error(e.message ?: "Lỗi kết nối API!")
            }
        }
    }
}

class GameViewModelFactory(
    private val repositoryImpl: GameRepositoryImpl
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}