package com.example.playverse.data.mapper

import com.example.playverse.data.remote.dto.BannerDto
import com.example.playverse.domain.model.Banner

/**
 * MAPPER: BannerMapper
 * VỊ TRÍ: data/mapper/BannerMapper.kt
 */
fun BannerDto.toDomain(): Banner {
    return Banner(
        id = id,
        title = title,
        image = image,
        gameId = gameId?.id
    )
}
