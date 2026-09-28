package com.example.playverse.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Game
import com.example.playverse.presentation.component.allgames.AllGamesHeader
import com.example.playverse.presentation.component.allgames.AllGamesItemRow
import com.example.playverse.presentation.state.GameUiState
import com.example.playverse.presentation.viewmodel.GameViewModel
import com.example.playverse.ui.theme.PlayVerseBackground
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSecondary
import com.example.playverse.ui.theme.PlayVerseTextSubtle

@Composable
fun AllGamesScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    onBackClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    onGameClick: (Game) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Tất cả") }

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
            // HEADER WITH SEARCH TOGGLE
            AllGamesHeader(
                title = "PlayVerse",
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                isSearchVisible = isSearchVisible,
                onToggleSearch = {
                    isSearchVisible = !isSearchVisible
                    if (!isSearchVisible) searchQuery = ""
                },
                onBackClick = onBackClick,
                onNotificationClick = { }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {
                    is GameUiState.Success -> {
                        // Extract Unique Categories
                        val categories = remember(state.games) {
                            buildList {
                                add("Tất cả")
                                addAll(
                                    state.games
                                        .flatMap { game ->
                                            game.category.split(Regex("[,/\\-|]")).map { it.trim() }.filter { it.isNotEmpty() }
                                        }
                                        .distinct()
                                        .sorted()
                                )
                            }
                        }

                        // Filter Games by Selected Category & Search Query
                        val filteredGames = remember(state.games, selectedCategory, searchQuery) {
                            state.games.filter { game ->
                                val matchesCategory = selectedCategory == "Tất cả" ||
                                        game.category.split(Regex("[,/\\-|]")).any { cat -> cat.trim().equals(selectedCategory, ignoreCase = true) }
                                val matchesQuery = searchQuery.isEmpty() ||
                                        game.title.contains(searchQuery, ignoreCase = true) ||
                                        game.publisher.contains(searchQuery, ignoreCase = true) ||
                                        game.category.contains(searchQuery, ignoreCase = true)
                                matchesCategory && matchesQuery
                            }
                        }

                        Column(modifier = Modifier.fillMaxSize()) {
                            // CATEGORY CHIPS SCROLLABLE ROW
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(categories) { category ->
                                    CategoryChip(
                                        text = category,
                                        isSelected = selectedCategory == category,
                                        onSelect = { selectedCategory = category },
                                        actionColor = PlayVerseBrandBlue,
                                        primaryTextColor = PlayVerseTextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // GAME LIST / EMPTY SEARCH STATE
                            if (filteredGames.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 4.dp,
                                        bottom = 100.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(
                                        items = filteredGames,
                                        key = { game -> game.id }
                                    ) { game ->
                                        AllGamesItemRow(
                                            game = game,
                                            onClick = { onGameClick(game) }
                                        )
                                    }
                                }
                            } else {
                                // EMPTY SEARCH STATE VIEW
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 32.dp, vertical = 40.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.SearchOff,
                                        contentDescription = "No results",
                                        tint = PlayVerseTextSubtle,
                                        modifier = Modifier.size(56.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Không tìm thấy game phù hợp",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PlayVerseTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Thử tìm kiếm với từ khóa khác hoặc bỏ chọn bộ lọc danh mục.",
                                        fontSize = 13.sp,
                                        color = PlayVerseTextSubtle,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Button(
                                        onClick = {
                                            searchQuery = ""
                                            selectedCategory = "Tất cả"
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = PlayVerseBrandBlue)
                                    ) {
                                        Text("Xóa bộ lọc", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
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
                                text = "Đang tải danh sách game...",
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

@Composable
private fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    actionColor: Color,
    primaryTextColor: Color
) {
    val chipBg by animateColorAsState(
        targetValue = if (isSelected) actionColor else Color.White,
        animationSpec = tween(durationMillis = 200),
        label = "chipBg"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else primaryTextColor,
        animationSpec = tween(durationMillis = 200),
        label = "textColor"
    )

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) actionColor else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(chipBg)
            .clickable { onSelect() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}
