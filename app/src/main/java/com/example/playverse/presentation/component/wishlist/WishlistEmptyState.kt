package com.example.playverse.presentation.component.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.ui.theme.*

@Composable
fun WishlistEmptyState(
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, PlayVerseBorder, RoundedCornerShape(20.dp))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(PlayVerseBrandBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = null,
                tint = PlayVerseBrandBlue,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = "Chưa có game nào trong thư viện",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = PlayVerseTextPrimary
        )

        Text(
            text = "Lưu lại các tựa game bạn yêu thích để dễ dàng truy cập và trải nghiệm bất cứ lúc nào!",
            fontSize = 13.sp,
            color = PlayVerseTextSubtle,
            lineHeight = 18.sp
        )

        Button(
            onClick = onExploreClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PlayVerseBrandBlue,
                contentColor = Color.White
            )
        ) {
            Text(text = "Khám phá Game ngay", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}
