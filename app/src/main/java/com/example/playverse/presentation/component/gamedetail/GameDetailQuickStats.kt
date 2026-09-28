package com.example.playverse.presentation.component.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Game

@Composable
fun GameDetailQuickStats(
    game: Game,
    modifier: Modifier = Modifier,
    cardBg: Color = Color.White,
    borderColor: Color = Color(0xFFE2E8F0),
    primaryText: Color = Color(0xFF191C24),
    secondaryText: Color = Color(0xFF8A94A6),
    actionBlue: Color = Color(0xFF0066FF),
    accentPurple: Color = Color(0xFF6366F1)
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Star,
            iconTint = Color(0xFFFFB800),
            value = String.format("%.1f", game.averageRating),
            label = "Đánh giá",
            cardBg = cardBg,
            borderColor = borderColor,
            primaryText = primaryText,
            secondaryText = secondaryText
        )

        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Storage,
            iconTint = actionBlue,
            value = game.size.ifEmpty { "1.5 GB" },
            label = "Dung lượng",
            cardBg = cardBg,
            borderColor = borderColor,
            primaryText = primaryText,
            secondaryText = secondaryText
        )

        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Category,
            iconTint = accentPurple,
            value = game.category.ifEmpty { "Action" },
            label = "Thể loại",
            cardBg = cardBg,
            borderColor = borderColor,
            primaryText = primaryText,
            secondaryText = secondaryText
        )

        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Business,
            iconTint = Color(0xFF10B981),
            value = game.publisher.take(6).ifEmpty { "Play" },
            label = "NPH",
            cardBg = cardBg,
            borderColor = borderColor,
            primaryText = primaryText,
            secondaryText = secondaryText
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
