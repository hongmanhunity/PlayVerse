package com.example.playverse.data.repository

import com.example.playverse.data.api.RetrofitInstance
import com.example.playverse.data.mapper.toDomain
import com.example.playverse.data.remote.api.BannerApiService
import com.example.playverse.domain.model.Banner
import com.example.playverse.domain.repository.BannerRepository

/**
 * REPOSITORY IMPLEMENTATION: BannerRepositoryImpl
 * VỊ TRÍ: data/repository/BannerRepositoryImpl.kt
 */
class BannerRepositoryImpl(
    private val apiService: BannerApiService = RetrofitInstance.bannerApi
) : BannerRepository {

    override suspend fun getBanners(): Result<List<Banner>> {
        return try {
            val response = apiService.getBanners()
            if (response.success) {
                val banners = response.data.map { it.toDomain() }
                Result.success(banners)
            } else {
                Result.failure(Exception("Không thể tải danh sách banner"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBannerById(id: String): Result<Banner> {
        return try {
            val response = apiService.getBanners()
            val bannerDto = response.data.find { it.id == id }
            if (bannerDto != null) {
                Result.success(bannerDto.toDomain())
            } else {
                Result.failure(Exception("Không tìm thấy banner theo ID"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
