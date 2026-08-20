package com.example.playverse.data.mapper

import com.example.playverse.data.remote.GameDto
import com.example.playverse.domain.model.Game
import kotlin.String

fun GameDto.toDomain() : Game {
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
        thumbnail = this.thumbnail ?: ""
    )
}