package com.example.playverse.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.User
import com.example.playverse.presentation.component.common.BottomNavBar

/**
 * SCREEN: ProfileScreen (Màn hình Cá nhân)
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
    val backgroundColor = Color(0xFFF4F7FC)
    val primaryTextColor = Color(0xFF191C24)
    val secondaryTextColor = Color(0xFF8A94A6)
    val brandBlue = Color(0xFF0066FF)

    val displayName = user?.name?.ifEmpty { "PlayVerse Gamer" } ?: "PlayVerse Gamer"
    val displayEmail = user?.email?.ifEmpty { "gamer@playverse.com" } ?: "gamer@playverse.com"
    val avatarInitial = displayName.take(1).uppercase()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryTextColor
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (!isLoggedIn) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = brandBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(80.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = "Guest Avatar",
                                        tint = brandBlue,
                                        modifier = Modifier.size(44.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Bạn chưa đăng nhập",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryTextColor
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Đăng nhập hoặc tạo tài khoản để trải nghiệm đầy đủ các tính năng của PlayVerse!",
                                fontSize = 14.sp,
                                color = secondaryTextColor,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onNavigateToLogin,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = brandBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Đăng Nhập",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = onNavigateToRegister,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = brandBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Tạo Tài Khoản Mới",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = brandBlue,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = avatarInitial,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = displayName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryTextColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = displayEmail,
                                    fontSize = 13.sp,
                                    color = secondaryTextColor
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFFBEB),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "VIP Member",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Cài đặt & Hỗ trợ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryTextColor,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
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
                            subtitle = "FAQ và liên hệ hỗ trợ"
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        ProfileOptionItem(
                            icon = Icons.Outlined.Shield,
                            title = "Điều khoản & Chính sách",
                            subtitle = "Bảo mật dữ liệu cá nhân"
                        )

                        if (isLoggedIn) {
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                            ProfileOptionItem(
                                icon = Icons.Outlined.Logout,
                                title = "Đăng xuất",
                                titleColor = Color(0xFFFF3B30),
                                onClick = onLogoutClick
                            )
                        }
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
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = titleColor,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF8A94A6)
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Arrow Right",
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(20.dp)
        )
    }
}
