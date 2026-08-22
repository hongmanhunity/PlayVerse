package com.example.playverse.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.NotificationItem
import com.example.playverse.presentation.component.common.BottomNavBar
import com.example.playverse.presentation.viewmodel.CommunityViewModel
import com.example.playverse.presentation.viewmodel.NotificationUiState

/**
 * SCREEN: AlertsScreen (Thông Báo & Tin Tức Khuyến Mãi)
 * VỊ TRÍ: presentation/screen/AlertsScreen.kt
 */
@Composable
fun AlertsScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel,
    onTabSelected: (String) -> Unit = {},
    onCenterSearchClick: () -> Unit = {}
) {
    val notificationState by viewModel.notificationsState.collectAsState()

    val primaryTextColor = Color(0xFF191C24)
    val brandBlue = Color(0xFF0066FF)

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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Thông Báo & Ưu Đãi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryTextColor
                )
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = notificationState) {
                    is NotificationUiState.Loading -> {
                        CircularProgressIndicator(
                            color = brandBlue,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is NotificationUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is NotificationUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(state.notifications, key = { it.id }) { item ->
                                NotificationCardItem(item = item)
                            }

                            item {
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                }
            }
        }

        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedTab = "Alerts",
            onTabSelected = onTabSelected,
            onCenterSearchClick = onCenterSearchClick
        )
    }
}

@Composable
private fun NotificationCardItem(item: NotificationItem) {
    val brandBlue = Color(0xFF0066FF)

    val icon = when (item.type) {
        "promotion" -> Icons.Outlined.ConfirmationNumber
        "event" -> Icons.Outlined.Campaign
        else -> Icons.Outlined.Notifications
    }

    val badgeColor = when (item.type) {
        "promotion" -> Color(0xFFEF4444)
        "event" -> Color(0xFFF59E0B)
        else -> brandBlue
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(badgeColor.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = item.type,
                tint = badgeColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C24),
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = item.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.message,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.timeAgo,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}
