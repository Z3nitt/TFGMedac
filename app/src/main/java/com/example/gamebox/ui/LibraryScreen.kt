// LibraryScreen.kt
package com.example.gamebox.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gamebox.R
import com.example.gamebox.viewmodel.LibraryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LibraryScreen(vm: LibraryViewModel = viewModel()) {
    val games       by vm.games.collectAsState()
    val collections by vm.collections.collectAsState()

    // Para depurar si llegan los datos
    LaunchedEffect(games, collections) {
        Log.d("LibraryScreen", "Games=${games.size}, Cols=${collections.size}")
    }

    // Cálculo del porcentaje “al vuelo”
    val pct by remember(games, collections) {
        derivedStateOf {
            if (collections.isEmpty()) 0f else {
                val done = collections.count { col ->
                    val have = games.count { it.collectionId == col.collectionId }
                    col.totalInSaga?.let { have >= it } ?: (have > 0)
                }
                done.toFloat() / collections.size
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondoalpha),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Contadores
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${games.size}", style = MaterialTheme.typography.headlineMedium)
                    Text(text = "Juegos", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${collections.size}", style = MaterialTheme.typography.headlineMedium)
                    Text(text = "Colecciones", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Gráfico de porcentaje
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = pct,
                    strokeWidth = 12.dp,
                    modifier = Modifier.size(100.dp)
                )
                Text(text = "${(pct * 100).toInt()}%", style = MaterialTheme.typography.titleLarge)
            }

            // Aquí podrías seguir añadiendo el resto de tu UI: últimos añadidos, menús, etc.
        }
    }
}
