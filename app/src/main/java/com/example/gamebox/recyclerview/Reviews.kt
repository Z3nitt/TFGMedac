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
                "Una experiencia épica con mundo abierto impresionante, misiones variadas y gráficos de última generación que redefinen la saga."
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.aw2,
                "Alan Wake II",
                "Hace un par de semanas",
                "Una historia absorbente con atmósfera escalofriante, narrativa envolvente y mecánicas de terror refinadas."
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.spiderman,
                "Marvel's Spider-Man",
                "Hace 3 días",
                "Jugabilidad fluida y acrobática, historia emotiva y un Nueva York vibrante que capta la esencia del trepamuros."
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.sims2,
                "Los Sims 2",
                "Hace 3 días",
                "Clásico atemporal que ofrece libertad creativa y simulación de vida profunda, con infinitas posibilidades de juego."
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.schedule1,
                "Schedule I",
                "Hace 3 días",
                "Juego de puzles con narrativa intrigante, mecánicas desafiantes y estética minimalista que engancha desde el principio."
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.oblivion,
                "The Elder Scrolls IV: Oblivion",
                "Hace 3 días",
                "RPG monumental con mundo inmenso, libertad total para explorar y una historia que atrapa durante horas."
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.balatro,
                "Balatro",
                "Hace 3 días",
                "Título indie con estilo único, humor mordaz y personajes carismáticos que ofrecen una experiencia fresca y divertida."
            )
        )

    }

    fun getReviews(): ArrayList<ListaEntrada> {
        return reviews
    }
}
