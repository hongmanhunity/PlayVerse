package com.example.playverse.data.remote.api

import com.example.playverse.data.remote.dto.WishlistResponseDto
import com.example.playverse.data.remote.dto.WishlistToggleResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface WishlistApiService {

    @GET("api/wishlist")
    suspend fun getWishlist(
        @Header("Authorization") token: String
    ): Response<WishlistResponseDto>

    @POST("api/wishlist/toggle/{gameId}")
    suspend fun toggleWishlist(
        @Path("gameId") gameId: String,
        @Header("Authorization") token: String
    ): Response<WishlistToggleResponseDto>
}
