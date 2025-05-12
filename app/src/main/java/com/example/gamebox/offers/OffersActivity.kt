package com.example.gamebox

import OffersAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.Offers.OfferGame
import com.example.gamebox.steam.repository.SteamRepository
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class OffersActivity : AppCompatActivity() {

    private lateinit var recyclerOffers: RecyclerView
    private lateinit var offersAdapter: OffersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)

        //Toolbar para la flecha de volver y titulo
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.title = "Juegos en oferta"

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        recyclerOffers = findViewById(R.id.recyclerOffers)
        recyclerOffers.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            val steamGames = SteamRepository.getTopDiscountedGames(10)
            offersAdapter = OffersAdapter(steamGames)
            recyclerOffers.adapter = offersAdapter
        }
    }

}
