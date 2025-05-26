package com.example.gamebox.recyclerview

import com.example.gamebox.R

class Reviews {

    private val reviews = ArrayList<ListaEntrada>()

    init {
        reviews.add(
            ListaEntrada(
                R.drawable.gtavi,
                "Grand Theft Auto VI",
                "Hace 1 hora",
                "[Aquí iría una reseña]"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.aw2,
                "Alan Wake II",
                "Hace un par de semanas",
                "[Aquí iría una reseña]"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.spiderman,
                "Marvel's Spider-Man",
                "Hace 3 días",
                "[Aquí iría una reseña]"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.sims2,
                "Los Sims 2",
                "Hace 3 días",
                "[Aquí iría una reseña]"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.schedule1,
                "Schedule I",
                "Hace 3 días",
                "[Aquí iría una reseña]"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.oblivion,
                "The Elder Scrolls IV: Oblivion",
                "Hace 3 días",
                "[Aquí iría una reseña]"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.balatro,
                "Balatro",
                "Hace 3 días",
                "[Aquí iría una reseña]"
            )
        )
    }

    fun getReviews(): ArrayList<ListaEntrada> {
        return reviews
    }
}
