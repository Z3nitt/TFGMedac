package com.example.gamebox.repository

import com.example.gamebox.model.CollectionEntry
import com.example.gamebox.model.GameEntry
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object LibraryRepository {
    private val auth    = FirebaseAuth.getInstance()
    private val db      = FirebaseFirestore.getInstance()
    private val uid: String
        get() = auth.currentUser?.uid
            ?: throw IllegalStateException("Usuario no autenticado")

    private val gamesCol = db.collection("users").document(uid).collection("library_games")
    private val colsCol  = db.collection("users").document(uid).collection("library_collections")

    /** Observa en tiempo real los juegos añadidos. */
    fun observeGames(): Flow<List<GameEntry>> = callbackFlow {
        val sub = gamesCol.addSnapshotListener { snap, err ->
            if (err != null) close(err)
            else {
                val list = snap!!.documents.mapNotNull { doc ->
                    val entry = doc.toObject(GameEntry::class.java)?.copy(docId = doc.id)
                    entry
                }
                trySend(list)
            }
        }
        awaitClose { sub.remove() }
    }

    /** Observa en tiempo real las colecciones. */
    fun observeCollections(): Flow<List<CollectionEntry>> = callbackFlow {
        val sub = colsCol.addSnapshotListener { snap, err ->
            if (err != null) close(err)
            else {
                val list = snap!!.documents.mapNotNull { it.toObject(CollectionEntry::class.java) }
                trySend(list)
            }
        }
        awaitClose { sub.remove() }
    }

    /** Crea una nueva colección y devuelve su ID. */
    suspend fun addCollection(name: String, totalInSaga: Int? = null): String {
        val doc = colsCol.document()
        val entry = CollectionEntry(
            collectionId = doc.id,
            name         = name,
            totalInSaga  = totalInSaga,
            createdAt    = Timestamp.now()
        )
        doc.set(entry).await()
        return doc.id
    }

    /**
     * Añade o actualiza un juego en la juegoteca usando un ID único
     * compuesto por collectionId y gameId para evitar sobrescrituras.
     */
    suspend fun addGame(entry: GameEntry) {
        val withTimestamp = entry.copy(addedAt = Timestamp.now())
        gamesCol
            .add(withTimestamp)
            .await()
    }

    suspend fun updateCollection(colId: String, name: String, totalInSaga: Int?) {
        colsCol.document(colId)
            .update(mapOf("name" to name, "totalInSaga" to totalInSaga))
            .await()
    }

    suspend fun deleteCollection(colId: String) {
        // Borra la colección
        colsCol.document(colId).delete().await()
        // Opcional: también borra todos los juegos de esa colección
        val batch = db.batch()
        val gamesSnap = gamesCol.whereEqualTo("collectionId", colId).get().await()
        gamesSnap.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
    suspend fun deleteGame(docId: String) {
        gamesCol.document(docId).delete().await()
    }

}
