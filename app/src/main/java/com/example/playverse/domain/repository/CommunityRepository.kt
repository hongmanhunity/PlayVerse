package com.example.playverse.domain.repository

import com.example.playverse.domain.model.NotificationItem
import com.example.playverse.domain.model.Post

interface CommunityRepository {
    suspend fun getPosts(): Result<List<Post>>
    suspend fun createPost(token: String, title: String, content: String, gameTitle: String): Result<Boolean>
    suspend fun toggleLike(token: String, postId: String): Result<Boolean>
    suspend fun getNotifications(): Result<List<NotificationItem>>
}
