package com.example.playverse.data.repository

import com.example.playverse.data.remote.api.CommunityApiService
import com.example.playverse.data.remote.dto.CreatePostRequestDto
import com.example.playverse.domain.model.NotificationItem
import com.example.playverse.domain.model.Post
import com.example.playverse.domain.repository.CommunityRepository

class CommunityRepositoryImpl(
    private val api: CommunityApiService
) : CommunityRepository {

    override suspend fun getPosts(): Result<List<Post>> {
        return try {
            val response = api.getPosts()
            if (response.isSuccessful && response.body() != null) {
                val postDtos = response.body()!!.data
                val posts = postDtos.map { dto ->
                    Post(
                        id = dto.id,
                        authorName = dto.authorName ?: "Gamer",
                        authorAvatar = dto.authorAvatar ?: "",
                        gameTitle = dto.gameTitle ?: "Chung",
                        title = dto.title,
                        content = dto.content,
                        likesCount = dto.likesCount,
                        commentsCount = dto.commentsCount,
                        timeAgo = "Mới đây"
                    )
                }
                Result.success(posts)
            } else {
                Result.failure(Exception("Không thể tải danh sách bài viết"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPost(
        token: String,
        title: String,
        content: String,
        gameTitle: String
    ): Result<Boolean> {
        return try {
            val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.createPost(
                token = authHeader,
                request = CreatePostRequestDto(title = title, content = content, gameTitle = gameTitle)
            )
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Đăng bài viết thất bại"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleLike(token: String, postId: String): Result<Boolean> {
        return try {
            val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.toggleLikePost(authHeader, postId)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Thao tác thất bại"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotifications(): Result<List<NotificationItem>> {
        return try {
            val response = api.getNotifications()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!.data
                val notifications = dtos.map { dto ->
                    NotificationItem(
                        id = dto.id,
                        title = dto.titleText,
                        message = dto.message,
                        type = dto.type,
                        badge = dto.badge ?: "HOT",
                        timeAgo = "Mới"
                    )
                }
                Result.success(notifications)
            } else {
                Result.failure(Exception("Không thể tải thông báo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
