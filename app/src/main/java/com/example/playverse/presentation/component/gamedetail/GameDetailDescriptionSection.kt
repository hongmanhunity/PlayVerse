package com.example.playverse.presentation.component.gamedetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Game

@Composable
fun GameDetailDescriptionSection(
    game: Game,
    modifier: Modifier = Modifier,
    cardBg: Color = Color.White,
    borderColor: Color = Color(0xFFE2E8F0),
    primaryText: Color = Color(0xFF191C24),
    secondaryText: Color = Color(0xFF8A94A6),
    bodyText: Color = Color(0xFF334155),
    actionBlue: Color = Color(0xFF0066FF)
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // DESCRIPTION BOX
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Mô tả game",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            val fullDesc = game.description.ifEmpty {
                "Trải nghiệm siêu phẩm game với đồ họa cực đỉnh, lối chơi hấp dẫn và hệ thống nhiệm vụ đa dạng. Tham gia ngay vào thế giới PlayVerse để khám phá những khoảnh khắc giải trí tuyệt vời nhất cùng bạn bè!"
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fullDesc,
                    fontSize = 14.sp,
                    color = bodyText,
                    lineHeight = 22.sp,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis
                )

                if (fullDesc.length > 120) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isExpanded) "Thu gọn" else "Xem thêm",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = actionBlue,
                        modifier = Modifier.clickable { isExpanded = !isExpanded }
                    )
                }
            }
        }

        // HIGHLIGHTS / SPECS CARDS
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Thông tin bổ sung",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LightInfoBox(
                    modifier = Modifier.weight(1f),
                    title = "Hệ điều hành",
                    value = "Android 7.0+",
                    cardBg = cardBg,
                    borderColor = borderColor,
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )
                LightInfoBox(
                    modifier = Modifier.weight(1f),
                    title = "Chế độ chơi",
                    value = "Online / Offline",
                    cardBg = cardBg,
                    borderColor = borderColor,
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )
            }
        }
    }
}

@Composable
private fun LightInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 11.sp,
                color = secondaryText,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                color = primaryText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
