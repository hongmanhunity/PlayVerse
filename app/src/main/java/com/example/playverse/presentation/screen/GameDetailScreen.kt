package com.example.playverse.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.playverse.domain.model.Game
import com.example.playverse.presentation.component.gamedetail.GameDetailBottomBar
import com.example.playverse.presentation.component.gamedetail.GameDetailDescriptionSection
import com.example.playverse.presentation.component.gamedetail.GameDetailHeroBanner
import com.example.playverse.presentation.component.gamedetail.GameDetailPreviewGallery
import com.example.playverse.presentation.component.gamedetail.GameDetailQuickStats
import com.example.playverse.presentation.component.gamedetail.GameDetailTagsSection

/**
 * SCREEN: GameDetailScreen (Màn hình Chi tiết Game modular hóa chuẩn Clean Architecture)
 * VỊ TRÍ: presentation/screen/GameDetailScreen.kt
 */
@Composable
fun GameDetailScreen(
    game: Game,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }

    // White Light Theme Palette
    val backgroundColor = Color(0xFFF4F7FC)
    val cardBg = Color.White
    val borderColor = Color(0xFFE2E8F0)
    val primaryText = Color(0xFF191C24)
    val secondaryText = Color(0xFF8A94A6)
    val bodyText = Color(0xFF334155)
    val actionBlue = Color(0xFF0066FF)
    val accentPurple = Color(0xFF6366F1)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            // 1. HERO BANNER
            GameDetailHeroBanner(
                game = game,
                isFavorite = isFavorite,
                onBackClick = onBackClick,
                onShareClick = {
                    Toast.makeText(context, "Đã sao chép liên kết chia sẻ!", Toast.LENGTH_SHORT).show()
                },
                onFavoriteClick = {
                    isFavorite = !isFavorite
                    val msg = if (isFavorite) "Đã thêm vào yêu thích!" else "Đã xóa khỏi yêu thích!"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                },
                backgroundColor = backgroundColor,
                primaryText = primaryText,
                secondaryText = secondaryText,
                actionBlue = actionBlue,
                accentPurple = accentPurple
            )

            // 2. MAIN CONTENT SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Quick Stats Row
                GameDetailQuickStats(
                    game = game,
                    cardBg = cardBg,
                    borderColor = borderColor,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    actionBlue = actionBlue,
                    accentPurple = accentPurple
                )

                // Preview Screenshots Gallery (4-5 images)
                GameDetailPreviewGallery(
                    game = game,
                    primaryText = primaryText,
                    borderColor = borderColor
                )

                // Tags & Genres
                GameDetailTagsSection(
                    game = game,
                    primaryText = primaryText,
                    actionBlue = actionBlue
                )

                // Description & Info Section
                GameDetailDescriptionSection(
                    game = game,
                    cardBg = cardBg,
                    borderColor = borderColor,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    bodyText = bodyText,
                    actionBlue = actionBlue
                )
            }
        }

        // 3. STICKY BOTTOM ACTION BAR
        GameDetailBottomBar(
            isDownloading = isDownloading,
            onPlayClick = {
                isDownloading = true
                Toast.makeText(context, "Đang mở game ${game.title}...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            backgroundColor = backgroundColor,
            actionBlue = actionBlue
        )
    }
}
