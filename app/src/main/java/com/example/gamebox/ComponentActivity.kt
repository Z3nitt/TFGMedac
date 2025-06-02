// VerColeccionesActivity.kt
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
import com.example.gamebox.model.CollectionEntry
import com.example.gamebox.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
class VerColeccionesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: LibraryViewModel = viewModel()
            val collections by vm.collections.collectAsState()

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
                            title = { Text("Tus colecciones", color = Color.Black) },
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
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(collections) { col: CollectionEntry ->
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        Intent(this@VerColeccionesActivity, CollectionDetailActivity::class.java)
                                            .putExtra("colId", col.collectionId)
                                            .putExtra("colName", col.name)
                                            .also { startActivity(it) }
                                    }
                                    .padding(vertical = 4.dp, horizontal = 16.dp),
                                headlineContent = {
                                    Text(
                                        text = col.name,
                                        color = Color.Black
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = "Total: ${col.totalInSaga ?: "?"}",
                                        color = Color.Black
                                    )
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = colorResource(id = R.color.trans),
                                    headlineColor = Color.Black,
                                    supportingColor = Color.Black
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
