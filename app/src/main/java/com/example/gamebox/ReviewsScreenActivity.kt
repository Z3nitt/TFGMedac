package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebox.recyclerview.AdaptadorDeReviews
import com.example.gamebox.recyclerview.ListaEntrada
import com.example.gamebox.recyclerview.Reviews

class ReviewsScreenActivity : BaseActivity() {

    private lateinit var reviews: ArrayList<ListaEntrada>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews_screen)

        val recyclerViewReviews = findViewById<RecyclerView>(R.id.recyclerviewreviews)
        val goBack: ImageView = findViewById(R.id.goBack)

        reviews = Reviews(ctx = this).getReviews()

        recyclerViewReviews.layoutManager = LinearLayoutManager(this)
        recyclerViewReviews.adapter = AdaptadorDeReviews(reviews)


        goBack.setOnClickListener {
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
