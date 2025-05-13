package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gamebox.settingsactivities.blockedusers
import com.example.gamebox.settingsactivities.condicionesdeuso
import com.example.gamebox.settingsactivities.helpcenter
import com.example.gamebox.settingsactivities.renewsubscription
import com.example.gamebox.settingsactivities.reportaproblem

class SettingsViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings_view)

        val goBack: ImageView = findViewById(R.id.backButton)

        val blockedUsers: TextView = findViewById(R.id.blockedUsers)

        val restoreSubscription: TextView = findViewById(R.id.restaurarSuscripcion)

        val helpCenter: TextView = findViewById(R.id.centroAyuda)

        val reportProblem: TextView = findViewById(R.id.reportProblem)

        val condicionesUso: TextView = findViewById(R.id.condicionesUso)

        goBack.setOnClickListener {
            startActivity(Intent(this, MainScreenActivity::class.java))
        }

        blockedUsers.setOnClickListener {
            startActivity(Intent(this, blockedusers::class.java))
        }

        restoreSubscription.setOnClickListener {
            startActivity(Intent(this, renewsubscription::class.java))
        }

        helpCenter.setOnClickListener {
            startActivity(Intent(this, helpcenter::class.java))
        }

        reportProblem.setOnClickListener {
            startActivity(Intent(this, reportaproblem::class.java))
        }

        condicionesUso.setOnClickListener {
            startActivity(Intent(this, condicionesdeuso::class.java))
        }
    }
}