package com.example.playverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameDto(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("tags") val tags: List<String>? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("size") val size: String? = null,
    @SerializedName("averageRating") val averageRating: Float? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("screenshots") val screenshots: List<String>? = null,
    @SerializedName("images") val images: List<String>? = null
)
