package com.example.playverse.presentation.component.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.domain.model.Comment
import com.example.playverse.ui.theme.*

@Composable
fun CommentCard(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = PlayVerseCardBg
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Avatar, Name, Rating & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!comment.userAvatar.isNullOrEmpty()) {
                        AsyncImage(
                            model = comment.userAvatar,
                            contentDescription = comment.userName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .border(1.dp, PlayVerseBorder, CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PlayVerseBrandBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = comment.userName.take(1).uppercase(),
                                color = PlayVerseBrandBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Column {
                        Text(
                            text = comment.userName.ifEmpty { "Người chơi PlayVerse" },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PlayVerseTextPrimary
                        )
                        Text(
                            text = comment.createdAt.ifEmpty { "Gần đây" },
                            fontSize = 11.sp,
                            color = PlayVerseTextMuted
                        )
                    }
                }

                // Rating Stars Display Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = PlayVerseWarningAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = String.format(java.util.Locale.US, "%.1f", comment.rating),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlayVerseWarningAmber
                    )
                }
            }

            // Comment Content Text
            Text(
                text = comment.content,
                fontSize = 13.sp,
                color = PlayVerseTextBody,
                lineHeight = 18.sp
            )
        }
    }
}
