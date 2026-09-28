package com.example.playverse.domain.model

data class Banner(
    val id: String,
    val title: String,
    val image: String,
    val gameId: String? = null
)
