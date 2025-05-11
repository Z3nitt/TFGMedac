package com.example.gamebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gamebox.steam.viewmodel.SearchViewModel

class GameDetailActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Leemos los extras
        val type       = intent.getStringExtra("item_type")
        val steamAppid = intent.getIntExtra("steam_appid", -1)
        val steamName  = intent.getStringExtra("steam_name")
        val epicTitle  = intent.getStringExtra("epic_title")
        val epicImage  = intent.getStringExtra("epic_image")

        setContent {
            // Capturamos Activity
            val activity = this@GameDetailActivity
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Detalle Juego") },
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
                        DetailContent(
                            type = type,
                            steamAppid = steamAppid,
                            steamName = steamName,
                            epicTitle = epicTitle,
                            epicImage = epicImage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    type: String?,
    steamAppid: Int,
    steamName: String?,
    epicTitle: String?,
    epicImage: String?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        val titleText = when(type) {
            SearchViewModel.ResultItem.Steam::class.simpleName -> steamName
            SearchViewModel.ResultItem.Epic::class.simpleName  -> epicTitle
            SearchViewModel.ResultItem.Both::class.simpleName  -> steamName ?: epicTitle
            else -> "—"
        } ?: "—"
        Text(text = titleText, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
        val storeText = when(type) {
            SearchViewModel.ResultItem.Steam::class.simpleName -> "Steam"
            SearchViewModel.ResultItem.Epic::class.simpleName  -> "Epic"
            SearchViewModel.ResultItem.Both::class.simpleName  -> "Steam y Epic"
            else -> "-"
        }
        Text(text = "Tienda: $storeText", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(12.dp))
        when(type) {
            SearchViewModel.ResultItem.Steam::class.simpleName -> {
                AsyncImage(
                    model = "https://cdn.cloudflare.steamstatic.com/steam/apps/$steamAppid/header.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
            SearchViewModel.ResultItem.Epic::class.simpleName -> {
                AsyncImage(
                    model = epicImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
            SearchViewModel.ResultItem.Both::class.simpleName -> {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = "https://cdn.cloudflare.steamstatic.com/steam/apps/$steamAppid/header.jpg",
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp)
                    )
                    AsyncImage(
                        model = epicImage,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp)
                    )
                }
            }
        }
    }
}