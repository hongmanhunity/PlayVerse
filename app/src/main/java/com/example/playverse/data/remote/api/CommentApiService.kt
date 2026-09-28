package com.example.playverse.data.remote.api

import com.example.playverse.data.remote.dto.CommentListResponseDto
import com.example.playverse.data.remote.dto.CreateCommentRequestDto
import com.example.playverse.data.remote.dto.SingleCommentResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApiService {

    @GET("api/reviews/game/{gameId}")
    suspend fun getReviewsByGame(
        @Path("gameId") gameId: String
    ): Response<CommentListResponseDto>

    @POST("api/reviews/game/{gameId}")
    suspend fun addReview(
        @Path("gameId") gameId: String,
        @Header("Authorization") token: String,
        @Body body: CreateCommentRequestDto
    ): Response<SingleCommentResponseDto>
}
