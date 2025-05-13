package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle

class MainScreenActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)

        drawerLayout = findViewById(R.id.drawer_layout)
        val menuButton: ImageView = findViewById(R.id.menulateral)
        val suscriptionScreen: Button = findViewById(R.id.button)
        val pfScreen: ImageView = findViewById(R.id.pfp)
        val moreText: TextView = findViewById(R.id.more)
        val more2Text: TextView = findViewById(R.id.more2)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.openDrawer, R.string.closeDrawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar clics del menú lateral
        setupDrawerMenu()

        // Configurar clics en la pantalla principal
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        pfScreen.setOnClickListener {
            startActivity(Intent(this, ProfileScreenActivity::class.java))
        }

        suscriptionScreen.setOnClickListener {
            startActivity(Intent(this, SuscriptionScreenActivity::class.java))
        }

        moreText.setOnClickListener {
            startActivity(Intent(this, NewGamesScreenActivity::class.java))
        }

        more2Text.setOnClickListener {
            startActivity(Intent(this, PopularScreenActivity::class.java))
        }

        // Ajustar márgenes para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupDrawerMenu() {

        findViewById<TextView>(R.id.menu_resenas).setOnClickListener {
            navigateTo(ReviewsScreenActivity::class.java)
        }

        findViewById<TextView>(R.id.menu_configuracion).setOnClickListener {
            navigateTo(SettingsViewActivity::class.java)
        }
        // Configurar los elementos del menú lateral para que lleve a las correspondientes pantallas

        /*findViewById<TextView>(R.id.menu_juegoteca).setOnClickListener {
            navigateTo(JuegotecaActivity::class.java)
        }

        findViewById<TextView>(R.id.menu_ofertas).setOnClickListener {
            navigateTo(OfertasActivity::class.java)
        }

        findViewById<TextView>(R.id.menu_novedades).setOnClickListener {
            navigateTo(NovedadesActivity::class.java)
        }

        findViewById<TextView>(R.id.menu_logros).setOnClickListener {
            navigateTo(LogrosActivity::class.java)
        }

        findViewById<TextView>(R.id.menu_ayuda).setOnClickListener {
            navigateTo(AyudaActivity::class.java)
        }

        findViewById<ImageView>(R.id.drawer_pfp).setOnClickListener {
            navigateTo(ProfileScreenActivity::class.java)
        }*/

        findViewById<ImageView>(R.id.drawer_pfp).setOnClickListener {
            navigateTo(ProfileScreenActivity::class.java)
        }
    }

    private fun <T : Any> navigateTo(activityClass: Class<T>) {
        startActivity(Intent(this, activityClass))
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}