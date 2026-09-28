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
import androidx.compose.ui.unit.dp
import com.example.playverse.domain.model.Banner
import com.example.playverse.domain.model.Game
import com.example.playverse.presentation.component.common.PlayVerseHeader
import com.example.playverse.presentation.component.home.HomeBanner
import com.example.playverse.presentation.component.home.HomeGameGridSection
import com.example.playverse.presentation.state.GameUiState
import com.example.playverse.presentation.viewmodel.GameViewModel
import com.example.playverse.ui.theme.PlayVerseBackground
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseTextSubtle

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    onNavigateToAllGames: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    onGameClick: (Game) -> Unit = {},
    onBannerClick: (Banner) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlayVerseBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            PlayVerseHeader()

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
                            HomeBanner(
                                banners = state.banners,
                                onBannerClick = onBannerClick
                            )

                            HomeGameGridSection(
                                title = "Popular Game",
                                games = state.popularGames.ifEmpty { state.games.take(8) },
                                onSeeAllClick = onNavigateToAllGames,
                                onGameClick = onGameClick
                            )

                            HomeGameGridSection(
                                title = "Flash Game",
                                games = state.flashGames.ifEmpty { state.games.takeLast(4) },
                                onSeeAllClick = onNavigateToAllGames,
                                onGameClick = onGameClick
                            )
                        }
                    }
                    is GameUiState.Loading -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = PlayVerseBrandBlue)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Đang tải trang Home...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PlayVerseTextSubtle
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
    }
}
