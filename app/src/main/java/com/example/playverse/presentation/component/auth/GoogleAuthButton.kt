package com.example.playverse.presentation.component.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.R
import com.example.playverse.ui.theme.PlayVerseBrandBlue

@Composable
fun GoogleAuthButton(
    modifier: Modifier = Modifier,
    text: String = "Đăng nhập với Google",
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val isInteractive = enabled && !isLoading
    val primaryText = if (isInteractive) Color(0xFF191C24) else Color(0xFF94A3B8)
    val borderColor = Color(0xFFE2E8F0)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .clickable(enabled = isInteractive) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = PlayVerseBrandBlue
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Biểu tượng Google 'G' 4 màu chính thức (Đỏ, Vàng, Lục, Lam)
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryText
                )
            }
        }
    }
}
