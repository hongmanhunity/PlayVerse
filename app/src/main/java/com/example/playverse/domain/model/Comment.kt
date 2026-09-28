package com.example.playverse.domain.model

data class Comment(
    val id: String = "",
    val gameId: String = "",          // Trích xuất từ Game.id
    val userId: String = "",          // Trích xuất từ User.id
    val userName: String = "",        // Trích xuất từ User.name
    val userAvatar: String? = null,   // Trích xuất từ User.avatarUrl
    val rating: Float = 5.0f,         // Số sao đánh giá (1.0 đến 5.0)
    val content: String = "",         // Nội dung bình luận
    val createdAt: String = ""        // Thời gian bình luận
)
