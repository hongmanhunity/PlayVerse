package com.example.playverse.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Game
import com.example.playverse.presentation.component.common.PlayVerseHeader
import com.example.playverse.presentation.component.wishlist.WishlistEmptyState
import com.example.playverse.presentation.component.wishlist.WishlistVerticalGameCard
import com.example.playverse.ui.theme.PlayVerseBackground
import com.example.playverse.ui.theme.PlayVerseBorder
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSubtle

@Composable
fun WishlistScreen(
    isLoggedIn: Boolean = false,
    wishlistGames: List<Game>,
    onGameClick: (Game) -> Unit,
    onRemoveFromWishlist: (Game) -> Unit,
    onBackClick: (() -> Unit)? = null,
    onNavigateToLogin: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    onCenterSearchClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
            // 1. HEADER SECTION (PLAYVERSE)
            PlayVerseHeader(
                onBackClick = onBackClick
            )

            // 2. MAIN CONTENT AREA
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (!isLoggedIn) {
                    // TRẠNG THÁI CHƯA ĐĂNG NHẬP (YÊU CẦU ĐĂNG NHẬP)
                    WishlistLoginRequiredState(
                        onLoginClick = onNavigateToLogin,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (wishlistGames.isEmpty()) {
                    // ĐÃ ĐĂNG NHẬP NHƯNG DANH SÁCH TRỐNG
                    WishlistEmptyState(
                        onExploreClick = onCenterSearchClick,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    // DANH SÁCH GAME YÊU THÍCH THỰC TẾ
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 100.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(wishlistGames, key = { it.id }) { game ->
                            WishlistVerticalGameCard(
                                game = game,
                                onGameClick = { onGameClick(game) },
                                onRemoveClick = {
                                    onRemoveFromWishlist(game)
                                    Toast.makeText(context, "Đã xóa ${game.title} khỏi Yêu thích", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WishlistLoginRequiredState(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, PlayVerseBorder, RoundedCornerShape(20.dp))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(PlayVerseBrandBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = PlayVerseBrandBlue,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = "Bạn chưa đăng nhập",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = PlayVerseTextPrimary
        )

        Text(
            text = "Vui lòng đăng nhập để lưu, quản lý và đồng bộ danh sách game yêu thích của bạn.",
            fontSize = 13.sp,
            color = PlayVerseTextSubtle,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onLoginClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PlayVerseBrandBlue,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().height(46.dp)
        ) {
            Text(text = "Đăng Nhập Ngay", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
