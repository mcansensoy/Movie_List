package com.example.omdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.example.omdb.ui.MovieSearchScreen
import com.example.omdb.ui.theme.OmdbTheme
//import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.omdb.ui.*

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
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Menu.route
                    ) {
                        composable(NavRoutes.Menu.route) {
                            MainMenuScreen(navController)
                        }
                        composable(NavRoutes.Search.route) {
                            MovieSearchScreenWithAppBar(navController)
                        }
                        composable(NavRoutes.Completed.route) {
                            CompletedScreen(navController)
                        }
                        composable(NavRoutes.Watchlist.route) {
                            PlaceholderScreen("Watchlist (yakında)")
                        }
                    }
                }
            }
        }
    }
}
