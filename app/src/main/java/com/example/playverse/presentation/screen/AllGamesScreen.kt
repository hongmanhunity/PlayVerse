package com.example.playverse.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.playverse.presentation.component.allgames.AllGamesHeader
import com.example.playverse.presentation.component.allgames.AllGamesItemRow
import com.example.playverse.presentation.component.common.BottomNavBar
import com.example.playverse.presentation.state.GameUiState
import com.example.playverse.presentation.viewmodel.GameViewModel

/**
 * SCREEN: AllGamesScreen (Trang Tất cả Game)
 * VỊ TRÍ: presentation/screen/AllGamesScreen.kt
 */
@Composable
fun AllGamesScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    onBackClick: () -> Unit = {},
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
            AllGamesHeader(
                title = "Tất cả Game",
                onBackClick = onBackClick,
                onNotificationClick = { },
                onSearchClick = { }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is GameUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                top = 8.dp,
                                bottom = 100.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = state.games,
                                key = { game -> game.id }
                            ) { game ->
                                AllGamesItemRow(
                                    game = game,
                                    onClick = { }
                                )
                            }
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
                                text = "Đang tải danh sách game...",
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
            selectedTab = "",
            onTabSelected = onTabSelected,
            onCenterSearchClick = { }
        )
    }
}
