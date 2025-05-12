package com.example.gamebox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.Offers.OfferGame
import com.google.android.material.appbar.MaterialToolbar

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

        val offerList = listOf(
            OfferGame("Wolverine Venganza", R.drawable.oblivion, "14,99€"),
            OfferGame("Dr. Stone #27", R.drawable.oblivion, "9,99€"),
            OfferGame("HULK #12", R.drawable.oblivion, "12,49€"),
            OfferGame("Daredevil", R.drawable.oblivion, "11,99€"),
            OfferGame("Blazer", R.drawable.oblivion, "10,00€"),
            OfferGame("Blazer", R.drawable.oblivion, "10,00€"),
            OfferGame("Blazer", R.drawable.oblivion, "10,00€"),
            OfferGame("Blazer", R.drawable.oblivion, "10,00€"),
            OfferGame("Blazer", R.drawable.oblivion, "10,00€"),
            OfferGame("Blazer", R.drawable.oblivion, "10,00€"),
        )

        offersAdapter = OffersAdapter(offerList)
        recyclerOffers.adapter = offersAdapter
    }
}
