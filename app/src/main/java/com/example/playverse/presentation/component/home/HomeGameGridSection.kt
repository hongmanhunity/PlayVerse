package com.example.playverse.presentation.component.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.domain.model.Game

@Composable
fun HomeGameGridSection(
    title: String,
    games: List<Game>,
    onSeeAllClick: () -> Unit = {},
    onGameClick: (Game) -> Unit = {}
) {
    val actionColor = Color(0xFF0066FF)
    val primaryTextColor = Color(0xFF191C24)

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )

            Text(
                text = "All",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = actionColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onSeeAllClick() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        val rows = games.chunked(4)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            rows.forEach { rowGames ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowGames.forEach { game ->
                        GameGridItem(
                            game = game,
                            onClick = { onGameClick(game) }
                        )
                    }

                    repeat(4 - rowGames.size) {
                        Spacer(modifier = Modifier.width(76.dp))
                    }
                }
            }
        }
    }
}
