package com.example.playverse.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Community : Screen("community")
    object Alerts : Screen("alerts")
    object Wishlist : Screen("wishlist")
    object AllGames : Screen("all_games")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")

    object GameDetail : Screen("game_detail/{gameId}") {
        fun createRoute(gameId: String): String = "game_detail/$gameId"
    }
}
