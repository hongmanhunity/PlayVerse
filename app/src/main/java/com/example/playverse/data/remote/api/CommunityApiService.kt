package com.example.playverse.data.remote.api

import com.example.playverse.data.remote.dto.CreatePostRequestDto
import com.example.playverse.data.remote.dto.NotificationsResponseDto
import com.example.playverse.data.remote.dto.PostsResponseDto
import retrofit2.Response
import retrofit2.http.*

interface CommunityApiService {
    @GET("api/posts")
    suspend fun getPosts(): Response<PostsResponseDto>

    @POST("api/posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequestDto
    ): Response<Map<String, Any>>

    @POST("api/posts/{id}/like")
    suspend fun toggleLikePost(
        @Header("Authorization") token: String,
        @Path("id") postId: String
    ): Response<Map<String, Any>>

    @GET("api/notifications")
    suspend fun getNotifications(): Response<NotificationsResponseDto>
}
