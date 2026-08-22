package com.example.playverse.domain.model

data class Post(
    val id: String = "",
    val authorName: String = "Gamer",
    val authorAvatar: String = "",
    val gameTitle: String = "Chung",
    val title: String = "",
    val content: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val timeAgo: String = "Vừa xong"
)

data class NotificationItem(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "system",
    val badge: String = "HOT",
    val timeAgo: String = "Gần đây"
)
