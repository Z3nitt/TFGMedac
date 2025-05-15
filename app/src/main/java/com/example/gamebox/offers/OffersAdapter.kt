import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamebox.R
import com.example.gamebox.UnifiedGame

class OffersAdapter(private var games: List<UnifiedGame>) :
    RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgGame)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtOriginalPrice: TextView = view.findViewById(R.id.txtOriginalPrice)
        val txtFinalPrice: TextView = view.findViewById(R.id.txtFinalPrice)
        val txtDiscount: TextView = view.findViewById(R.id.txtDiscount)
        val imgPlatformLogo: ImageView = view.findViewById(R.id.imgPlatformLogo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_offer_game, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val game = games[position]

        holder.txtTitle.text = game.title
        holder.txtOriginalPrice.text = game.originalPrice
        holder.txtFinalPrice.text = game.finalPrice
        holder.txtDiscount.text = "-${game.discountPercent}%"

        holder.txtOriginalPrice.paintFlags =
            holder.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        if(game.finalPrice == "0"){
            holder.txtFinalPrice.text = "GRATIS"
        }

        //Dependiendo de la plataforma, muestra el logo correspondiente
        val logoRes = when (game.platform.lowercase()) {
            "steam" -> R.drawable.ic_steam
            "epic" -> R.drawable.ic_epic
            else -> null
        }

        if (logoRes != null) {
            holder.imgPlatformLogo.setImageResource(logoRes)
        }

        Glide.with(holder.itemView.context)
            .load(game.imageUrl)
            .into(holder.img)
    }

    override fun getItemCount(): Int = games.size

    fun updateList(newGames: List<UnifiedGame>) {
        games = newGames
        //Se puede hacer mas eficiente
        notifyDataSetChanged()
    }

}
