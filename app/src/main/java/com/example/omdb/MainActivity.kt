package com.example.omdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState // StateFlow -> Compose state dönüşümü için
import androidx.compose.runtime.livedata.observeAsState
import com.example.omdb.ui.MovieUiState
import com.example.omdb.ui.MovieViewModel

// Compose ile çok küçük bir arama ekranı:
// - TextField: film adı
// - Button: Find
// - Altında: Title ve imdbRating (başarılıysa)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
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

@Composable
fun MovieSearchScreen(vm: MovieViewModel = viewModel()) {
    // TextField için Compose tarafında state
    var query by remember { mutableStateOf(TextFieldValue("")) }

    // ViewModel'in akışını (StateFlow) Compose state'e dönüştürüyoruz.
    val uiState by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("OMDb Film Arama (Title + IMDb Score)",
            style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        // ARAMA BAR
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Film başlığı (örn. Inception)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // FIND BUTONU
        Button(
            onClick = { vm.search(query.text) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Find")
        }

        Spacer(Modifier.height(24.dp))

        // UI DURUMUNA GÖRE GÖSTER
        when (uiState) {
            is MovieUiState.Idle -> {
                Text("Bir film adı girip 'Find' butonuna bas.")
            }
            is MovieUiState.Loading -> {
                CircularProgressIndicator()
            }
            is MovieUiState.Error -> {
                val msg = (uiState as MovieUiState.Error).message
                Text("Hata: $msg", color = MaterialTheme.colorScheme.error)
            }
            is MovieUiState.Success -> {
                val data = (uiState as MovieUiState.Success).data
                // Yalnızca "Title" ve "imdbRating" gösteriyoruz (istek buydu).
                // Null gelebilme ihtimaline karşı "?" ile güvenli gösterim.
                Text("Title: ${data.title ?: "-"}",
                    style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("IMDb Rating: ${data.imdbRating ?: "-"}",
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}