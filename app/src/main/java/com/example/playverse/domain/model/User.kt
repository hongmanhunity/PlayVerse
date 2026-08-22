package com.example.playverse.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val token: String = "",
    val avatarUrl: String? = null
)
