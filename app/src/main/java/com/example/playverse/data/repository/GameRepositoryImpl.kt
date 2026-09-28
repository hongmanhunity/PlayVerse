package com.example.playverse.data.repository

import com.example.playverse.data.mapper.toDomain
import com.example.playverse.data.remote.api.GameApiService
import com.example.playverse.data.remote.dto.GameResponseDto
import com.example.playverse.domain.model.Game
import com.example.playverse.domain.repository.GameRepository

data class GameRepositoryImpl(
    val apiService: GameApiService
): GameRepository {
    override suspend fun getGames(): List<Game> {
        val response = apiService.getGames()
        if(response.isSuccessful && response.body() != null) {
            val body: GameResponseDto? = response.body()
            val gameList = body?.data ?: emptyList()
            return gameList.map { it.toDomain() }
        } else {
            throw Exception("API Error: ${response.code()}")
        }
    }

    override suspend fun getPopularGames(): List<Game> {
        val response = apiService.getPopularGames()
        if(response.isSuccessful && response.body() != null) {
            val body: GameResponseDto? = response.body()
            return body?.data?.map { it.toDomain() } ?: emptyList()
        }
        return emptyList()
    }

    override suspend fun getFlashGames(): List<Game> {
        val response = apiService.getFlashGames()
        if(response.isSuccessful && response.body() != null) {
            val body: GameResponseDto? = response.body()
            return body?.data?.map { it.toDomain() } ?: emptyList()
        }
        return emptyList()
    }

    override suspend fun getGameById(id: String): Game? {
        val response = apiService.getGameById(id)
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()
            return body?.data?.toDomain()
        }
        return null
    }
}