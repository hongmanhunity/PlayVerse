package com.example.playverse.presentation.component.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * COMPONENT AUTH: AuthHeader (Phiên bản Xanh / Trắng Hiện Đại)
 * VỊ TRÍ: presentation/component/auth/AuthHeader.kt
 */
@Composable
fun AuthHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit = {}
) {
    val brandBlue = Color(0xFF0066FF)
    val primaryTextColor = Color(0xFF191C24)
    val secondaryTextColor = Color(0xFF8A94A6)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            onClick = onBackClick,
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = primaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brandBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsEsports,
                    contentDescription = "PlayVerse Logo",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "PlayVerse",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = brandBlue
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = secondaryTextColor
        )
    }
}
