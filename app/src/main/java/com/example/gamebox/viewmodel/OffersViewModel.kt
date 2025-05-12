package com.example.gamebox.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebox.steam.repository.SteamRepository
import com.example.gamebox.steam.repository.FeaturedGame
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {
    private val _games = mutableStateListOf<FeaturedGame>()
    val games: List<FeaturedGame> get() = _games

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            val result = SteamRepository.getTopDiscountedGames(10)
            _games.clear()
            _games.addAll(result)
        }
    }
}
