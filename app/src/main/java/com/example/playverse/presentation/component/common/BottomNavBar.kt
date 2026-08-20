package com.example.playverse.presentation.component.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * COMPONENT COMMON: BottomNavBar
 * VỊ TRÍ: presentation/component/common/BottomNavBar.kt
 */
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    icon = Icons.Filled.Home,
                    label = "Home",
                    isSelected = selectedTab == "Home",
                    onClick = { onTabSelected("Home") }
                )

                NavItem(
                    icon = Icons.Outlined.AccountBalanceWallet,
                    label = "Wallet",
                    isSelected = selectedTab == "Wallet",
                    onClick = { onTabSelected("Wallet") }
                )

                Spacer(modifier = Modifier.width(48.dp))

                NavItem(
                    icon = Icons.Outlined.Notifications,
                    label = "Alerts",
                    isSelected = selectedTab == "Alerts",
                    onClick = { onTabSelected("Alerts") }
                )

                NavItem(
                    icon = Icons.Outlined.Person,
                    label = "Profile",
                    isSelected = selectedTab == "Profile",
                    onClick = { onTabSelected("Profile") }
                )
            }
        }

        Surface(
            shape = CircleShape,
            color = Color(0xFF0066FF),
            shadowElevation = 8.dp,
            modifier = Modifier
                .size(56.dp)
                .offset(y = (-14).dp)
                .clickable { onCenterSearchClick() }
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
    onClick: () -> Unit
) {
    val tint = if (isSelected) Color(0xFF191C24) else Color(0xFFA0AEC0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = tint
        )
    }
}
