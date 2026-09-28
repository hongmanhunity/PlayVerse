package com.example.playverse.presentation.component.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.playverse.domain.model.Game

@Composable
fun GameDetailPreviewGallery(
    game: Game,
    modifier: Modifier = Modifier,
    primaryText: Color = Color(0xFF191C24),
    borderColor: Color = Color(0xFFE2E8F0)
) {
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    // Fallback high quality preview screenshots if game.screenshots is empty
    val previewImages = remember(game.screenshots, game.images, game.thumbnail) {
        val list = game.screenshots.ifEmpty { game.images }
        if (list.isNotEmpty()) {
            list
        } else {
            listOf(
                game.thumbnail.ifEmpty { "https://images.unsplash.com/photo-1542751371-adc38448a05e" },
                "https://images.unsplash.com/photo-1538481199705-c710c4e965fc",
                "https://images.unsplash.com/photo-1511512578047-dfb367046420",
                "https://images.unsplash.com/photo-1550745165-9bc0b252726f"
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hình ảnh & Gameplay",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Text(
                text = "${previewImages.size} ảnh",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8A94A6)
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(previewImages) { index, imageUrl ->
                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { selectedImageUrl = imageUrl }
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Preview Image ${index + 1}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Zoom indicator badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomIn,
                            contentDescription = "Zoom",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }

    // FULLSCREEN LIGHTBOX DIALOG PREVIEW
    selectedImageUrl?.let { imageUrl ->
        Dialog(
            onDismissRequest = { selectedImageUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.92f))
                    .clickable { selectedImageUrl = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Full Preview",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Close Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(20.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { selectedImageUrl = null },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
