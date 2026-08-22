package com.example.playverse.data.repository

import com.example.playverse.data.local.UserPreferences
import com.example.playverse.data.remote.api.AuthApiService
import com.example.playverse.data.remote.dto.GoogleAuthRequestDto
import com.example.playverse.data.remote.dto.LoginRequestDto
import com.example.playverse.data.remote.dto.RegisterRequestDto
import com.example.playverse.domain.model.User
import com.example.playverse.domain.repository.AuthRepository
import org.json.JSONObject

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApiService.login(LoginRequestDto(email = email, password = password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val token = body.token ?: body.accessToken ?: ""
                val userData = body.user ?: body.data
                val user = User(
                    id = userData?.id ?: userData?.userId ?: "",
                    name = userData?.name ?: userData?.fullName ?: userData?.username ?: email.substringBefore("@"),
                    email = userData?.email ?: email,
                    token = token,
                    avatarUrl = userData?.avatar ?: userData?.avatarUrl
                )
                userPreferences.saveUser(user)
                Result.success(user)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMsg.ifEmpty { "Đăng nhập thất bại (Mã lỗi: ${response.code()})" }))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi kết nối server: ${e.localizedMessage ?: "Vui lòng thử lại"}"))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            val response = authApiService.register(
                RegisterRequestDto(
                    name = name,
                    fullName = name,
                    username = name,
                    email = email,
                    password = password
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val token = body.token ?: body.accessToken ?: ""
                val userData = body.user ?: body.data
                val user = User(
                    id = userData?.id ?: userData?.userId ?: "",
                    name = userData?.name ?: userData?.fullName ?: userData?.username ?: name.ifEmpty { email.substringBefore("@") },
                    email = userData?.email ?: email,
                    token = token,
                    avatarUrl = userData?.avatar ?: userData?.avatarUrl
                )
                userPreferences.saveUser(user)
                Result.success(user)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMsg.ifEmpty { "Đăng ký thất bại (Mã lỗi: ${response.code()})" }))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi kết nối server: ${e.localizedMessage ?: "Vui lòng thử lại"}"))
        }
    }

    override suspend fun loginWithGoogle(
        email: String,
        name: String?,
        avatar: String?,
        googleId: String?
    ): Result<User> {
        return try {
            val response = authApiService.googleAuth(
                GoogleAuthRequestDto(email = email, name = name, avatar = avatar, googleId = googleId)
            )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val token = body.token ?: body.accessToken ?: ""
                val userData = body.user ?: body.data
                val user = User(
                    id = userData?.id ?: userData?.userId ?: "",
                    name = userData?.name ?: userData?.fullName ?: userData?.username ?: email.substringBefore("@"),
                    email = userData?.email ?: email,
                    token = token,
                    avatarUrl = userData?.avatar ?: userData?.avatarUrl
                )
                userPreferences.saveUser(user)
                Result.success(user)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMsg.ifEmpty { "Đăng nhập Google thất bại (Mã lỗi: ${response.code()})" }))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Lỗi kết nối server: ${e.localizedMessage ?: "Vui lòng thử lại"}"))
        }
    }

    override fun getSavedUser(): User? {
        return userPreferences.getUser()
    }

    override fun isLoggedIn(): Boolean {
        return userPreferences.isLoggedIn()
    }

    override fun logout() {
        userPreferences.clearSession()
    }

    private fun parseErrorMessage(errorBodyJson: String?): String {
        if (errorBodyJson.isNullOrEmpty()) return ""
        return try {
            val json = JSONObject(errorBodyJson)
            json.optString("message", json.optString("error", ""))
        } catch (e: Exception) {
            ""
        }
    }
}
