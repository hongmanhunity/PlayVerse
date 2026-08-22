package com.example.playverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("_id") val id: String,
    @SerializedName("authorName") val authorName: String? = "Gamer",
    @SerializedName("authorAvatar") val authorAvatar: String? = "",
    @SerializedName("gameTitle") val gameTitle: String? = "Chung",
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("likesCount") val likesCount: Int = 0,
    @SerializedName("commentsCount") val commentsCount: Int = 0,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class PostsResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("count") val count: Int = 0,
    @SerializedName("data") val data: List<PostDto> = emptyList()
)

data class CreatePostRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("gameTitle") val gameTitle: String = "Chung"
)

data class NotificationDto(
    @SerializedName("_id") val id: String,
    @SerializedName("titleText") val titleText: String,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: String = "system",
    @SerializedName("badge") val badge: String? = "HOT",
    @SerializedName("createdAt") val createdAt: String? = null
)

data class NotificationsResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("count") val count: Int = 0,
    @SerializedName("data") val data: List<NotificationDto> = emptyList()
)
