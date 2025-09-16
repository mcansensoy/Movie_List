package com.example.omdb.ui

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.omdb.data.MovieEntity
import coil.compose.AsyncImage

@Composable
fun CompletedScreen(navController: NavController, vm: CompletedViewModel = hiltViewModel()) {
    val movies by vm.movies.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { vm.changeSort(SortType.ALPHABETICAL) }) {
                Text("Alphabetical")
            }
            Button(onClick = { vm.changeSort(SortType.RATING) }) {
                Text("IMDB Rating")
            }
            Button(onClick = { vm.changeSort(SortType.USER_RATING) }) { // ⭐ yeni
                Text("User Rating")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(movies, key = { it.imdbID }) { movie ->
                CompletedMovieRow(movie, onDelete = { vm.removeMovie(movie.imdbID) })
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}

@Composable
fun CompletedMovieRow(movie: MovieEntity, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(8.dp)
    ) {

        Text(movie.title, style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Year: ${movie.year}")
            Spacer(Modifier.weight(1f))

            if (movie.userRating != null) {
                Text("User: ${movie.userRating}/10")
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

            Spacer(Modifier.height(8.dp))

            IconButton(onClick = { onDelete() }, modifier = Modifier.align(Alignment.End)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete movie"
                )
            }
        }
    }
}
