import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamebox.R
import com.example.gamebox.steam.repository.FeaturedGame

class OffersAdapter(private val games: List<FeaturedGame>) :
    RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgGame)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtOriginalPrice: TextView = view.findViewById(R.id.txtOriginalPrice)
        val txtFinalPrice: TextView = view.findViewById(R.id.txtFinalPrice)
        val txtDiscount: TextView = view.findViewById(R.id.txtDiscount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_offer_game, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val game = games[position]

        holder.txtTitle.text = game.name
        holder.txtOriginalPrice.text = formatPrice(game.originalPrice)
        holder.txtFinalPrice.text = formatPrice(game.finalPrice)
        holder.txtDiscount.text = "-${game.discountPercent}%"

        holder.txtOriginalPrice.paintFlags =
            holder.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        Glide.with(holder.itemView.context)
            .load(game.image)
            .into(holder.img)
    }

    override fun getItemCount(): Int = games.size

    private fun formatPrice(price: Int?): String {
        if (price == null) return "—"
        val euros = price / 100.0
        return "€%.2f".format(euros)
    }
}
