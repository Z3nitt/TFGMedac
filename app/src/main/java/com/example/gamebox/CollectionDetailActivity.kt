// CollectionDetailActivity.kt
package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamebox.model.GameEntry
import com.example.gamebox.steam.viewmodel.SearchViewModel
import com.example.gamebox.viewmodel.LibraryViewModel

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

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.fondo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = { Text(colName, color = Color.Black) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Atrás",
                                        tint = Color.Black
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
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
                                    .padding(vertical = 4.dp, horizontal = 16.dp),
                                leadingContent = {
                                    AsyncImage(
                                        model = game.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                },
                                headlineContent = {
                                    Text(text = game.title, color = Color.Black)
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = colorResource(id = R.color.trans),
                                    headlineColor = Color.Black
                                )
                            )
                            Divider(color = Color.Black.copy(alpha = 0.2f))
                        }
                    }
                }
            }
        }
    }
}
