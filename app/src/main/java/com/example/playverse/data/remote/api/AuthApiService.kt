package com.example.playverse.data.remote.api

import com.example.playverse.data.remote.dto.AuthResponseDto
import com.example.playverse.data.remote.dto.LoginRequestDto
import com.example.playverse.data.remote.dto.RegisterRequestDto
import com.example.playverse.data.remote.dto.GoogleAuthRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<AuthResponseDto>

    @POST("api/auth/google")
    suspend fun googleAuth(@Body request: GoogleAuthRequestDto): Response<AuthResponseDto>
}
