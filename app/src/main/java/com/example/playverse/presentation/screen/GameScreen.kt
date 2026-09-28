package com.example.playverse.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playverse.presentation.navigation.AppNavigation
import com.example.playverse.presentation.viewmodel.GameViewModel

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel
) {
    AppNavigation(
        modifier = modifier,
        gameViewModel = viewModel
    )
}
