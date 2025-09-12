package com.example.omdb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Movie List", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.Search.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.Completed.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Completed")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.Watchlist.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Watchlist")
        }
    }
}
