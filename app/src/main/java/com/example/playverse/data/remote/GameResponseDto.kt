package com.example.playverse.data.remote

import com.google.gson.annotations.SerializedName

data class GameResponseDto(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("count") val count: Int? = null,
    @SerializedName("page") val page: Int? = null,
    @SerializedName("totalPages") val totalPages: Int? = null,
    @SerializedName("data") val data: List<GameDto>? = null
)
