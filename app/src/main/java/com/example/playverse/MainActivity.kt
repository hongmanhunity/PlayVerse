package com.example.playverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import com.example.playverse.data.api.RetrofitInstance
import com.example.playverse.data.repository.GameRepositoryImpl
import com.example.playverse.presentation.navigation.AppNavigation
import com.example.playverse.presentation.viewmodel.GameViewModel
import com.example.playverse.presentation.viewmodel.GameViewModelFactory
import com.example.playverse.ui.theme.PlayVerseBackground
import com.example.playverse.ui.theme.PlayVerseTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(GameRepositoryImpl(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlayVerseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PlayVerseBackground
                ) {
                    AppNavigation(gameViewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlayVerseTheme {
        Greeting("Android")
    }
}