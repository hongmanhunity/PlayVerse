package com.example.playverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WishlistResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("count") val count: Int? = 0,
    @SerializedName("data") val data: List<GameDto>? = null
)

data class WishlistToggleResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("isWishlisted") val isWishlisted: Boolean? = false,
    @SerializedName("message") val message: String? = null
)
