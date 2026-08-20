package com.example.playverse.domain.repository

import com.example.playverse.domain.model.Game

interface GameRepository {
    suspend fun getGames(): List<Game>
    suspend fun getPopularGames(): List<Game>
    suspend fun getFlashGames(): List<Game>
}