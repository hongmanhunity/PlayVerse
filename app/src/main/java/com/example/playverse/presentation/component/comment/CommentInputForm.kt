package com.example.playverse.presentation.component.comment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.domain.model.User
import com.example.playverse.ui.theme.*

@Composable
fun CommentInputForm(
    currentUser: User,
    onPostComment: (content: String, rating: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var commentText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf(5.0f) }
    var isPosting by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PlayVerseCardBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // User Info & Star Rating Picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!currentUser.avatarUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = currentUser.avatarUrl,
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, PlayVerseBrandBlue, CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PlayVerseBrandBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = PlayVerseBrandBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = currentUser.name.ifEmpty { "Game Player" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PlayVerseTextPrimary
                )
            }

            // Interactive Star Rating Picker (1 to 5 Stars)
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                for (i in 1..5) {
                    val starRating = i.toFloat()
                    val isSelected = starRating <= selectedRating
                    Icon(
                        imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star $i",
                        tint = if (isSelected) PlayVerseWarningAmber else PlayVerseTextSubtle,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { selectedRating = starRating }
                    )
                }
            }
        }

        // Input Field
        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = {
                Text(
                    text = "Chia sẻ cảm nhận của bạn về game này...",
                    color = PlayVerseTextMuted,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PlayVerseBackground,
                unfocusedContainerColor = PlayVerseBackground,
                focusedBorderColor = PlayVerseBrandBlue,
                unfocusedBorderColor = PlayVerseBorder,
                focusedTextColor = PlayVerseTextPrimary,
                unfocusedTextColor = PlayVerseTextPrimary
            ),
            maxLines = 4
        )

        // Submit Button
        Button(
            onClick = {
                if (commentText.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập nội dung bình luận!", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isPosting = true
                onPostComment(commentText.trim(), selectedRating)
                commentText = ""
                isPosting = false
                Toast.makeText(context, "Đã đăng bình luận thành công!", Toast.LENGTH_SHORT).show()
            },
            enabled = !isPosting && commentText.isNotBlank(),
            modifier = Modifier
                .align(Alignment.End)
                .height(42.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PlayVerseBrandBlue,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Gửi bình luận", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}
