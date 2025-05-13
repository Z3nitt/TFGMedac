package com.example.gamebox

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamebox.steam.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Capturamos la Activity
            val activity = this@MainActivity
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Buscar Juego") },
                            navigationIcon = {
                                IconButton(onClick = { activity.finish() }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_arrow_back),
                                        contentDescription = "Atrás"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SearchScreen(
                            viewModel = viewModel(),
                            onGameSelected = { selectedItem ->
                                val intent = Intent(activity, GameDetailActivity::class.java).apply {
                                    putExtra("item_type", selectedItem::class.simpleName)
                                    // dentro de onGameSelected = { selectedItem -> … }
                                    when (selectedItem) {
                                        is SearchViewModel.ResultItem.Steam -> {
                                            putExtra("steam_appid", selectedItem.info.appid)
                                            putExtra("steam_name",  selectedItem.info.name)
                                        }
                                        is SearchViewModel.ResultItem.Epic -> {
                                            putExtra("epic_id",    selectedItem.info.id)
                                            putExtra("epic_title", selectedItem.info.title)
                                            putExtra("epic_image", selectedItem.info.imageUrl)
                                            // ← Usa selectedItem.info.price aquí:
                                            putExtra("epic_price", selectedItem.info.price)
                                        }
                                        is SearchViewModel.ResultItem.Both -> {
                                            putExtra("steam_appid", selectedItem.steam.appid)
                                            putExtra("steam_name",  selectedItem.steam.name)
                                            putExtra("epic_id",     selectedItem.epic.id)
                                            putExtra("epic_title",  selectedItem.epic.title)
                                            putExtra("epic_image",  selectedItem.epic.imageUrl)
                                            // ← Y aquí:
                                            putExtra("epic_price",  selectedItem.epic.price)
                                        }
                                    }



                                }
                                activity.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onGameSelected: (SearchViewModel.ResultItem) -> Unit
) {
    val query by remember { derivedStateOf { viewModel.query } }
    val suggestions by remember { derivedStateOf { viewModel.suggestions } }
    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier
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
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
        )

        if (expanded && suggestions.isNotEmpty()) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(suggestions) { item ->
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
                                onGameSelected(item)
                                expanded = false
                            }
                            .padding(8.dp)
                    )
                    Divider()
                }
            }
        }
    }
}