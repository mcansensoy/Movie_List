package com.example.omdb.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.omdb.data.WatchlistEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(navController: NavController, vm: WatchlistViewModel = hiltViewModel()) {
    val movies by vm.movies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Watchlist") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { vm.changeSort(WatchlistSortType.ALPHABETICAL) }) {
                    Text("Alphabetical")
                }
                Button(onClick = { vm.changeSort(WatchlistSortType.RATING) }) {
                    Text("IMDB Rating")
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(movies, key = { it.imdbID }) { movie ->
                    WatchlistMovieRow(movie, onDelete = { vm.removeMovie(movie.imdbID) })
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }
        }
    }
}

@Composable
fun WatchlistMovieRow(movie: WatchlistEntity, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(movie.title, style = MaterialTheme.typography.titleMedium)
                    Text("Year: ${movie.year}")
                }

                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete movie"
                    )
                }
            }

            if (!movie.poster.isNullOrBlank() && movie.poster != "N/A") {
                AsyncImage(
                    model = movie.poster,
                    contentDescription = "Poster of ${movie.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Text("No poster available")
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text("IMDB: ${movie.imdbRating ?: "Unknown"}")
                Text("Genre: ${movie.genre ?: "Unknown"}")
                Text("Director: ${movie.director ?: "Unknown"}")
                Text("Plot: ${movie.plot ?: "Unknown"}")
            }
        }
    }
}
