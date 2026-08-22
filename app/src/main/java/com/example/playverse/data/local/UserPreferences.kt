package com.example.playverse.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.playverse.domain.model.User

class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("playverse_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_AVATAR_URL = "avatar_url"
    }

    fun saveUser(user: User) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_TOKEN, user.token)
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            if (!user.avatarUrl.isNullOrEmpty()) {
                putString(KEY_AVATAR_URL, user.avatarUrl)
            }
            apply()
        }
    }

    fun saveAvatarUri(uriString: String) {
        prefs.edit().putString(KEY_AVATAR_URL, uriString).apply()
    }

    fun getUser(): User? {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn) return null

        return User(
            id = prefs.getString(KEY_USER_ID, "") ?: "",
            name = prefs.getString(KEY_USER_NAME, "Game thủ") ?: "Game thủ",
            email = prefs.getString(KEY_USER_EMAIL, "") ?: "",
            token = prefs.getString(KEY_TOKEN, "") ?: "",
            avatarUrl = prefs.getString(KEY_AVATAR_URL, null)
        )
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
