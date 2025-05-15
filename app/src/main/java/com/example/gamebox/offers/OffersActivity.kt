package com.example.gamebox

import OffersAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.epic.repository.EpicRepository
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

        val loader = findViewById<ProgressBar>(R.id.loader)
        loader.visibility = View.VISIBLE

        recyclerOffers = findViewById(R.id.recyclerOffers)
        recyclerOffers.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            val steamGames = SteamRepository.getTopDiscountedGames()
            val epicGames = EpicRepository().searchDiscountedGames()
            loader.visibility = View.GONE

            val allGames = mutableListOf<UnifiedGame>()

            allGames += steamGames.map {
                UnifiedGame(
                    title = it.name,
                    imageUrl = it.image,
                    finalPrice = formatPrice(it.finalPrice),
                    originalPrice = formatPrice(it.originalPrice),
                    discountPercent = it.discountPercent.toString()
                )
            }

            allGames += epicGames.map {
                UnifiedGame(
                    title = it.title,
                    imageUrl = it.imageUrl,
                    finalPrice = it.finalPrice,
                    originalPrice = it.originalPrice,
                    discountPercent = it.discountPercent
                )
            }
            offersAdapter = OffersAdapter(allGames)
            recyclerOffers.adapter = offersAdapter
        }
    }

    fun formatPrice(price: Int?): String {
        if (price == null) return "—"
        return "€%.2f".format(price / 100.0)
    }

}

data class UnifiedGame(
    val title: String,
    val imageUrl: String,
    val finalPrice: String?,
    val originalPrice: String?,
    val discountPercent: String?
)

