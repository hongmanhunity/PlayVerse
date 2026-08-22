package com.example.playverse.domain.model

/**
 * DOMAIN MODEL: Banner
 * VỊ TRÍ: domain/model/Banner.kt
 */
data class Banner(
    val id: String,
    val title: String,
    val image: String,
    val gameId: String? = null
)
