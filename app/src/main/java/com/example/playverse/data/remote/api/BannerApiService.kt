package com.example.playverse.data.remote.api

import com.example.playverse.data.remote.dto.BannerResponseDto
import retrofit2.http.GET

interface BannerApiService {
    @GET("api/banners")
    suspend fun getBanners(): BannerResponseDto
}
