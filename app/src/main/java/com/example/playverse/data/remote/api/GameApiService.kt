package com.example.playverse.data.remote.api

import com.example.playverse.data.remote.dto.GameResponseDto
import com.example.playverse.data.remote.dto.SingleGameResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApiService {
    @GET("api/games")
    suspend fun getGames(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ) : Response<GameResponseDto>

    @GET("api/games/popular")
    suspend fun getPopularGames() : Response<GameResponseDto>

    @GET("api/games/flash")
    suspend fun getFlashGames() : Response<GameResponseDto>

    @GET("api/games/{id}")
    suspend fun getGameById(
        @Path("id") id: String
    ): Response<SingleGameResponseDto>
}

