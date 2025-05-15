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

class OffersAdapter(private val games: List<UnifiedGame>) :
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



        holder.txtTitle.text = game.title
        holder.txtOriginalPrice.text = game.originalPrice
        holder.txtFinalPrice.text = game.finalPrice
        holder.txtDiscount.text = "-${game.discountPercent}%"

        holder.txtOriginalPrice.paintFlags =
            holder.txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        if(game.finalPrice == "0"){
            holder.txtFinalPrice.text = "GRATIS"
        }

        Glide.with(holder.itemView.context)
            .load(game.imageUrl)
            .into(holder.img)
    }

    override fun getItemCount(): Int = games.size

}
