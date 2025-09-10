package com.example.omdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.example.omdb.ui.MovieSearchScreen
import com.example.omdb.ui.theme.OmdbTheme

// @AndroidEntryPoint: Hilt injection'ı Activity seviyesinde açar.
// Bu sayede Composable içinde `hiltViewModel()` kullanabiliriz.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmdbTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieSearchScreen()
                }
            }
        }
    }
}
