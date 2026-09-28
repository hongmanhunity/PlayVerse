package com.example.playverse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playverse.domain.model.Game
import com.example.playverse.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val repository: WishlistRepository
) : ViewModel() {

    private val _wishlistGames = MutableStateFlow<List<Game>>(emptyList())
    val wishlistGames = _wishlistGames.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadWishlist(token: String) {
        if (token.isBlank()) {
            _wishlistGames.value = emptyList()
            repository.clearLocalWishlist()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getWishlist(token)
            _wishlistGames.value = result.getOrDefault(emptyList())
            _isLoading.value = false
        }
    }

    fun toggleWishlist(game: Game, token: String, onUnauthenticated: () -> Unit = {}) {
        if (token.isBlank()) {
            onUnauthenticated()
            return
        }

        viewModelScope.launch {
            repository.toggleWishlist(game, token)
            val result = repository.getWishlist(token)
            _wishlistGames.value = result.getOrDefault(emptyList())
        }
    }

    fun clearWishlist() {
        _wishlistGames.value = emptyList()
        repository.clearLocalWishlist()
    }

    fun isFavorite(gameId: String): Boolean {
        return _wishlistGames.value.any { it.id == gameId }
    }
}

class WishlistViewModelFactory(
    private val repository: WishlistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WishlistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
