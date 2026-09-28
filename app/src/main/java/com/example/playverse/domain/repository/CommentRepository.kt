package com.example.playverse.domain.repository

import com.example.playverse.domain.model.Comment
import com.example.playverse.domain.model.User

interface CommentRepository {
    suspend fun getCommentsForGame(gameId: String): Result<List<Comment>>
    suspend fun addComment(gameId: String, content: String, rating: Float, user: User): Result<Comment>
}
