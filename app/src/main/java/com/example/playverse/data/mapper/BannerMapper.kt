package com.example.playverse.data.mapper

import com.example.playverse.data.remote.dto.BannerDto
import com.example.playverse.domain.model.Banner

fun BannerDto.toDomain(): Banner {
    return Banner(
        id = id,
        title = title,
        image = image,
        gameId = gameId?.id
    )
}
