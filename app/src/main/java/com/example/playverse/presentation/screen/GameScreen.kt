package com.example.playverse.presentation.screen

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.playverse.data.api.RetrofitInstance
import com.example.playverse.data.local.UserPreferences
import com.example.playverse.data.repository.AuthRepositoryImpl
import com.example.playverse.presentation.state.AuthUiState
import com.example.playverse.presentation.viewmodel.AuthViewModel
import com.example.playverse.presentation.viewmodel.GameViewModel

enum class CurrentAppScreen {
    HOME,
    ALL_GAMES,
    PROFILE,
    LOGIN,
    REGISTER
}

/**
 * SCREEN CONTAINER: GameScreen (Router điều phối các màn hình trong app & Tích hợp Auth ViewModel)
 * VỊ TRÍ: presentation/screen/GameScreen.kt
 */
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel
) {
    val context = LocalContext.current
    val authViewModel = remember(context) {
        AuthViewModel(
            AuthRepositoryImpl(
                authApiService = RetrofitInstance.authApi,
                userPreferences = UserPreferences(context)
            )
        )
    }

    val authState by authViewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    var currentScreen by remember { mutableStateOf(CurrentAppScreen.HOME) }

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            currentScreen = CurrentAppScreen.PROFILE
            authViewModel.resetUiState()
        }
    }

    when (currentScreen) {
        CurrentAppScreen.HOME -> {
            HomeScreen(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToAllGames = { currentScreen = CurrentAppScreen.ALL_GAMES },
                onTabSelected = { tab ->
                    when (tab) {
                        "Home" -> currentScreen = CurrentAppScreen.HOME
                        "Profile" -> currentScreen = CurrentAppScreen.PROFILE
                        else -> { }
                    }
                }
            )
        }
        CurrentAppScreen.ALL_GAMES -> {
            AllGamesScreen(
                modifier = modifier,
                viewModel = viewModel,
                onBackClick = { currentScreen = CurrentAppScreen.HOME },
                onTabSelected = { tab ->
                    when (tab) {
                        "Home" -> currentScreen = CurrentAppScreen.HOME
                        "Profile" -> currentScreen = CurrentAppScreen.PROFILE
                        else -> { }
                    }
                }
            )
        }
        CurrentAppScreen.PROFILE -> {
            ProfileScreen(
                modifier = modifier,
                isLoggedIn = isLoggedIn,
                user = currentUser,
                onNavigateToLogin = {
                    authViewModel.resetUiState()
                    currentScreen = CurrentAppScreen.LOGIN
                },
                onNavigateToRegister = {
                    authViewModel.resetUiState()
                    currentScreen = CurrentAppScreen.REGISTER
                },
                onLogoutClick = {
                    authViewModel.logout()
                },
                onTabSelected = { tab ->
                    when (tab) {
                        "Home" -> currentScreen = CurrentAppScreen.HOME
                        "Profile" -> currentScreen = CurrentAppScreen.PROFILE
                        else -> { }
                    }
                },
                onCenterSearchClick = { currentScreen = CurrentAppScreen.ALL_GAMES }
            )
        }
        CurrentAppScreen.LOGIN -> {
            LoginScreen(
                modifier = modifier,
                uiState = authState,
                onBackClick = { currentScreen = CurrentAppScreen.PROFILE },
                onLoginSubmit = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigateToRegister = {
                    authViewModel.resetUiState()
                    currentScreen = CurrentAppScreen.REGISTER
                }
            )
        }
        CurrentAppScreen.REGISTER -> {
            RegisterScreen(
                modifier = modifier,
                uiState = authState,
                onBackClick = { currentScreen = CurrentAppScreen.PROFILE },
                onRegisterSubmit = { name, email, password ->
                    authViewModel.register(name, email, password)
                },
                onNavigateToLogin = {
                    authViewModel.resetUiState()
                    currentScreen = CurrentAppScreen.LOGIN
                }
            )
        }
    }
}
