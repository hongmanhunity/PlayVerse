package com.example.playverse.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.playverse.presentation.component.common.BottomNavBar
import com.example.playverse.presentation.component.home.HomeBanner
import com.example.playverse.presentation.component.home.HomeGameGridSection
import com.example.playverse.presentation.component.home.HomeHeader
import com.example.playverse.presentation.state.GameUiState
import com.example.playverse.presentation.viewmodel.GameViewModel

/**
 * SCREEN: HomeScreen (Trang chủ)
 * VỊ TRÍ: presentation/screen/HomeScreen.kt
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    onNavigateToAllGames: () -> Unit = {},
    onTabSelected: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundColor = Color(0xFFF4F7FC)
    val secondaryTextColor = Color(0xFF8A94A6)
    val actionColor = Color(0xFF0066FF)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeHeader(
                title = "Game",
                onNotificationClick = { },
                onSearchClick = onNavigateToAllGames
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is GameUiState.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 100.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            HomeBanner()

                            HomeGameGridSection(
                                title = "Popular Game",
                                games = state.popularGames.ifEmpty { state.games.take(8) },
                                onSeeAllClick = onNavigateToAllGames
                            )

                            HomeGameGridSection(
                                title = "Flash Game",
                                games = state.flashGames.ifEmpty { state.games.takeLast(4) },
                                onSeeAllClick = onNavigateToAllGames
                            )
                        }
                    }
                    is GameUiState.Loading -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = actionColor)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Đang tải trang Home...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = secondaryTextColor
                            )
                        }
                    }
                    is GameUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }

        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedTab = "Home",
            onTabSelected = onTabSelected,
            onCenterSearchClick = onNavigateToAllGames
        )
    }
}
