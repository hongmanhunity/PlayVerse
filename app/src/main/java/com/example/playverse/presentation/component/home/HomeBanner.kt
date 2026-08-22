package com.example.playverse.presentation.component.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playverse.domain.model.Banner

/**
 * COMPONENT HOME: HomeBanner (Slider cuộn ngang + Dots Indicator)
 * VỊ TRÍ: presentation/component/home/HomeBanner.kt
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeBanner(
    banners: List<Banner> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Choose Your Game",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF191C24)
        )

        if (banners.isEmpty()) {
            // Skeleton / Fallback Card khi chưa có data
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Đang tải danh sách banner...",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { banners.size })

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Horizontal Pager cho phép trượt ngang các Banner
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    pageSpacing = 12.dp
                ) { page ->
                    val banner = banners[page]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E293B))
                    ) {
                        // Hình ảnh Banner
                        AsyncImage(
                            model = banner.image,
                            contentDescription = banner.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Lớp phủ Gradient mờ đằng sau chữ để chữ luôn nổi bật dễ đọc
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.75f)
                                        ),
                                        startY = 60f
                                    )
                                )
                        )

                        // Tiêu đề của Banner
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = banner.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Hàng chấm nhỏ (Dots Indicator) hiển thị vị trí trang hiện tại
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    repeat(banners.size) { index ->
                        val isSelected = pagerState.currentPage == index

                        val width by animateDpAsState(
                            targetValue = if (isSelected) 22.dp else 7.dp,
                            animationSpec = tween(durationMillis = 300),
                            label = "dotWidth"
                        )
                        val color by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF0066FF) else Color(0xFFCBD5E1),
                            animationSpec = tween(durationMillis = 300),
                            label = "dotColor"
                        )

                        Box(
                            modifier = Modifier
                                .height(7.dp)
                                .width(width)
                                .clip(if (isSelected) RoundedCornerShape(4.dp) else CircleShape)
                                .background(color)
                        )
                    }
                }
            }
        }
    }
}
