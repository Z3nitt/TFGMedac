package com.example.gamebox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.Offers.OfferGame

class OffersAdapter(private val games: List<OfferGame>) :
    RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgGame: ImageView = itemView.findViewById(R.id.imgGame)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_offer_game, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val game = games[position]
        holder.imgGame.setImageResource(game.imageResId)
        holder.txtPrice.text = game.price
    }

    override fun getItemCount(): Int = games.size
}
