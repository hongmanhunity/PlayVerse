package com.example.playverse.domain.repository

import com.example.playverse.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun loginWithGoogle(
        email: String,
        name: String? = null,
        avatar: String? = null,
        googleId: String? = null,
        idToken: String? = null
    ): Result<User>
    fun getSavedUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
}
