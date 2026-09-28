package com.example.playverse.data.api

import com.example.playverse.data.remote.api.AuthApiService
import com.example.playverse.data.remote.api.BannerApiService
import com.example.playverse.data.remote.api.CommunityApiService
import com.example.playverse.data.remote.api.GameApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.playverse.data.remote.api.CommentApiService
import com.example.playverse.data.remote.api.WishlistApiService

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.100.8:5000/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GameApiService by lazy {
        retrofit.create(GameApiService::class.java)
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val communityApi: CommunityApiService by lazy {
        retrofit.create(CommunityApiService::class.java)
    }

    val bannerApi: BannerApiService by lazy {
        retrofit.create(BannerApiService::class.java)
    }

    val commentApi: CommentApiService by lazy {
        retrofit.create(CommentApiService::class.java)
    }

    val wishlistApi: WishlistApiService by lazy {
        retrofit.create(WishlistApiService::class.java)
    }
}