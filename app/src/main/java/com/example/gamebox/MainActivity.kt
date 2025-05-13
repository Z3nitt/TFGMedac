@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gamebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
    val query by remember { derivedStateOf { viewModel.query } }
    val suggestions by remember { derivedStateOf { viewModel.suggestions } }
    val selected by remember { derivedStateOf { viewModel.selectedItem } }
    val headerUrls by remember { derivedStateOf { viewModel.headerUrls } }

    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                viewModel.onQueryChange(it)
                expanded = true
            },
            label = { Text("Nombre de juego") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = { expanded =! expanded }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
        )

        if (expanded && suggestions.isNotEmpty()) {
            LazyColumn(Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                items(suggestions) { item ->
                    // sólo el nombre, sin prefijo
                    val name = when (item) {
                        is SearchViewModel.ResultItem.Steam -> item.info.name
                        is SearchViewModel.ResultItem.Epic  -> item.info.title
                        is SearchViewModel.ResultItem.Both  -> item.steam.name
                    }
                    Text(
                        text = name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectItem(item)
                                expanded = false
                            }
                            .padding(8.dp)
                    )
                    Divider()
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        selected?.let { sel ->
            // Título
            val titleText = when (sel) {
                is SearchViewModel.ResultItem.Steam -> sel.info.name
                is SearchViewModel.ResultItem.Epic  -> sel.info.title
                is SearchViewModel.ResultItem.Both  -> sel.steam.name
            }
            Text(text = titleText, style = MaterialTheme.typography.titleMedium)

            // 2) Tienda(s)
            val stores = when (sel) {
                is SearchViewModel.ResultItem.Steam -> "Steam"
                is SearchViewModel.ResultItem.Epic  -> "Epic"
                is SearchViewModel.ResultItem.Both  -> "Steam y Epic"
            }
            Text(text = "Tienda: $stores", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(12.dp))

            // Imágenes
            Spacer(Modifier.height(12.dp))
            when (sel) {
                is SearchViewModel.ResultItem.Both -> {
                    // dos side-by-side
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        headerUrls.forEach { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier.weight(1f).height(180.dp)
                            )
                        }
                    }
                }
                else -> {
                    headerUrls.firstOrNull()?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(180.dp)
                        )
                    }
                }
            }
        }
    }
}

