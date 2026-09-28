package com.example.playverse.presentation.component.gamedetail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.domain.model.Game

@Composable
fun GameDetailHeroBanner(
    game: Game,
    isFavorite: Boolean,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    backgroundColor: Color = Color(0xFFF4F7FC),
    primaryText: Color = Color(0xFF191C24),
    secondaryText: Color = Color(0xFF8A94A6),
    actionBlue: Color = Color(0xFF0066FF),
    accentPurple: Color = Color(0xFF6366F1)
) {
    val favoriteColor by animateColorAsState(
        targetValue = if (isFavorite) Color(0xFFEF4444) else secondaryText,
        animationSpec = tween(durationMillis = 300),
        label = "favoriteColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        // Background Banner Image
        AsyncImage(
            model = game.thumbnail.ifEmpty { "https://images.unsplash.com/photo-1542751371-adc38448a05e" },
            contentDescription = game.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Light Gradient Overlays
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.45f),
                            Color.Transparent,
                            backgroundColor.copy(alpha = 0.7f),
                            backgroundColor
                        )
                    )
                )
        )

        // Top Bar Controls (Back, Share, Favorite)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = primaryText,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Action Buttons (Share & Favorite)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onShareClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = primaryText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = favoriteColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Title & Publisher Overlay at bottom of Hero
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Category Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(actionBlue, accentPurple)
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = game.category.split(Regex("[,/\\-|]")).firstOrNull { it.isNotBlank() }?.trim() ?: "Gaming",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = game.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = primaryText,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Nhà phát hành: ${game.publisher.ifEmpty { "PlayVerse Games" }}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryText
            )
        }
    }
}
