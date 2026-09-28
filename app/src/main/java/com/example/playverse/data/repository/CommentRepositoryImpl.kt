package com.example.playverse.data.repository

import android.content.Context
import com.example.playverse.data.remote.api.CommentApiService
import com.example.playverse.data.remote.dto.CreateCommentRequestDto
import com.example.playverse.domain.model.Comment
import com.example.playverse.domain.model.User
import com.example.playverse.domain.repository.CommentRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CommentRepositoryImpl(
    private val commentApiService: CommentApiService,
    private val context: Context
) : CommentRepository {

    private val prefs = context.getSharedPreferences("playverse_game_comments", Context.MODE_PRIVATE)
    private val gson = Gson()

    override suspend fun getCommentsForGame(gameId: String): Result<List<Comment>> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Thử gọi API Backend Render trước
                val response = commentApiService.getReviewsByGame(gameId)
                if (response.isSuccessful && response.body()?.data != null) {
                    val dtoList = response.body()!!.data!!
                    val domainComments = dtoList.map { dto ->
                        Comment(
                            id = dto.id ?: "",
                            gameId = dto.gameId ?: gameId,
                            userId = dto.user?.id ?: dto.user?.userId ?: "",
                            userName = dto.user?.name ?: dto.user?.fullName ?: dto.user?.username ?: "Game Player",
                            userAvatar = dto.user?.avatar ?: dto.user?.avatarUrl,
                            rating = dto.rating ?: 5.0f,
                            content = dto.comment ?: dto.content ?: "",
                            createdAt = formatDate(dto.createdAt)
                        )
                    }
                    // Lưu lại cache local
                    saveCommentsToPrefs(gameId, domainComments)
                    return@withContext Result.success(domainComments)
                }

                // 2. Nếu API chưa có dữ liệu hoặc lỗi mạng -> Lấy từ Local Cache
                val localComments = getLocalComments(gameId)
                Result.success(localComments)
            } catch (e: Exception) {
                // Lấy từ Local Cache khi offline
                val localComments = getLocalComments(gameId)
                Result.success(localComments)
            }
        }
    }

    override suspend fun addComment(
        gameId: String,
        content: String,
        rating: Float,
        user: User
    ): Result<Comment> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Nếu User có token -> Gửi API lên Backend Render
                if (user.token.isNotBlank()) {
                    val authHeader = if (user.token.startsWith("Bearer ")) user.token else "Bearer ${user.token}"
                    val response = commentApiService.addReview(
                        gameId = gameId,
                        token = authHeader,
                        body = CreateCommentRequestDto(rating = rating, comment = content)
                    )

                    if (response.isSuccessful && response.body()?.data != null) {
                        val dto = response.body()!!.data!!
                        val newComment = Comment(
                            id = dto.id ?: UUID.randomUUID().toString(),
                            gameId = gameId,
                            userId = dto.user?.id ?: user.id,
                            userName = dto.user?.name ?: dto.user?.username ?: user.name,
                            userAvatar = dto.user?.avatar ?: user.avatarUrl,
                            rating = dto.rating ?: rating,
                            content = dto.comment ?: content,
                            createdAt = formatDate(dto.createdAt)
                        )
                        // Cập nhật lại danh sách local
                        val existing = getLocalComments(gameId).toMutableList()
                        existing.add(0, newComment)
                        saveCommentsToPrefs(gameId, existing)

                        return@withContext Result.success(newComment)
                    }
                }

                // 2. Nếu chưa có token hoặc API trả về lỗi -> Lưu vào Local Store
                val now = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val localComment = Comment(
                    id = UUID.randomUUID().toString(),
                    gameId = gameId,
                    userId = user.id.ifEmpty { "user_${System.currentTimeMillis()}" },
                    userName = user.name.ifEmpty { "Game Player" },
                    userAvatar = user.avatarUrl,
                    rating = rating,
                    content = content.trim(),
                    createdAt = now
                )

                val existing = getLocalComments(gameId).toMutableList()
                existing.add(0, localComment)
                saveCommentsToPrefs(gameId, existing)

                Result.success(localComment)
            } catch (e: Exception) {
                // Fallback lưu local khi có sự cố mạng
                val now = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val localComment = Comment(
                    id = UUID.randomUUID().toString(),
                    gameId = gameId,
                    userId = user.id,
                    userName = user.name,
                    userAvatar = user.avatarUrl,
                    rating = rating,
                    content = content,
                    createdAt = now
                )
                val existing = getLocalComments(gameId).toMutableList()
                existing.add(0, localComment)
                saveCommentsToPrefs(gameId, existing)

                Result.success(localComment)
            }
        }
    }

    private fun getLocalComments(gameId: String): List<Comment> {
        val json = prefs.getString("comments_$gameId", null)
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Comment>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun saveCommentsToPrefs(gameId: String, comments: List<Comment>) {
        val json = gson.toJson(comments)
        prefs.edit().putString("comments_$gameId", json).apply()
    }

    private fun formatDate(dateStr: String?): String {
        if (dateStr.isNullOrBlank()) {
            return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        }
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = isoFormat.parse(dateStr)
            if (date != null) {
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
            } else dateStr
        } catch (e: Exception) {
            dateStr.take(10)
        }
    }
}
