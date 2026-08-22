package com.example.playverse.presentation.component.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * COMPONENT AUTH: GoogleAuthButton (Nút Đăng nhập với biểu tượng Google chuẩn 4 màu)
 * VỊ TRÍ: presentation/component/auth/GoogleAuthButton.kt
 */
@Composable
fun GoogleAuthButton(
    text: String = "Đăng nhập với Google",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryText = Color(0xFF191C24)
    val borderColor = Color(0xFFE2E8F0)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Authentic 4-Color Google Logo Canvas Icon
            GoogleLogoIcon(modifier = Modifier.size(20.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryText
            )
        }
    }
}

/**
 * Biểu tượng Google 'G' 4 màu chuẩn (Đỏ, Vàng, Lục, Lam)
 */
@Composable
fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokeWidth = width * 0.22f
        val arcSize = Size(width - strokeWidth, height - strokeWidth)
        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
        val center = Offset(width / 2f, height / 2f)

        // Red top arc (#EA4335)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = -45f,
            sweepAngle = -135f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )

        // Yellow left arc (#FBBC05)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = -180f,
            sweepAngle = -90f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )

        // Green bottom arc (#34A853)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = -270f,
            sweepAngle = -45f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )

        // Blue right arc (#4285F4)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -315f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )

        // Horizontal bar of G
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(center.x, center.y),
            end = Offset(width - strokeWidth / 4f, center.y),
            strokeWidth = strokeWidth
        )
    }
}
