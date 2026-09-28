package com.example.playverse.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.data.local.UserPreferences
import com.example.playverse.domain.model.Post
import com.example.playverse.presentation.component.common.PlayVerseHeader
import com.example.playverse.presentation.state.CommunityUiState
import com.example.playverse.presentation.viewmodel.CommunityViewModel
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseDivider
import com.example.playverse.ui.theme.PlayVerseTextBody
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSecondary

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel,
    onTabSelected: (String) -> Unit = {},
    onCenterSearchClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }
    val user = userPreferences.getUser()
    val postsState by viewModel.postsState.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header PlayVerse cơ bản thống nhất
            PlayVerseHeader()

            HorizontalDivider(color = PlayVerseDivider)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = postsState) {
                    is CommunityUiState.Loading -> {
                        CircularProgressIndicator(
                            color = PlayVerseBrandBlue,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is CommunityUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is CommunityUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.posts, key = { it.id }) { post ->
                                PostCardItem(post = post)
                                HorizontalDivider(
                                    color = PlayVerseDivider,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(140.dp))
                            }
                        }
                    }
                }
            }
        }

        // Nút đăng bài nổi phong cách Floating Gradient Pill hiện đại
        Surface(
            onClick = {
                if (userPreferences.isLoggedIn()) {
                    showCreateDialog = true
                } else {
                    Toast.makeText(context, "Vui lòng đăng nhập để đăng bài!", Toast.LENGTH_SHORT).show()
                }
            },
            shape = CircleShape,
            color = Color.Transparent,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 86.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2563EB), // PlayVerse Brand Blue
                                Color(0xFF1D4ED8)  // Deep Royal Blue
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Đăng bài",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Đăng bài",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }

        if (showCreateDialog) {
            CreatePostDialog(
                onDismiss = { showCreateDialog = false },
                onSubmit = { title, content, gameTag ->
                    viewModel.createPost(
                        token = user?.token ?: "",
                        title = title,
                        content = content,
                        gameTitle = gameTag
                    ) { success ->
                        if (success) {
                            Toast.makeText(context, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show()
                            showCreateDialog = false
                        } else {
                            Toast.makeText(context, "Đăng bài thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun PostCardItem(
    post: Post
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (post.authorAvatar.isNotEmpty()) {
                AsyncImage(
                    model = post.authorAvatar,
                    contentDescription = "Author Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(PlayVerseBrandBlue, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = post.authorName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlayVerseTextPrimary
                )
                Text(
                    text = post.timeAgo,
                    fontSize = 11.sp,
                    color = PlayVerseTextSecondary
                )
            }

            Surface(
                color = PlayVerseBrandBlue.copy(alpha = 0.08f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = post.gameTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = PlayVerseBrandBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = post.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PlayVerseTextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = post.content,
            fontSize = 14.sp,
            color = PlayVerseTextBody,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun CreatePostDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var gameTitle by remember { mutableStateOf("Chung") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSubmit(title, content, gameTitle)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PlayVerseBrandBlue)
            ) {
                Text(text = "Đăng Bài", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Hủy", color = PlayVerseTextSecondary)
            }
        },
        title = {
            Text(
                text = "Tạo Bài Viết Thảo Luận",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = gameTitle,
                    onValueChange = { gameTitle = it },
                    label = { Text("Chủ đề / Tên Game") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tiêu đề bài viết") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung chia sẻ...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
