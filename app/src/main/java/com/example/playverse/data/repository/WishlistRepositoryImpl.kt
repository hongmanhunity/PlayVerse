package com.example.playverse.data.repository

import android.content.Context
import com.example.playverse.data.mapper.toDomain
import com.example.playverse.data.remote.api.WishlistApiService
import com.example.playverse.domain.model.Game
import com.example.playverse.domain.repository.WishlistRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishlistRepositoryImpl(
    private val wishlistApiService: WishlistApiService,
    private val context: Context
) : WishlistRepository {

    private val prefs = context.getSharedPreferences("playverse_wishlist_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    override suspend fun getWishlist(token: String): Result<List<Game>> {
        return withContext(Dispatchers.IO) {
            try {
                if (token.isNotBlank()) {
                    val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"
                    val response = wishlistApiService.getWishlist(authHeader)
                    if (response.isSuccessful && response.body()?.data != null) {
                        val games = response.body()!!.data!!.map { it.toDomain() }
                        saveLocalWishlist(games)
                        return@withContext Result.success(games)
                    }
                    Result.success(getLocalWishlist())
                } else {
                    // Chưa đăng nhập thì danh sách yêu thích luôn là rỗng
                    Result.success(emptyList())
                }
            } catch (e: Exception) {
                if (token.isNotBlank()) {
                    Result.success(getLocalWishlist())
                } else {
                    Result.success(emptyList())
                }
            }
        }
    }

    override suspend fun toggleWishlist(game: Game, token: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            if (token.isBlank()) {
                return@withContext Result.failure(Exception("Vui lòng đăng nhập để lưu game yêu thích!"))
            }

            try {
                val currentLocal = getLocalWishlist().toMutableList()
                val exists = currentLocal.any { it.id == game.id }
                val newWishlistedState = !exists

                if (newWishlistedState) {
                    if (!exists) currentLocal.add(0, game)
                } else {
                    currentLocal.removeAll { it.id == game.id }
                }
                saveLocalWishlist(currentLocal)

                val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = wishlistApiService.toggleWishlist(game.id, authHeader)
                if (response.isSuccessful && response.body()?.isWishlisted != null) {
                    val apiState = response.body()!!.isWishlisted!!
                    return@withContext Result.success(apiState)
                }

                Result.success(newWishlistedState)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun isWishlisted(gameId: String): Boolean {
        return withContext(Dispatchers.IO) {
            getLocalWishlist().any { it.id == gameId }
        }
    }

    override fun clearLocalWishlist() {
        prefs.edit().remove("user_wishlist_games").apply()
    }

    private fun getLocalWishlist(): List<Game> {
        val json = prefs.getString("user_wishlist_games", null)
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Game>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun saveLocalWishlist(games: List<Game>) {
        val json = gson.toJson(games)
        prefs.edit().putString("user_wishlist_games", json).apply()
    }
}
