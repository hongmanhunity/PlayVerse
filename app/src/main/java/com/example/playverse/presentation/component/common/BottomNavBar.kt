package com.example.playverse.presentation.component.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.ui.theme.PlayVerseBrandBlue
import com.example.playverse.ui.theme.PlayVerseTextPrimary
import com.example.playverse.ui.theme.PlayVerseTextSubtle

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedTab: String = "Home",
    onTabSelected: (String) -> Unit = {},
    onCenterSearchClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Thanh nền chứa 4 tab chính
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White.copy(alpha = 0.98f),
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Home
                NavItem(
                    icon = Icons.Filled.Home,
                    label = "Home",
                    isSelected = selectedTab == "Home",
                    onClick = { onTabSelected("Home") },
                    modifier = Modifier.weight(1f)
                )

                // Tab 2: Diễn Đàn (Community)
                NavItem(
                    icon = Icons.Outlined.Forum,
                    label = "Diễn Đàn",
                    isSelected = selectedTab == "Community",
                    onClick = { onTabSelected("Community") },
                    modifier = Modifier.weight(1f)
                )

                // Khoảng trống ở giữa nhường chỗ cho nút tròn nổi bật
                Spacer(modifier = Modifier.width(60.dp))

                // Tab 3: Yêu thích (Wishlist)
                NavItem(
                    icon = if (selectedTab == "Wishlist") Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    label = "Yêu thích",
                    isSelected = selectedTab == "Wishlist",
                    onClick = { onTabSelected("Wishlist") },
                    modifier = Modifier.weight(1f)
                )

                // Tab 4: Cá nhân (Profile)
                NavItem(
                    icon = Icons.Outlined.Person,
                    label = "Profile",
                    isSelected = selectedTab == "Profile",
                    onClick = { onTabSelected("Profile") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Nút tròn tìm kiếm ở chính giữa (Nổi lên trên)
        Surface(
            onClick = onCenterSearchClick,
            shape = CircleShape,
            color = PlayVerseBrandBlue,
            shadowElevation = 8.dp,
            modifier = Modifier
                .size(54.dp)
                .offset(y = (-14).dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Floating Button",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = if (isSelected) PlayVerseBrandBlue else PlayVerseTextSubtle

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = tint
            )
        }
    }
}
