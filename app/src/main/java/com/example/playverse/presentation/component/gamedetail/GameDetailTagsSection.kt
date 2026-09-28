package com.example.playverse.presentation.component.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Game

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameDetailTagsSection(
    game: Game,
    modifier: Modifier = Modifier,
    primaryText: Color = Color(0xFF191C24),
    actionBlue: Color = Color(0xFF0066FF)
) {
    val tagList = buildList {
        if (game.category.isNotEmpty()) {
            addAll(game.category.split(Regex("[,/\\-]")).map { it.trim() }.filter { it.isNotEmpty() })
        }
        game.tags.forEach { rawTag ->
            addAll(rawTag.split(Regex("[,/\\-]")).map { it.trim() }.filter { it.isNotEmpty() })
        }
    }.distinct().ifEmpty { listOf("Action", "Adventure", "Multiplayer", "3D Graphic") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Thẻ & Thể loại",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = primaryText
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            tagList.forEach { tagText ->
                LightTagChip(text = tagText, actionColor = actionBlue)
            }
        }
    }
}

@Composable
private fun LightTagChip(
    text: String,
    actionColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = actionColor
        )
    }
}
