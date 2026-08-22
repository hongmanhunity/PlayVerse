package com.example.playverse.presentation.screen

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.playverse.data.api.RetrofitInstance
import com.example.playverse.data.local.UserPreferences
import com.example.playverse.data.repository.AuthRepositoryImpl
import com.example.playverse.data.repository.CommunityRepositoryImpl
import com.example.playverse.domain.model.Game
import com.example.playverse.presentation.state.AuthUiState
import com.example.playverse.presentation.viewmodel.AuthViewModel
import com.example.playverse.presentation.viewmodel.CommunityViewModel
import com.example.playverse.presentation.viewmodel.GameViewModel

enum class CurrentAppScreen {
    HOME,
    COMMUNITY,
    ALERTS,
    ALL_GAMES,
    GAME_DETAIL,
    PROFILE,
    LOGIN,
    REGISTER
}

/**
 * SCREEN CONTAINER: GameScreen (Router điều phối tất cả màn hình ứng dụng)
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

    val communityViewModel = remember {
        CommunityViewModel(
            CommunityRepositoryImpl(RetrofitInstance.communityApi)
        )
    }

    val authState by authViewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    var currentScreen by remember { mutableStateOf(CurrentAppScreen.HOME) }
    var previousScreen by remember { mutableStateOf(CurrentAppScreen.HOME) }
    var selectedGame by remember { mutableStateOf<Game?>(null) }

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            currentScreen = CurrentAppScreen.PROFILE
            authViewModel.resetUiState()
        }
    }

    val onTabNavigate: (String) -> Unit = { tab ->
        when (tab) {
            "Home" -> currentScreen = CurrentAppScreen.HOME
            "Community" -> currentScreen = CurrentAppScreen.COMMUNITY
            "Alerts" -> currentScreen = CurrentAppScreen.ALERTS
            "Profile" -> currentScreen = CurrentAppScreen.PROFILE
            else -> { }
        }
    }

    when (currentScreen) {
        CurrentAppScreen.HOME -> {
            HomeScreen(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToAllGames = { currentScreen = CurrentAppScreen.ALL_GAMES },
                onTabSelected = onTabNavigate,
                onGameClick = { game ->
                    selectedGame = game
                    previousScreen = CurrentAppScreen.HOME
                    currentScreen = CurrentAppScreen.GAME_DETAIL
                }
            )
        }
        CurrentAppScreen.COMMUNITY -> {
            CommunityScreen(
                modifier = modifier,
                viewModel = communityViewModel,
                onTabSelected = onTabNavigate,
                onCenterSearchClick = { currentScreen = CurrentAppScreen.ALL_GAMES }
            )
        }
        CurrentAppScreen.ALERTS -> {
            AlertsScreen(
                modifier = modifier,
                viewModel = communityViewModel,
                onTabSelected = onTabNavigate,
                onCenterSearchClick = { currentScreen = CurrentAppScreen.ALL_GAMES }
            )
        }
        CurrentAppScreen.ALL_GAMES -> {
            AllGamesScreen(
                modifier = modifier,
                viewModel = viewModel,
                onBackClick = { currentScreen = CurrentAppScreen.HOME },
                onTabSelected = onTabNavigate,
                onGameClick = { game ->
                    selectedGame = game
                    previousScreen = CurrentAppScreen.ALL_GAMES
                    currentScreen = CurrentAppScreen.GAME_DETAIL
                }
            )
        }
        CurrentAppScreen.GAME_DETAIL -> {
            selectedGame?.let { game ->
                GameDetailScreen(
                    game = game,
                    modifier = modifier,
                    onBackClick = { currentScreen = previousScreen }
                )
            } ?: run {
                currentScreen = CurrentAppScreen.HOME
            }
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
                onTabSelected = onTabNavigate,
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
                onGoogleLoginSubmit = { gEmail, gName, gAvatar ->
                    authViewModel.loginWithGoogle(gEmail, gName, gAvatar)
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
                onGoogleLoginSubmit = { gEmail, gName, gAvatar ->
                    authViewModel.loginWithGoogle(gEmail, gName, gAvatar)
                },
                onNavigateToLogin = {
                    authViewModel.resetUiState()
                    currentScreen = CurrentAppScreen.LOGIN
                }
            )
        }
    }
}
