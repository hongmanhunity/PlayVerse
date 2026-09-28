package com.example.playverse.domain.repository

import com.example.playverse.domain.model.Game

interface WishlistRepository {
    suspend fun getWishlist(token: String): Result<List<Game>>
    suspend fun toggleWishlist(game: Game, token: String): Result<Boolean>
    suspend fun isWishlisted(gameId: String): Boolean
    fun clearLocalWishlist()
}
