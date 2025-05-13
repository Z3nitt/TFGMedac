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
                "Muy buen juego, guapísimo."
            )
        )

        reviews.add(
            ListaEntrada(
                R.drawable.oblivion,
                "The Elder Scrolls IV: Oblivion",
                "Hace 2 horas",
                "Un juego impresionante con una historia profunda y un mundo abierto muy inmersivo."
            )
        )

        reviews.add(
            ListaEntrada(
                R.drawable.acshadows,
                "Assassin's Creed: Shadows",
                "Hace una semana",
                "Está guapo, se lo recomiendo a todo el mundo."
            )
        )

        reviews.add(
            ListaEntrada(
                R.drawable.schedule1,
                "Schedule I",
                "Hace un mes",
                "Simulador de ser Walter White. Juegazo"
            )
        )
    }

    fun getReviews(): ArrayList<ListaEntrada> {
        return reviews
    }
}
