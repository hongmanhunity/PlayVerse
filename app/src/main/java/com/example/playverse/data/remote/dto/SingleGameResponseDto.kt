package com.example.playverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SingleGameResponseDto(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("data") val data: GameDto? = null
)
