// VerColeccionesActivity.kt
package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

            Scaffold(
                topBar = { TopAppBar(title = { Text("Tus colecciones") }) }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(collections) { col: CollectionEntry ->
                        // Cada fila sólo de ancho completo, con padding y clickable
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Navegar a detalle de colección
                                    Intent(this@VerColeccionesActivity, CollectionDetailActivity::class.java)
                                        .putExtra("colId", col.collectionId)
                                        .putExtra("colName", col.name)
                                        .also { startActivity(it) }
                                }
                                .padding(vertical = 8.dp),
                            headlineContent = { Text(col.name) },
                            supportingContent = { Text("Total: ${col.totalInSaga ?: "?"}") }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
