package com.example.playverse.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * REMOTE DTO: BannerDto & BannerResponseDto
 * VỊ TRÍ: data/remote/dto/BannerDto.kt
 */
data class BannerResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("count") val count: Int,
    @SerializedName("data") val data: List<BannerDto>
)

data class BannerDto(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("gameId") val gameId: BannerGameIdDto? = null
)

data class BannerGameIdDto(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String? = null
)
