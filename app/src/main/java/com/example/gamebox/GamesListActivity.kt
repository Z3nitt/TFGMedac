// GamesListActivity.kt
package com.example.gamebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gamebox.model.GameEntry
import com.example.gamebox.viewmodel.LibraryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class GamesListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: LibraryViewModel = viewModel()
            val games by vm.games.collectAsState()

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
                            title = { Text("Todos tus juegos") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Atrás"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    }
                ) { padding ->
                    LazyColumn(
                        Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(games) { game: GameEntry ->
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* … */ },
                                headlineContent = { Text(game.title) },
                                leadingContent = {
                                    AsyncImage(
                                        model = game.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(56.dp)
                                    )
                                },
                                trailingContent = {
                                    IconButton(onClick = { vm.deleteGame(game.docId) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Borrar")
                                    }
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                )
                            )
                            Divider(color = Color.White.copy(alpha = 0.2f))
                        }
                    }
                }
            }
        }
    }
}
