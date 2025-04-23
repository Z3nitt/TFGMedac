@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gamebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamebox.steam.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SearchScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val query = viewModel.query
    val suggestions = viewModel.suggestions
    val exists = viewModel.exists
    val headerUrl = viewModel.headerUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                viewModel.onQueryChange(it)
                expanded = true
            },
            label = { Text("Nombre de juego") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Lista de sugerencias debajo del campo
        if (expanded && suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
            ) {
                items(suggestions) { app ->
                    Text(
                        text = app.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onQueryChange(app.name)
                                viewModel.search()
                                expanded = false
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (exists) {
            true -> {
                Text(
                    text = "✅ ¡Juego encontrado: $query!",
                    style = MaterialTheme.typography.titleMedium
                )
                headerUrl?.let { url ->
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }
            false -> Text(
                text = "❌ No existe un juego llamado '$query'.",
                style = MaterialTheme.typography.bodyMedium
            )
            null -> { /* no mostrar nada */ }
        }
    }
}