package com.example.gamebox.model

import com.google.firebase.Timestamp

data class GameEntry(
    val docId: String = "",
    val gameId: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val collectionId: String = "",
    val addedAt: Timestamp = Timestamp.now()
)