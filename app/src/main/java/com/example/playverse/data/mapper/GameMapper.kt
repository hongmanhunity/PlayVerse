package com.example.playverse.data.mapper

import com.example.playverse.data.remote.dto.GameDto
import com.example.playverse.domain.model.Game

fun GameDto.toDomain(): Game {
    val screenshotList = this.screenshots?.takeIf { it.isNotEmpty() }
        ?: this.images?.takeIf { it.isNotEmpty() }
        ?: emptyList()

    return Game(
        id = this.id ?: "",
        title = this.title ?: "Không có tên",
        slug = this.slug ?: "",
        publisher = this.publisher ?: "Epic Games",
        category = this.category ?: "Action",
        tags = this.tags ?: emptyList(),
        description = this.description ?: "Chưa có mô tả.",
        size = this.size ?: "N/A",
        averageRating = this.averageRating ?: 0f,
        thumbnail = this.thumbnail ?: "",
        screenshots = screenshotList
    )
}