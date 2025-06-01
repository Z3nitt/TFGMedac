package com.example.gamebox.recyclerview

import android.content.Context
import com.example.gamebox.R

class Reviews (private val ctx: Context){

    private val reviews = ArrayList<ListaEntrada>()

    init {
        reviews.add(
            ListaEntrada(
                R.drawable.gtavi,
                "Grand Theft Auto VI",
                ctx.getString(R.string.time_1_hour),
                "Una experiencia épica con mundo abierto impresionante, misiones variadas y gráficos de última generación que redefinen la saga.",
                autor = "Santiago Spina"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.aw2,
                "Alan Wake II",
                ctx.getString(R.string.time_2_weeks),
                "Alan Wake II expande la mitología de Bright Falls con una trama oscura y retorcida que atrapa desde el primer capítulo. La combinación de narrativa episódica y mecánicas de terror psicológico logra momentos de verdadera tensión, mientras el trabajo de iluminación y sonido potencia una atmósfera opresiva. Además, la evolución del combate con linterna y armas de fuego ofrece una experiencia satisfactoria que mantiene el pulso en cada enfrentamiento."
                ,autor = "Juan Luis Serrano"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.spiderman,
                "Marvel's Spider-Man",
                ctx.getString(R.string.time_3_days),
                "Jugabilidad fluida y acrobática, historia emotiva y un Nueva York vibrante que capta la esencia del trepamuros."
                ,autor = "Armando Garcia"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.sims2,
                "Los Sims 2",
                ctx.getString(R.string.time_3_days),
                "Los Sims 2 sigue siendo un hito en la simulación de vida gracias a su sistema de herencias genéticas y las trayectorias de vida dinámicas. Cada decisión del jugador—desde desarrollar habilidades hasta forjar relaciones—se refleja en generaciones posteriores, dando un sentido de continuidad único. Las expansiones enriquecen aún más la experiencia con carreras, vacaciones y aventuras sobrenaturales, haciendo que cada partida se sienta fresca y llena de posibilidades."
                ,autor = "Juan Francisco Martinez"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.schedule1,
                "Schedule I",
                ctx.getString(R.string.time_3_days),
                "Juego de puzles con narrativa intrigante, mecánicas desafiantes y estética minimalista que engancha desde el principio."
                ,autor = "Daniel Ye"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.oblivion,
                "The Elder Scrolls IV: Oblivion",
                ctx.getString(R.string.time_3_days),
                "RPG monumental con mundo inmenso, libertad total para explorar y una historia que atrapa durante horas."
                ,autor = "User984923GR"
            )
        )
        reviews.add(
            ListaEntrada(
                R.drawable.balatro,
                "Balatro",
                ctx.getString(R.string.time_3_days),
                "Balatro destaca por su sentido del humor mordaz y personajes inusuales que rompen con lo convencional. La narrativa avanza a través de diálogos ágiles y situaciones absurdas que invitan a explorar cada rincón del mundo con curiosidad. Su estilo artístico desenfadado y el diseño de niveles no lineal permiten al jugador disfrutar de minijuegos, puzles y encuentros impredecibles que convierten a Balatro en una propuesta indie fresca y memorable."
                ,autor = "Carl Johnson"
            )
        )

    }

    fun getReviews(): ArrayList<ListaEntrada> {
        return reviews
    }
}
