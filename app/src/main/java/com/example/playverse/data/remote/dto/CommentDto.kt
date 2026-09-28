package com.example.playverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("game") val gameId: String? = null,
    @SerializedName("user") val user: UserDto? = null,
    @SerializedName("rating") val rating: Float? = 5.0f,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class CreateCommentRequestDto(
    @SerializedName("rating") val rating: Float,
    @SerializedName("comment") val comment: String
)

data class CommentListResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<CommentDto>? = null
)

data class SingleCommentResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: CommentDto? = null
)
