package com.example.playverse.domain.model

data class Game(
    val id: String,
    val title: String,
    val slug: String,
    val publisher: String,
    val category: String,
    val tags: List<String>,
    val description: String,
    val size: String,
    val averageRating: Float,
    val thumbnail: String,
    val screenshots: List<String> = emptyList(),
    val images: List<String> = screenshots
)