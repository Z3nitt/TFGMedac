package com.example.gamebox

import OffersAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.epic.repository.EpicRepository
import com.example.gamebox.steam.repository.SteamRepository
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class OffersActivity : BaseActivity() {

    private lateinit var recyclerOffers: RecyclerView
    private lateinit var offersAdapter: OffersAdapter
    private lateinit var fullList: List<UnifiedGame>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)

        //Toolbar para la flecha de volver y titulo
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.juegos_en_oferta_con_emoji)

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
                    appId = it.id,
                    title = it.name,
                    imageUrl = it.image,
                    finalPrice = formatPrice(it.finalPrice),
                    originalPrice = formatPrice(it.originalPrice),
                    discountPercent = it.discountPercent.toString(),
                    platform = "steam"
                )
            }

            allGames += epicGames.map {
                UnifiedGame(
                    title = it.title,
                    imageUrl = it.imageUrl,
                    finalPrice = it.finalPrice,
                    originalPrice = it.originalPrice,
                    discountPercent = it.discountPercent,
                    platform = "epic"
                )
            }
            fullList = allGames
            offersAdapter = OffersAdapter(fullList)
            recyclerOffers.adapter = offersAdapter

            val platformFilter: Spinner = findViewById(R.id.platformFilter)

            //Listener para el Spinner para elegir las plataformas
            platformFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val filtered = when (position) {
                    1 -> fullList.filter { it.platform == "steam" }
                    2 -> fullList.filter { it.platform == "epic" }
                    else -> fullList
                }
                    offersAdapter.updateList(filtered)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

    }

    fun formatPrice(price: Int?): String {
        if (price == null) return "—"
        return "€%.2f".format(price / 100.0)
    }

}

//Clase para reunir todos los juegos de cualquier plataforma
data class UnifiedGame(
    val appId: Int? = null, //SOLO PARA STEAM
    val title: String,
    val imageUrl: String,
    val finalPrice: String?,
    val originalPrice: String?,
    val discountPercent: String?,
    val platform: String
)

