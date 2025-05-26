package com.example.gamebox.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.R

class AdaptadorDeReviews(
    private val userModelList: List<ListaEntrada>
) : RecyclerView.Adapter<AdaptadorDeReviews.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reviews, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entrada = userModelList[position]
        holder.imagenPortada.setImageResource(entrada.idImagen)
        holder.tituloVideojuego.text = entrada.tituloVideojuego
        holder.textoReview.text = entrada.textoReview
        holder.textoTiempo.text = entrada.tiempo

        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val nombre = userModelList[pos].tituloVideojuego
                Toast.makeText(it.context, "Nombre del videojuego: $nombre", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int = userModelList.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imagenPortada: ImageView = v.findViewById(R.id.imagenPortada)
        val tituloVideojuego: TextView = v.findViewById(R.id.tituloVideojuego)
        val textoTiempo: TextView = v.findViewById(R.id.textoTiempo)
        val textoReview: TextView = v.findViewById(R.id.textoReview)

        init {
            tituloVideojuego.setTextColor(Color.BLACK)
            textoReview.setTextColor(Color.BLACK)
            textoTiempo.setTextColor(Color.BLACK)
        }
    }
}
