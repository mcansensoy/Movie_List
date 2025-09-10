package com.example.omdb.ui

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import com.example.omdb.model.MovieDetail
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
    val movieDetails by vm.movieDetails.collectAsState()

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
            items(movies) { movie ->
                Column {
                    MovieRow(movie, onClick = { vm.toggleMovieDetail(movie.imdbID) })

                    // Eğer bu film için detay yüklenmişse, göster
                    movieDetails[movie.imdbID]?.let { detail ->
                        MovieDetailCard(detail)
                    }

                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }

            // Son olarak "Load more" butonu göstermek istersen:
            item {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {
                            if(page > 1){
                                page -= 1
                                vm.search(query, page)
                            }
                        },
                        modifier = Modifier
                            .width(140.dp)
                            .defaultMinSize(minHeight = 30.dp)
                    ) {
                        Text("Previous Page")
                    }

                    Spacer(Modifier.width(12.dp))
                    Box(modifier = Modifier.width(20.dp)) {
                        Text("$page",
                            modifier = Modifier.align(Alignment.Center))
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            page += 1
                            vm.search(query, page) // sayfa arttır -> yeni sonuçları getirir (OMDb sayfa başına 10)
                        },
                        modifier = Modifier.width(140.dp).defaultMinSize(minHeight = 30.dp)
                    ) {
                        Text("Next Page")
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MovieRow(movie: MovieItem, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() } // Satıra tıklanabilirlik
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

@Composable
fun MovieDetailCard(detail: MovieDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("IMDB Rating: ${detail.imdbRating ?: "Unknown"}")
            Text("Genre: ${detail.genre ?: "Unknown"}")
            Text("Director: ${detail.director ?: "Unknown"}")
            Text("Plot: ${detail.plot ?: "Unknown"}")

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(160.dp)
                        .defaultMinSize(minHeight = 40.dp)
                ) {
                    Text("Add to completed")
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(160.dp)
                        .defaultMinSize(minHeight = 40.dp)
                ) {
                    Text("Add to watchlist")
                }
            }
        }
    }
}
