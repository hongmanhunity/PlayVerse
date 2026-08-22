package com.example.playverse.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.data.local.UserPreferences
import com.example.playverse.domain.model.User
import com.example.playverse.presentation.component.common.BottomNavBar

/**
 * SCREEN: ProfileScreen (Kéo chữ Profile và toàn bộ nội dung lên sát mép trên)
 * VỊ TRÍ: presentation/screen/ProfileScreen.kt
 */
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

    val primaryTextColor = Color(0xFF191C24)
    val secondaryTextColor = Color(0xFF64748B)
    val brandBlue = Color(0xFF0066FF)

    val displayName = user?.name?.ifEmpty { "PlayVerse Gamer" } ?: "PlayVerse Gamer"
    val displayEmail = user?.email?.ifEmpty { "gamer@playverse.com" } ?: "gamer@playverse.com"
    val avatarInitial = displayName.take(1).uppercase()
    val userIdCode = if (!user?.id.isNullOrEmpty()) user.id else "Chưa cập nhật"

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
        ) {
            // Header Profile đẩy sát lên trên
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryTextColor
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!isLoggedIn) {
                    // Chưa đăng nhập (Hiển thị đẩy cao)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Bạn chưa đăng nhập",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Vui lòng đăng nhập để xem thông tin cá nhân",
                            fontSize = 14.sp,
                            color = secondaryTextColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onNavigateToLogin,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = brandBlue),
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
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = brandBlue),
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
                    // Đã đăng nhập (Đẩy cao sát header)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        // Avatar + Nút đổi ảnh
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clickable { imagePickerLauncher.launch("image/*") }
                            ) {
                                if (!currentAvatarUri.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = currentAvatarUri,
                                        contentDescription = "User Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(brandBlue, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = avatarInitial,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = displayName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryTextColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Đổi ảnh đại diện",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = brandBlue,
                                    modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Thông tin dạng chữ cơ bản
                        Text(
                            text = "Thông tin cá nhân",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ProfileInfoRow(label = "Họ và tên:", value = displayName)
                        ProfileInfoRow(label = "Email:", value = displayEmail)
                        ProfileInfoRow(label = "Mã tài khoản:", value = userIdCode)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))

                Text(
                    text = "Cài đặt & Hỗ trợ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryTextColor
                )

                // Cài đặt dạng chữ đơn giản
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileOptionItem(
                        icon = Icons.Outlined.DarkMode,
                        title = "Giao diện (Sáng / Tối)",
                        subtitle = "Tự động theo hệ thống"
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ProfileOptionItem(
                        icon = Icons.Outlined.Language,
                        title = "Ngôn ngữ",
                        subtitle = "Tiếng Việt"
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ProfileOptionItem(
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        title = "Trợ giúp & Hỗ trợ",
                        subtitle = "FAQ và liên hệ"
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ProfileOptionItem(
                        icon = Icons.Outlined.Shield,
                        title = "Điều khoản & Chính sách",
                        subtitle = "Bảo mật thông tin"
                    )

                    if (isLoggedIn) {
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        ProfileOptionItem(
                            icon = Icons.AutoMirrored.Outlined.Logout,
                            title = "Đăng xuất",
                            titleColor = Color(0xFFEF4444),
                            onClick = onLogoutClick
                        )
                    }
                }
            }
        }

        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedTab = "Profile",
            onTabSelected = onTabSelected,
            onCenterSearchClick = onCenterSearchClick
        )
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
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF64748B)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF191C24)
        )
    }
}

@Composable
private fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = Color(0xFF191C24),
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
                    color = Color(0xFF64748B)
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Arrow Right",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
        )
    }
}
