package com.example.playverse.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.data.local.UserPreferences
import com.example.playverse.domain.model.User
import com.example.playverse.ui.theme.PlayVerseBorder
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseDivider
import com.example.playverse.ui.theme.PlayVerseErrorRed
import com.example.playverse.ui.theme.PlayVerseTextMuted
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSecondary

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean = false,
    user: User? = null,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    onCenterSearchClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val userPreferences = remember(context) { UserPreferences(context) }

    val displayName = user?.name?.ifEmpty { "PlayVerse Gamer" } ?: "PlayVerse Gamer"
    val displayEmail = user?.email?.ifEmpty { "gamer@playverse.com" } ?: "gamer@playverse.com"
    val avatarInitial = displayName.take(1).uppercase()

    var currentAvatarUri by remember(user) {
        mutableStateOf(user?.avatarUrl ?: userPreferences.getUser()?.avatarUrl)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentAvatarUri = it.toString()
            userPreferences.saveAvatarUri(it.toString())
            Toast.makeText(context, "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==================== PHẦN 1: THÔNG TIN NGƯỜI DÙNG HOẶC KHÁCH ====================
            if (!isLoggedIn) {
                // TRẠNG THÁI CHƯA ĐĂNG NHẬP
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PlayVerseBrandBlue.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Guest Avatar",
                            tint = PlayVerseBrandBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Bạn chưa đăng nhập",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlayVerseTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Đăng nhập hoặc đăng ký tài khoản để lưu game yêu thích, bình luận và tham gia diễn đàn.",
                        fontSize = 13.sp,
                        color = PlayVerseTextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onNavigateToLogin,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PlayVerseBrandBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Đăng Nhập",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onNavigateToRegister,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PlayVerseBrandBlue),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PlayVerseBrandBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Tạo Tài Khoản Mới",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // TRẠNG THÁI ĐÃ ĐĂNG NHẬP
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    // Header Avatar + Thông tin tóm tắt
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .clickable { imagePickerLauncher.launch("image/*") }
                        ) {
                            if (!currentAvatarUri.isNullOrEmpty()) {
                                AsyncImage(
                                    model = currentAvatarUri,
                                    contentDescription = "User Avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(PlayVerseBrandBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = avatarInitial,
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = displayName,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = PlayVerseTextPrimary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = displayEmail,
                                fontSize = 13.sp,
                                color = PlayVerseTextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Đổi ảnh đại diện",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PlayVerseBrandBlue,
                                modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = PlayVerseDivider)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Bảng chi tiết thông tin
                    Text(
                        text = "Thông tin tài khoản",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlayVerseTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileInfoRow(label = "Tên hiển thị:", value = displayName)
                    ProfileInfoRow(label = "Email:", value = displayEmail)
                }
            }

            // ==================== PHẦN 2: CÀI ĐẶT & HỖ TRỢ ====================
            HorizontalDivider(color = PlayVerseDivider)

            Text(
                text = "Cài đặt & Hỗ trợ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PlayVerseTextPrimary
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileOptionItem(
                    icon = Icons.Outlined.DarkMode,
                    title = "Giao diện (Sáng / Tối)",
                    subtitle = "Tự động theo hệ thống"
                )
                HorizontalDivider(color = PlayVerseDivider)

                ProfileOptionItem(
                    icon = Icons.Outlined.Language,
                    title = "Ngôn ngữ",
                    subtitle = "Tiếng Việt"
                )
                HorizontalDivider(color = PlayVerseDivider)

                ProfileOptionItem(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    title = "Trợ giúp & Hỗ trợ",
                    subtitle = "Câu hỏi thường gặp và liên hệ"
                )
                HorizontalDivider(color = PlayVerseDivider)

                ProfileOptionItem(
                    icon = Icons.Outlined.Shield,
                    title = "Điều khoản & Chính sách",
                    subtitle = "Bảo mật thông tin người dùng"
                )

                if (isLoggedIn) {
                    HorizontalDivider(color = PlayVerseDivider)
                    ProfileOptionItem(
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        title = "Đăng xuất",
                        titleColor = PlayVerseErrorRed,
                        onClick = onLogoutClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = PlayVerseTextSecondary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = PlayVerseTextPrimary
        )
    }
}

@Composable
private fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = PlayVerseTextPrimary,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = titleColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = PlayVerseTextSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Arrow Right",
            tint = PlayVerseTextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
