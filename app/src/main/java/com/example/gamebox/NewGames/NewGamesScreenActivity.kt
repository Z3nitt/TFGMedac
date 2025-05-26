package com.example.gamebox

import NewGamesAdapter
import OffersAdapter
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.Offers.OfferGame
import com.example.gamebox.steam.repository.SteamRepository
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class NewGamesScreenActivity : AppCompatActivity() {

    private lateinit var recyclerNewGames: RecyclerView
    private lateinit var newGamesAdapter: NewGamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_games_screen)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.title = "Novedades"

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val loader = findViewById<ProgressBar>(R.id.loader)
        loader.visibility = View.VISIBLE

        recyclerNewGames = findViewById(R.id.recyclerNewGames)
        recyclerNewGames.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            val steamGames = SteamRepository.getNewReleases()
            loader.visibility = View.GONE

            newGamesAdapter = NewGamesAdapter(steamGames)
            recyclerNewGames.adapter = newGamesAdapter
        }
    }

}
