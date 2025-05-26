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
    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    private val uid: String
        get() = auth.currentUser?.uid
            ?: throw IllegalStateException("Usuario no autenticado")

    private val gamesCol = db
        .collection("users")
        .document(uid)
        .collection("library_games")

    private val colsCol = db
        .collection("users")
        .document(uid)
        .collection("library_collections")

    /** Observa en tiempo real los juegos añadidos. */
    fun observeGames(): Flow<List<GameEntry>> = callbackFlow {
        val sub = gamesCol.addSnapshotListener { snap, err ->
            if (err != null) close(err)
            else {
                val list = snap!!.documents.mapNotNull { doc ->
                    doc.toObject(GameEntry::class.java)
                        ?.copy(docId = doc.id)
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
                val list = snap!!.documents.mapNotNull { doc ->
                    doc.toObject(CollectionEntry::class.java)
                        // aquí añadimos el id para que la igualdad de objetos cambie
                        ?.copy(collectionId = doc.id)
                }
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

    /** Añade un juego a la juegoteca. */
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
        // Borra también los juegos de esa colección
        val batch   = db.batch()
        val snap    = gamesCol.whereEqualTo("collectionId", colId).get().await()
        snap.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }

    suspend fun deleteGame(docId: String) {
        gamesCol.document(docId).delete().await()
    }

    /**
     * Marca o desmarca un logro para un juego concreto.
     * Se guarda en subcolección "achievements" del documento del juego.
     */
    suspend fun setAchievementState(
        gameId: String,
        achievementName: String,
        achieved: Boolean
    ) {
        gamesCol
            .document(gameId)
            .collection("achievements")
            .document(achievementName)
            .set(mapOf(
                "achieved" to achieved,
                "updatedAt" to Timestamp.now()
            ))
            .await()
    }

    /**
     * Observa en tiempo real todos los estados de logros de un juego.
     * Devuelve un Flow<Map<nombreLogro, estáLogrado>>.
     */
    fun observeAchievementStates(gameId: String): Flow<Map<String, Boolean>> = callbackFlow {
        val sub = gamesCol
            .document(gameId)
            .collection("achievements")
            .addSnapshotListener { snap, err ->
                if (err != null) close(err)
                else {
                    val map = snap!!.documents
                        .mapNotNull { doc ->
                            val name = doc.id
                            val done = doc.getBoolean("achieved") ?: false
                            name to done
                        }
                        .toMap()
                    trySend(map)
                }
            }
        awaitClose { sub.remove() }
    }
}
