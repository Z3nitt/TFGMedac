package com.example.gamebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamebox.epic.model.EpicAppInfo
import com.example.gamebox.epic.repository.EpicRepository
import com.example.gamebox.steam.model.AppInfo
import com.example.gamebox.steam.viewmodel.SearchViewModel
import com.example.gamebox.viewmodel.LibraryViewModel
import com.example.gamebox.model.CollectionEntry
import com.example.gamebox.model.GameEntry

class GameDetailActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type       = intent.getStringExtra("item_type")
        val steamAppid = intent.getIntExtra("steam_appid", -1)
        val steamName  = intent.getStringExtra("steam_name")
        val epicTitle  = intent.getStringExtra("epic_title")
        val epicImage  = intent.getStringExtra("epic_image")

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.fondo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                MaterialTheme {
                    Scaffold(
                        containerColor = Color.Transparent,
                        topBar = {
                            TopAppBar(
                                title = { Text("Detalle Juego", color = Color.Black) },
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Atrás",
                                            tint = Color.Black
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
                            )
                        }
                    ) { innerPadding ->
                        Box(Modifier.padding(innerPadding)) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    type: String?,
    steamAppid: Int,
    steamName: String?,
    epicTitle: String?,
    epicImage: String?
) {
    // Repositorio para re-fetch de Epic
    val epicRepo = remember { EpicRepository() }
    var detailEpicPrice by remember { mutableStateOf<String?>(null) }
    var detailEpicDescription by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(epicTitle) {
        epicTitle?.takeIf { it.isNotBlank() }?.let { title ->
            val result = try {
                epicRepo.searchApps(title, 0, 1).firstOrNull()
            } catch (_: Exception) {
                null
            }
            detailEpicPrice = result?.price ?: "—"
            detailEpicDescription = result?.description
        }
    }

    // ViewModel de búsqueda para Steam y cabeceras
    val searchVm: SearchViewModel = viewModel()
    LaunchedEffect(type, steamAppid, epicTitle) {
        when (type) {
            SearchViewModel.ResultItem.Steam::class.simpleName -> {
                searchVm.selectItem(
                    SearchViewModel.ResultItem.Steam(
                        AppInfo(appid = steamAppid, name = steamName.orEmpty())
                    )
                )
            }
            SearchViewModel.ResultItem.Epic::class.simpleName -> {
                searchVm.selectItem(
                    SearchViewModel.ResultItem.Epic(
                        EpicAppInfo(
                            id = epicTitle.orEmpty(),
                            title = epicTitle.orEmpty(),
                            imageUrl = epicImage.orEmpty(),
                            price = null,
                            description = null
                        )
                    )
                )
            }
            SearchViewModel.ResultItem.Both::class.simpleName -> {
                searchVm.selectItem(
                    SearchViewModel.ResultItem.Both(
                        steam = AppInfo(appid = steamAppid, name = steamName.orEmpty()),
                        epic = EpicAppInfo(
                            id = epicTitle.orEmpty(),
                            title = epicTitle.orEmpty(),
                            imageUrl = epicImage.orEmpty(),
                            price = null,
                            description = null
                        )
                    )
                )
            }
            else -> {}
        }
    }

    // Precios y cabeceras
    val steamPrice by remember { derivedStateOf { searchVm.steamPrice } }
    val headerUrls by remember { derivedStateOf { searchVm.headerUrls } }

    // Descripción
    val steamDescription by remember { derivedStateOf { searchVm.steamShortDescription } }

    // ViewModel de librería
    val libVm: LibraryViewModel = viewModel()
    val collections by libVm.collections.collectAsState(initial = emptyList())

    // Estados de diálogo
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCol by remember { mutableStateOf<CollectionEntry?>(null) }
    var newName by remember { mutableStateOf("") }
    var totalTxt by remember { mutableStateOf("") }

    // Datos del juego
    val titleText = when (type) {
        SearchViewModel.ResultItem.Steam::class.simpleName -> steamName
        SearchViewModel.ResultItem.Epic::class.simpleName  -> epicTitle
        SearchViewModel.ResultItem.Both::class.simpleName  -> steamName ?: epicTitle
        else -> "—"
    } ?: "—"

    Column(Modifier.padding(16.dp)) {
        // Título
        Text(text = titleText, style = MaterialTheme.typography.titleLarge, color = Color.Black)
        Spacer(Modifier.height(8.dp))

        // Precios
        when (type) {
            SearchViewModel.ResultItem.Steam::class.simpleName -> {
                Text(
                    text = "Precio Steam: ${steamPrice ?: "—"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
            SearchViewModel.ResultItem.Epic::class.simpleName -> {
                Text(
                    text = "Precio Epic: ${detailEpicPrice ?: "—"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
            SearchViewModel.ResultItem.Both::class.simpleName -> {
                Text(
                    text = "Precio Steam: ${steamPrice ?: "—"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Precio Epic: ${detailEpicPrice ?: "—"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
            else -> { /* nada */ }
        }
        Spacer(Modifier.height(12.dp))

        // Tienda
        Text(
            "Tienda: ${
                when (type) {
                    SearchViewModel.ResultItem.Steam::class.simpleName -> "Steam"
                    SearchViewModel.ResultItem.Epic::class.simpleName  -> "Epic"
                    SearchViewModel.ResultItem.Both::class.simpleName  -> "Steam y Epic"
                    else -> "-"
                }
            }",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Spacer(Modifier.height(12.dp))

        // Imágenes
        if (headerUrls.size <= 1) {
            AsyncImage(
                model = headerUrls.firstOrNull().orEmpty(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                headerUrls.forEach { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        val descriptionText = when (type) {
            SearchViewModel.ResultItem.Steam::class.simpleName -> steamDescription
            SearchViewModel.ResultItem.Epic::class.simpleName  -> detailEpicDescription
            else -> null
        }
        descriptionText?.let {
            Spacer(Modifier.height(12.dp))
            Text(text = it, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.principal),
                contentColor = Color.Black
            )
        ) {
            Text("Añadir a Juegoteca")
        }
    }

    // Diálogo "Añadir"
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("¿A qué colección?") },
            text = {
                Column {
                    TextButton(onClick = {
                        selectedCol = null; newName = ""; totalTxt = ""
                    }) { Text("+ Crear nueva colección") }
                    Divider()
                    collections.forEach { col ->
                        TextButton(
                            onClick = { selectedCol = col },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(col.name) }
                        Divider()
                    }
                    if (selectedCol == null) {
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = totalTxt,
                            onValueChange = { totalTxt = it },
                            label = { Text("Total") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedCol != null) {
                        libVm.addGameToCollection(
                            GameEntry(
                                gameId       = "steam_$steamAppid",
                                title        = titleText,
                                imageUrl     = headerUrls.firstOrNull().orEmpty(),
                                collectionId = selectedCol!!.collectionId
                            )
                        )
                    } else {
                        libVm.createCollection(newName, totalTxt.toIntOrNull()) { colId ->
                            libVm.addGameToCollection(
                                GameEntry(
                                    gameId       = "steam_$steamAppid",
                                    title        = titleText,
                                    imageUrl     = headerUrls.firstOrNull().orEmpty(),
                                    collectionId = colId
                                )
                            )
                        }
                    }
                    showAddDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
            },
            containerColor = colorResource(id = R.color.principal)
        )
    }
}
