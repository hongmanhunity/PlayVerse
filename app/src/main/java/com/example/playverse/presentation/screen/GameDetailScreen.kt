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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.playverse.domain.model.Comment
import com.example.playverse.domain.model.Game
import com.example.playverse.domain.model.User
import com.example.playverse.presentation.component.gamedetail.GameDetailBottomBar
import com.example.playverse.presentation.component.gamedetail.GameDetailCommentSection
import com.example.playverse.presentation.component.gamedetail.GameDetailDescriptionSection
import com.example.playverse.presentation.component.gamedetail.GameDetailHeroBanner
import com.example.playverse.presentation.component.gamedetail.GameDetailPreviewGallery
import com.example.playverse.presentation.component.gamedetail.GameDetailQuickStats
import com.example.playverse.presentation.component.gamedetail.GameDetailTagsSection
import com.example.playverse.ui.theme.PlayVerseAccentPurple
import com.example.playverse.ui.theme.PlayVerseBackground
import com.example.playverse.ui.theme.PlayVerseBorder
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseCardBg
import com.example.playverse.ui.theme.PlayVerseTextBody
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSubtle

@Composable
fun GameDetailScreen(
    game: Game,
    comments: List<Comment> = emptyList(),
    currentUser: User? = null,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onPostComment: (content: String, rating: Float) -> Unit = { _, _ -> },
    onNavigateToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val gameLink = "https://playverse.app/game/${game.slug.ifEmpty { game.id }}"

    var isDownloading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlayVerseBackground)
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
                    clipboardManager.setText(AnnotatedString(gameLink))
                    Toast.makeText(context, "Đã sao chép liên kết game: $gameLink", Toast.LENGTH_SHORT).show()
                },
                onFavoriteClick = {
                    onFavoriteClick()
                    val msg = if (!isFavorite) "Đã thêm vào yêu thích!" else "Đã xóa khỏi yêu thích!"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                },
                backgroundColor = PlayVerseBackground,
                primaryText = PlayVerseTextPrimary,
                secondaryText = PlayVerseTextSubtle,
                actionBlue = PlayVerseBrandBlue,
                accentPurple = PlayVerseAccentPurple
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
                    cardBg = PlayVerseCardBg,
                    borderColor = PlayVerseBorder,
                    primaryText = PlayVerseTextPrimary,
                    secondaryText = PlayVerseTextSubtle,
                    actionBlue = PlayVerseBrandBlue,
                    accentPurple = PlayVerseAccentPurple
                )

                // Preview Screenshots Gallery (4-5 images)
                GameDetailPreviewGallery(
                    game = game,
                    primaryText = PlayVerseTextPrimary,
                    borderColor = PlayVerseBorder
                )

                // Tags & Genres
                GameDetailTagsSection(
                    game = game,
                    primaryText = PlayVerseTextPrimary,
                    actionBlue = PlayVerseBrandBlue
                )

                // Description & Info Section
                GameDetailDescriptionSection(
                    game = game,
                    cardBg = PlayVerseCardBg,
                    borderColor = PlayVerseBorder,
                    primaryText = PlayVerseTextPrimary,
                    secondaryText = PlayVerseTextSubtle,
                    bodyText = PlayVerseTextBody,
                    actionBlue = PlayVerseBrandBlue
                )

                // Comments & Reviews Section
                GameDetailCommentSection(
                    comments = comments,
                    currentUser = currentUser,
                    onPostComment = onPostComment,
                    onNavigateToLogin = onNavigateToLogin
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
            backgroundColor = PlayVerseBackground,
            actionBlue = PlayVerseBrandBlue
        )
    }
}
