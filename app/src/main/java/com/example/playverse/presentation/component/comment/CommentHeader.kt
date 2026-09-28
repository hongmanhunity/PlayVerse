package com.example.playverse.presentation.component.comment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSubtle
import com.example.playverse.ui.theme.PlayVerseWarningAmber

@Composable
fun CommentHeader(
    totalComments: Int,
    averageRating: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Bình luận & Đánh giá",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PlayVerseTextPrimary
            )
            Text(
                text = "$totalComments lượt đánh giá từ cộng đồng",
                fontSize = 13.sp,
                color = PlayVerseTextSubtle
            )
        }

        // Average Rating Badge
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PlayVerseWarningAmber.copy(alpha = 0.15f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = PlayVerseWarningAmber,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = String.format(java.util.Locale.US, "%.1f", averageRating),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlayVerseWarningAmber
                )
            }
        }
    }
}
