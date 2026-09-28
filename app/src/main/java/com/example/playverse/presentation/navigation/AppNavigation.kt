package com.example.playverse.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.playverse.data.api.RetrofitInstance
import com.example.playverse.data.local.UserPreferences
import com.example.playverse.data.repository.AuthRepositoryImpl
import com.example.playverse.data.repository.CommentRepositoryImpl
import com.example.playverse.data.repository.CommunityRepositoryImpl
import com.example.playverse.data.repository.WishlistRepositoryImpl
import com.example.playverse.domain.model.Game
import com.example.playverse.presentation.component.common.BottomNavBar
import com.example.playverse.presentation.screen.*
import com.example.playverse.presentation.state.AuthUiState
import com.example.playverse.presentation.state.GameDetailUiState
import com.example.playverse.presentation.state.GameUiState
import com.example.playverse.presentation.viewmodel.AuthViewModel
import com.example.playverse.presentation.viewmodel.CommentViewModel
import com.example.playverse.presentation.viewmodel.CommunityViewModel
import com.example.playverse.presentation.viewmodel.GameViewModel
import com.example.playverse.presentation.viewmodel.WishlistViewModel
import com.example.playverse.ui.theme.PlayVerseBackground
import com.example.playverse.ui.theme.PlayVerseBrandBlue

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel,
    navController: NavHostController = rememberNavController()
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

    val wishlistViewModel = remember(context) {
        WishlistViewModel(
            WishlistRepositoryImpl(
                wishlistApiService = RetrofitInstance.wishlistApi,
                context = context
            )
        )
    }

    val commentViewModel = remember(context) {
        CommentViewModel(
            CommentRepositoryImpl(
                commentApiService = RetrofitInstance.commentApi,
                context = context
            )
        )
    }

    val wishlistGames by wishlistViewModel.wishlistGames.collectAsState()
    val gameComments by commentViewModel.comments.collectAsState()

    val authState by authViewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    // Đồng bộ danh sách Wishlist khi user thay đổi
    LaunchedEffect(currentUser) {
        val token = currentUser?.token ?: ""
        wishlistViewModel.loadWishlist(token)
    }

    // Xử lý khi đăng nhập / đăng ký thành công
    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            authViewModel.resetUiState()
            navController.popBackStack()
        }
    }

    val handleToggleWishlist: (Game) -> Unit = { game ->
        val token = currentUser?.token ?: ""
        wishlistViewModel.toggleWishlist(
            game = game,
            token = token,
            onUnauthenticated = {
                android.widget.Toast.makeText(context, "Vui lòng đăng nhập để lưu game yêu thích!", android.widget.Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Login.route)
            }
        )
    }

    // Lấy route hiện tại để đồng bộ trạng thái thanh BottomNavBar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Chỉ hiển thị BottomNavBar ở các màn hình chính
    val bottomBarRoutes = setOf(
        Screen.Home.route,
        Screen.Community.route,
        Screen.Alerts.route,
        Screen.Profile.route,
        Screen.Wishlist.route,
        Screen.AllGames.route
    )
    val showBottomBar = currentRoute in bottomBarRoutes

    val selectedTab = when (currentRoute) {
        Screen.Home.route -> "Home"
        Screen.Community.route -> "Community"
        Screen.Wishlist.route -> "Wishlist"
        Screen.Profile.route -> "Profile"
        else -> ""
    }

    // Hàm chuyển tab phản hồi ngay lập tức và chính xác
    val navigateToTab: (String) -> Unit = { tab ->
        val targetRoute = when (tab) {
            "Home" -> Screen.Home.route
            "Community" -> Screen.Community.route
            "Wishlist" -> Screen.Wishlist.route
            "Profile" -> Screen.Profile.route
            else -> Screen.Home.route
        }

        if (currentRoute != targetRoute) {
            navController.navigate(targetRoute) {
                popUpTo(Screen.Home.route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val onCenterSearchClick: () -> Unit = {
        if (currentRoute != Screen.AllGames.route) {
            navController.navigate(Screen.AllGames.route) {
                popUpTo(Screen.Home.route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlayVerseBackground)
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. MÀN HÌNH CHÍNH (HOME)
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = gameViewModel,
                    onNavigateToAllGames = onCenterSearchClick,
                    onFavoriteClick = {
                        navController.navigate(Screen.Wishlist.route)
                    },
                    onTabSelected = navigateToTab,
                    onGameClick = { game ->
                        gameViewModel.getGameById(game.id)
                        navController.navigate(Screen.GameDetail.createRoute(game.id))
                    },
                    onBannerClick = { banner ->
                        val targetGameId = banner.gameId
                        if (!targetGameId.isNullOrEmpty()) {
                            gameViewModel.getGameById(targetGameId)
                            navController.navigate(Screen.GameDetail.createRoute(targetGameId))
                        } else {
                            val currentState = gameViewModel.uiState.value
                            if (currentState is GameUiState.Success) {
                                val matchedGame = currentState.games.firstOrNull {
                                    it.title.contains(banner.title, ignoreCase = true) ||
                                            banner.title.contains(it.title, ignoreCase = true)
                                } ?: currentState.games.firstOrNull()

                                if (matchedGame != null) {
                                    gameViewModel.getGameById(matchedGame.id)
                                    navController.navigate(Screen.GameDetail.createRoute(matchedGame.id))
                                }
                            }
                        }
                    }
                )
            }

            // 2. MÀN HÌNH DIỄN ĐÀN (COMMUNITY)
            composable(Screen.Community.route) {
                CommunityScreen(
                    viewModel = communityViewModel,
                    onTabSelected = navigateToTab,
                    onCenterSearchClick = onCenterSearchClick
                )
            }

            // 3. MÀN HÌNH THÔNG BÁO (ALERTS)
            composable(Screen.Alerts.route) {
                AlertsScreen(
                    viewModel = communityViewModel,
                    onTabSelected = navigateToTab,
                    onCenterSearchClick = onCenterSearchClick
                )
            }

            // 4. MÀN HÌNH DANH SÁCH YÊU THÍCH (WISHLIST)
            composable(Screen.Wishlist.route) {
                WishlistScreen(
                    isLoggedIn = isLoggedIn,
                    wishlistGames = wishlistGames,
                    onGameClick = { game ->
                        gameViewModel.getGameById(game.id)
                        navController.navigate(Screen.GameDetail.createRoute(game.id))
                    },
                    onRemoveFromWishlist = { game ->
                        handleToggleWishlist(game)
                    },
                    onBackClick = null,
                    onNavigateToLogin = {
                        authViewModel.resetUiState()
                        navController.navigate(Screen.Login.route)
                    },
                    onTabSelected = navigateToTab,
                    onCenterSearchClick = onCenterSearchClick
                )
            }

            // 5. MÀN HÌNH TẤT CẢ GAME (ALL GAMES)
            composable(Screen.AllGames.route) {
                AllGamesScreen(
                    viewModel = gameViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onTabSelected = navigateToTab,
                    onGameClick = { game ->
                        gameViewModel.getGameById(game.id)
                        navController.navigate(Screen.GameDetail.createRoute(game.id))
                    }
                )
            }

            // 6. MÀN HÌNH CHI TIẾT GAME (GAME DETAIL)
            composable(
                route = Screen.GameDetail.route,
                arguments = listOf(navArgument("gameId") { type = NavType.StringType })
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId") ?: ""

                LaunchedEffect(gameId) {
                    if (gameId.isNotEmpty()) {
                        gameViewModel.getGameById(gameId)
                        commentViewModel.loadComments(gameId)
                    }
                }

                val detailState by gameViewModel.detailUiState.collectAsState()
                val gamesListState by gameViewModel.uiState.collectAsState()

                // Hiển thị ngay game từ danh sách tổng nếu có để tránh giật màn hình
                val cachedGame = remember(gameId, gamesListState) {
                    (gamesListState as? GameUiState.Success)?.games?.firstOrNull { it.id == gameId }
                }

                val activeGame = when (val state = detailState) {
                    is GameDetailUiState.Success -> state.game
                    else -> cachedGame
                }

                val handlePostComment: (String, Float) -> Unit = { content, rating ->
                    currentUser?.let { u ->
                        if (activeGame != null) {
                            commentViewModel.postComment(activeGame.id, content, rating, u)
                        }
                    }
                }

                if (activeGame != null) {
                    GameDetailScreen(
                        game = activeGame,
                        comments = gameComments,
                        currentUser = currentUser,
                        isFavorite = wishlistGames.any { it.id == activeGame.id },
                        onFavoriteClick = { handleToggleWishlist(activeGame) },
                        onPostComment = handlePostComment,
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route)
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } else if (detailState is GameDetailUiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PlayVerseBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PlayVerseBrandBlue)
                    }
                } else if (detailState is GameDetailUiState.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PlayVerseBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (detailState as GameDetailUiState.Error).message,
                            color = Color.Red
                        )
                    }
                }
            }

            // 7. MÀN HÌNH HỒ SƠ CÁ NHÂN (PROFILE)
            composable(Screen.Profile.route) {
                ProfileScreen(
                    isLoggedIn = isLoggedIn,
                    user = currentUser,
                    onNavigateToLogin = {
                        authViewModel.resetUiState()
                        navController.navigate(Screen.Login.route)
                    },
                    onNavigateToRegister = {
                        authViewModel.resetUiState()
                        navController.navigate(Screen.Register.route)
                    },
                    onLogoutClick = {
                        authViewModel.logout()
                        wishlistViewModel.clearWishlist()
                        android.widget.Toast.makeText(context, "Đã đăng xuất thành công!", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    onTabSelected = navigateToTab,
                    onCenterSearchClick = onCenterSearchClick
                )
            }

            // 8. MÀN HÌNH ĐĂNG NHẬP (LOGIN)
            composable(Screen.Login.route) {
                LoginScreen(
                    uiState = authState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onLoginSubmit = { email, password ->
                        authViewModel.login(email, password)
                    },
                    onGoogleLoginSubmit = { gEmail, gName, gAvatar, idToken, googleId ->
                        authViewModel.loginWithGoogle(
                            email = gEmail,
                            name = gName,
                            avatar = gAvatar,
                            idToken = idToken,
                            googleId = googleId
                        )
                    },
                    onNavigateToRegister = {
                        authViewModel.resetUiState()
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            // 9. MÀN HÌNH ĐĂNG KÝ (REGISTER)
            composable(Screen.Register.route) {
                RegisterScreen(
                    uiState = authState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRegisterSubmit = { name, email, password ->
                        authViewModel.register(name, email, password)
                    },
                    onGoogleLoginSubmit = { gEmail, gName, gAvatar, idToken, googleId ->
                        authViewModel.loginWithGoogle(
                            email = gEmail,
                            name = gName,
                            avatar = gAvatar,
                            idToken = idToken,
                            googleId = googleId
                        )
                    },
                    onNavigateToLogin = {
                        authViewModel.resetUiState()
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
        }

        // DUY NHẤT 1 THANH BOTTOM NAVIGATION BAR NỔI TẠI GỐC CỦA ỨNG DỤNG
        if (showBottomBar) {
            BottomNavBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                selectedTab = selectedTab,
                onTabSelected = navigateToTab,
                onCenterSearchClick = onCenterSearchClick
            )
        }
    }
}
