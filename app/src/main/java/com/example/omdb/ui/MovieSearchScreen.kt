package com.example.omdb.ui

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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically

import androidx.compose.animation.core.tween

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun MovieSearchScreen(vm: MovieViewModel = hiltViewModel(), modifier: Modifier) {
    // Local UI states
    val query by vm.query.collectAsState()
    val page by vm.page.collectAsState()

    // ViewModel state'lerini Compose'a bağla
    val movies by vm.movies.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val movieDetails by vm.movieDetails.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val yearRange by vm.yearRange.collectAsState()
    val ratingRange by vm.ratingRange.collectAsState()

    var showFilters by remember { mutableStateOf(false) }
    var startYear by remember { mutableStateOf("") }
    var endYear by remember { mutableStateOf("") }
    var minRating by remember { mutableStateOf("") }
    var maxRating by remember { mutableStateOf("") }
    var filterError by remember { mutableStateOf<String?>(null) }



    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {

        Spacer(Modifier.height(72.dp))
        //Text("Search for movies", style = MaterialTheme.typography.titleMedium)

        //Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { vm.updateQuery(it) },
            label = { Text("Type the movie name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { showFilters = !showFilters }, modifier = Modifier.width(140.dp).defaultMinSize(minHeight = 32.dp)) {
                Text(if (showFilters) "Hide Filters" else "Show Filters")
            }
            Spacer(Modifier.width(24.dp))

            Button(onClick = {vm.goToPage(1)}, modifier = Modifier.width(140.dp).defaultMinSize(minHeight = 32.dp)) {
                Text("Find")
            }
        }


        Spacer(Modifier.height(12.dp))

        AnimatedVisibility(
            visible = showFilters,
            enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
        ) {
            Column(Modifier.padding(8.dp)) {
                // Year range
                Row {
                    OutlinedTextField(
                        value = startYear,
                        onValueChange = { startYear = it },
                        label = { Text("Start Year") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = endYear,
                        onValueChange = { endYear = it },
                        label = { Text("End Year") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Rating range
                Row {
                    OutlinedTextField(
                        value = minRating,
                        onValueChange = { minRating = it },
                        label = { Text("Min Rating") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = maxRating,
                        onValueChange = { maxRating = it },
                        label = { Text("Max Rating") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {
                            startYear = ""
                            endYear = ""
                            minRating = ""
                            maxRating = ""
                            vm.updateYearRange(1900, 2100)
                            vm.updateRatingRange(0f, 10f)
                        },
                        modifier = Modifier.width(140.dp).defaultMinSize(minHeight = 32.dp)
                    ) {
                        Text("Clear Filters")
                    }

                    Spacer(Modifier.width(24.dp))

                    Button(
                        onClick = {
                            val success = vm.applyFiltersWithValues(startYear, endYear, minRating, maxRating)
                            if (!success) {
                                filterError = "Lütfen sadece sayı giriniz."
                            } else {
                                filterError = null
                            }
                        },
                        modifier = Modifier.width(140.dp).defaultMinSize(minHeight = 32.dp)
                    ) {
                        Text("Apply Filters")
                    }
                }

                filterError?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
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
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
            items(movies) { movie ->
                Column {
                    MovieRow(movie, onClick = { vm.toggleMovieDetail(movie.imdbID) })

                    // Eğer bu film için detay yüklenmişse, göster
                    AnimatedVisibility(
                        visible = movieDetails[movie.imdbID] != null,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                    ) {
                        movieDetails[movie.imdbID]?.let { detail ->
                            MovieDetailCard(detail)
                        }
                    }

                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }

            // Son olarak "Load more" butonu göstermek istersen:
            if (movies.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Previous Page
                        Button(
                            onClick = {
                                if (page > 1) {
                                    vm.goToPage(page - 1)
                                    coroutineScope.launch {
                                        listState.scrollToItem(0) // en üste kay
                                    }
                                }
                            },
                            enabled = page > 1, // 1. sayfadaysa disable
                            modifier = Modifier.width(140.dp).defaultMinSize(minHeight = 30.dp)
                        ) {
                            Text("Previous Page")
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            "$page",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(Modifier.width(12.dp))

                        // Next Page
                        Button(
                            onClick = {
                                vm.goToPage(page + 1)
                                coroutineScope.launch {
                                    listState.scrollToItem(0) // en üste kay
                                }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreenWithAppBar(navController: NavController, vm: MovieViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->
        MovieSearchScreen(vm = vm, modifier = Modifier.padding(padding))
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
