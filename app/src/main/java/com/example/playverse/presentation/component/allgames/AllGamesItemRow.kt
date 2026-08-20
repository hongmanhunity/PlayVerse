package com.example.playverse.presentation.component.allgames

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.domain.model.Game

/**
 * COMPONENT ALLGAMES: AllGamesItemRow (Căn chỉnh chuẩn Alignment.Top để Title bằng mép trên của ảnh)
 * VỊ TRÍ: presentation/component/allgames/AllGamesItemRow.kt
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllGamesItemRow(
    game: Game,
    onClick: () -> Unit = {},
    primaryTextColor: Color = Color(0xFF191C24),
    secondaryTextColor: Color = Color(0xFF8A94A6)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = game.thumbnail.ifEmpty { "https://images.unsplash.com/photo-1542751371-adc38448a05e" },
            contentDescription = game.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFE2E8F0))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = game.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            val tagList = buildList {
                if (game.category.isNotEmpty()) {
                    addAll(game.category.split(Regex("[,/\\-]")).map { it.trim() }.filter { it.isNotEmpty() })
                }
                game.tags.forEach { rawTag ->
                    addAll(rawTag.split(Regex("[,/\\-]")).map { it.trim() }.filter { it.isNotEmpty() })
                }
            }.distinct().ifEmpty { if (game.publisher.isNotEmpty()) listOf(game.publisher) else listOf("Action") }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 3,
                modifier = Modifier.fillMaxWidth()
            ) {
                tagList.take(3).forEach { tagText ->
                    TagChip(text = tagText)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%.1f", game.averageRating),
                    fontSize = 13.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFB800),
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "•",
                    fontSize = 12.sp,
                    color = secondaryTextColor
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = game.size.ifEmpty { "1.5 GB" },
                    fontSize = 13.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TagChip(
    text: String
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFE2E8F0),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64748B)
        )
    }
}
