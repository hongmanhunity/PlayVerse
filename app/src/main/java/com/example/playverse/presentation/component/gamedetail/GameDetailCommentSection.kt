package com.example.playverse.presentation.component.gamedetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Comment
import com.example.playverse.domain.model.User
import com.example.playverse.presentation.component.comment.CommentCard
import com.example.playverse.presentation.component.comment.CommentGuestPrompt
import com.example.playverse.presentation.component.comment.CommentHeader
import com.example.playverse.presentation.component.comment.CommentInputForm
import com.example.playverse.ui.theme.*

@Composable
fun GameDetailCommentSection(
    comments: List<Comment>,
    currentUser: User?,
    onPostComment: (content: String, rating: Float) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Tính điểm trung bình đánh giá
    val avgRating = if (comments.isNotEmpty()) {
        comments.map { it.rating }.average().toFloat()
    } else 5.0f

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. TIÊU ĐỀ SECTION & THỐNG KÊ ĐÁNH GIÁ (CommentHeader Component)
        CommentHeader(
            totalComments = comments.size,
            averageRating = avgRating
        )

        HorizontalDivider(color = PlayVerseBorder, thickness = 1.dp)

        // 2. KHUNG NHẬP BÌNH LUẬN NẾU ĐÃ ĐĂNG NHẬP / THẺ GỢI Ý ĐĂNG NHẬP (CommentInputForm / CommentGuestPrompt)
        if (currentUser != null) {
            CommentInputForm(
                currentUser = currentUser,
                onPostComment = onPostComment
            )
        } else {
            CommentGuestPrompt(
                onNavigateToLogin = onNavigateToLogin
            )
        }

        // 3. DANH SÁCH BÌNH LUẬN CỦA CỘNG ĐỒNG (CommentCard Components)
        if (comments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có bình luận nào. Hãy là người đầu tiên đánh giá!",
                    color = PlayVerseTextSubtle,
                    fontSize = 14.sp
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                comments.forEach { comment ->
                    CommentCard(comment = comment)
                }
            }
        }
    }
}
