package com.example.gamebox

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailedReview : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detailed_review)

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val gameNameTxt = findViewById<TextView>(R.id.tituloVideojuegoCompleto)
        val gameImageView = findViewById<ImageView>(R.id.imagenPortadaCompleta)
        val timeTxt = findViewById<TextView>(R.id.textoFecha)
        val reviewTxt = findViewById<TextView>(R.id.textoReviewCompleta)
        val autorTxt = findViewById<TextView>(R.id.textoAutor)

        val gameName  = intent.getStringExtra("game_name")
        val gameImage  = intent.getIntExtra("game_image", 0)
        val time  = intent.getStringExtra("time")
        val review  = intent.getStringExtra("review")
        val autor = intent.getStringExtra("autor")

        gameNameTxt.text = gameName
        gameImageView.setImageResource(gameImage)
        timeTxt.text = time
        reviewTxt.text = review
        autorTxt.text = autor

        //Listener para volver atras
        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}