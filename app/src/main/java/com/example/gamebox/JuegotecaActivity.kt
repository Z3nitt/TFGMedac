package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import com.example.gamebox.model.CollectionEntry
import com.example.gamebox.model.GameEntry
import com.example.gamebox.viewmodel.LibraryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background


@OptIn(ExperimentalMaterial3Api::class)
class JuegotecaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: LibraryViewModel = viewModel()
            val games by vm.games.collectAsState()
            val totalG by vm.totalGames.collectAsState()
            val totalC by vm.totalCollections.collectAsState()
            val pct    by vm.percentComplete.collectAsState()
            val context = this

            // Flags
            var showMenu   by remember { mutableStateOf(false) }
            var showEdit   by remember { mutableStateOf(false) }
            var showDelete by remember { mutableStateOf(false) }

            // Dropdown state
            var expanded    by remember { mutableStateOf(false) }
            var selectedCol by remember { mutableStateOf<CollectionEntry?>(null) }
            var newName     by remember { mutableStateOf("") }
            var totalTxt    by remember { mutableStateOf("") }
            val collections by vm.collections.collectAsState()

            Scaffold(
                containerColor = Color(0xFFDFFFE0),
                topBar = { TopAppBar(title = { Text("Mi Juegoteca") }) }) { padding ->
                Column(
                    Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    // Contadores
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // --- Juegos: al hacer click abre GamesListActivity ---
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    startActivity(
                                        Intent(this@JuegotecaActivity, GamesListActivity::class.java)
                                    )
                                }
                        ) {
                            Text(text = "$totalG", style = MaterialTheme.typography.headlineMedium)
                            Text(text = "Juegos", style = MaterialTheme.typography.bodySmall)
                        }

                        // --- Colecciones: igual que antes ---
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showMenu = true }
                        ) {
                            Text(text = "$totalC", style = MaterialTheme.typography.headlineMedium)
                            Text(text = "Colecciones", style = MaterialTheme.typography.bodySmall)
                        }
                    }


                    // Gráfico
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
                        Text(
                            text = "${(pct * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Últimos añadidos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    LazyRow(
                        Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(games.sortedByDescending { it.addedAt }.take(10)) { g ->
                            AsyncImage(
                                model = g.imageUrl,
                                contentDescription = g.title,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        Intent(context, GameDetailActivity::class.java).also { intent ->
                                            if (g.gameId.startsWith("steam_")) {
                                                intent.putExtra("item_type", "Steam")
                                                intent.putExtra("steam_appid", g.gameId.removePrefix("steam_").toIntOrNull() ?: -1)
                                                intent.putExtra("steam_name", g.title)
                                            } else {
                                                intent.putExtra("item_type", "Epic")
                                                intent.putExtra("epic_title", g.title)
                                                intent.putExtra("epic_image", g.imageUrl)
                                            }
                                            startActivity(intent)
                                        }
                                    }
                            )
                        }
                    }
                }

                // Menu principal
                if (showMenu) {
                    AlertDialog(
                        onDismissRequest = { showMenu = false },
                        title = { Text("¿Qué quieres hacer?") },
                        text = {
                            Column {
                                TextButton(onClick = {
                                    showMenu = false
                                    startActivity(Intent(context, VerColeccionesActivity::class.java))
                                }) { Text("Ver colecciones") }
                                Divider()
                                TextButton(onClick = { showMenu = false; showEdit = true }) { Text("Editar colección") }
                                Divider()
                                TextButton(onClick = { showMenu = false; showDelete = true }) { Text("Borrar colección") }
                            }
                        },
                        confirmButton = {},
                        dismissButton = {
                            TextButton(onClick = { showMenu = false }) { Text("Cancelar") }
                        }
                    )
                }

                // Editar
                if (showEdit) {
                    GenericCrudDialog(
                        title = "Editar colección",
                        collections = collections,
                        expanded = expanded,
                        selected = selectedCol,
                        newName = newName,
                        totalTxt = totalTxt,
                        showFields = true,
                        onExpandedChange = { expanded = !expanded },
                        onSelect = { selectedCol = it },
                        onNewValues = { (n, t) -> newName = n; totalTxt = t },
                        onConfirm = {
                            selectedCol?.let { vm.updateCollection(it.collectionId, newName, totalTxt.toIntOrNull()) {} }
                        },
                        onDismiss = { showEdit = false }
                    )
                }

                // Borrar
                if (showDelete) {
                    GenericCrudDialog(
                        title = "Borrar colección",
                        collections = collections,
                        expanded = expanded,
                        selected = selectedCol,
                        newName = newName,
                        totalTxt = totalTxt,
                        showFields = false,
                        onExpandedChange = { expanded = !expanded },
                        onSelect = { selectedCol = it },
                        onNewValues = { },
                        onConfirm = {
                            selectedCol?.let { vm.deleteCollection(it.collectionId) {} }
                        },
                        onDismiss = { showDelete = false }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericCrudDialog(
    title: String,
    collections: List<CollectionEntry>,
    expanded: Boolean,
    selected: CollectionEntry?,
    newName: String,
    totalTxt: String,
    showFields: Boolean,
    onExpandedChange: ()->Unit,
    onSelect: (CollectionEntry)->Unit,
    onNewValues: (Pair<String,String>)->Unit,
    onConfirm: ()->Unit,
    onDismiss: ()->Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .clickable(onClick = onExpandedChange)
                        .padding(12.dp)
                ) {
                    Text(
                        selected?.name ?: "Selecciona colección…",
                        Modifier.align(Alignment.CenterStart)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        Modifier.align(Alignment.CenterEnd)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = onDismiss
                ) {
                    collections.forEach { col ->
                        DropdownMenuItem(text = { Text(col.name) }, onClick = {
                            onSelect(col)
                            onExpandedChange()
                        })
                    }
                }
                if (showFields) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { onNewValues(it to totalTxt) },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = totalTxt,
                        onValueChange = { onNewValues(newName to it) },
                        label = { Text("Total") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
