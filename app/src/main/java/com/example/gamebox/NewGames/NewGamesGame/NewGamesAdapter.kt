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

class NewGamesAdapter(private val games: List<FeaturedGame>) :
    RecyclerView.Adapter<NewGamesAdapter.NewGameViewHolder>() {

    inner class NewGameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgGame)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewGameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_newgame, parent, false)
        return NewGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewGameViewHolder, position: Int) {
        val game = games[position]

        holder.txtTitle.text = game.name
        holder.txtPrice.text = formatPrice(game.originalPrice)

        /*holder.txtPrice.paintFlags =
            holder.txtPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG*/

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
