package com.example.omdb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.omdb.model.MovieItem

@Composable
fun MovieSearchScreen(vm: MovieViewModel = hiltViewModel()) {
    // Local UI states
    var query by remember { mutableStateOf("") }
    var page by remember { mutableStateOf(1) }

    // ViewModel state'lerini Compose'a bağla
    val movies by vm.movies.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("OMDb Film Arama", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Film başlığını girin") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                page = 1 // yeni arama -> page 1
                vm.search(query, page)
            }) {
                Text("Find")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        // Liste
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(movies) { movie: MovieItem ->
                MovieRow(movie)
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }

            // Son olarak "Load more" butonu göstermek istersen:
            item {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {
                        page += 1
                        vm.search(query, page) // sayfa arttır -> yeni sonuçları getirir (OMDb sayfa başına 10)
                    }) {
                        Text("Load more (page $page)")
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MovieRow(movie: MovieItem) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(text = movie.title, fontWeight = FontWeight.Bold)
        Text(text = "Year: ${movie.year}")

        Spacer(Modifier.height(8.dp))

        // Poster göster (Coil AsyncImage)
        if (movie.poster != "N/A") {
            AsyncImage(
                model = movie.poster,
                contentDescription = "Poster of ${movie.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else {
            Text(text = "No poster available")
        }

        Spacer(Modifier.height(8.dp))
    }
}
