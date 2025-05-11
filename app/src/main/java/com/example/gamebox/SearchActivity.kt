package com.example.gamebox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamebox.steam.viewmodel.SearchViewModel

class SearchActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Capturamos la Activity para poder llamar finish()
            val activity = this@SearchActivity
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
                        // Aquí llamas a tu SearchScreen con la callback
                        SearchScreen(
                            viewModel = viewModel(),
                            onGameSelected = { selectedItem ->
                                val intent = Intent(activity, GameDetailActivity::class.java).apply {
                                    putExtra("item_type", selectedItem::class.simpleName)
                                    when (selectedItem) {
                                        is SearchViewModel.ResultItem.Steam -> {
                                            putExtra("steam_appid", selectedItem.info.appid)
                                            putExtra("steam_name", selectedItem.info.name)
                                        }
                                        is SearchViewModel.ResultItem.Epic -> {
                                            putExtra("epic_id", selectedItem.info.id)
                                            putExtra("epic_title", selectedItem.info.title)
                                            putExtra("epic_image", selectedItem.info.imageUrl)
                                        }
                                        is SearchViewModel.ResultItem.Both -> {
                                            putExtra("steam_appid", selectedItem.steam.appid)
                                            putExtra("steam_name", selectedItem.steam.name)
                                            putExtra("epic_id", selectedItem.epic.id)
                                            putExtra("epic_title", selectedItem.epic.title)
                                            putExtra("epic_image", selectedItem.epic.imageUrl)
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
