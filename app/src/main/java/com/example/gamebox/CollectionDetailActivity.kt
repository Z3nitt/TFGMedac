// CollectionDetailActivity.kt
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gamebox.model.GameEntry
import com.example.gamebox.steam.viewmodel.SearchViewModel
import com.example.gamebox.viewmodel.LibraryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class CollectionDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val colId   = intent.getStringExtra("colId") ?: return
        val colName = intent.getStringExtra("colName") ?: "Colección"

        setContent {
            val vm: LibraryViewModel = viewModel()
            val games by vm.games.collectAsState()
            val filtered: List<GameEntry> = games.filter { it.collectionId == colId }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(colName) },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                            }
                        }
                    )
                }
            ) { padding ->
                LazyColumn(
                    Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(filtered) { game ->
                        ListItem(
                            headlineContent = { Text(game.title) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Al pulsar, abrimos GameDetailActivity
                                    Intent(this@CollectionDetailActivity, GameDetailActivity::class.java).apply {
                                        if (game.gameId.startsWith("steam_")) {
                                            putExtra("item_type", SearchViewModel.ResultItem.Steam::class.simpleName)
                                            putExtra("steam_appid", game.gameId.removePrefix("steam_").toIntOrNull() ?: -1)
                                            putExtra("steam_name", game.title)
                                        } else {
                                            putExtra("item_type", SearchViewModel.ResultItem.Epic::class.simpleName)
                                            putExtra("epic_title", game.title)
                                            putExtra("epic_image", game.imageUrl)
                                        }
                                    }.also { startActivity(it) }
                                }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
