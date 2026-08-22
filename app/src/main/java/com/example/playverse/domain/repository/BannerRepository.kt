package com.example.playverse.domain.repository

import com.example.playverse.domain.model.Banner

/**
 * DOMAIN REPOSITORY INTERFACE: BannerRepository
 * VỊ TRÍ: domain/repository/BannerRepository.kt
 */
interface BannerRepository {
    suspend fun getBanners(): Result<List<Banner>>
    suspend fun getBannerById(id: String): Result<Banner>
}
