package com.example.gamebox

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class SettingsViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view)

        val backBtn: ImageView = findViewById(R.id.backButton)

        backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        val spinner = findViewById<Spinner>(R.id.spinner_language)


        val labels = resources.getStringArray(R.array.language_labels)
        val values = resources.getStringArray(R.array.language_values)


        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val currentLang = prefs.getString("app_language", Locale.getDefault().language) ?: "es"
        val currentIndex = values.indexOf(currentLang).coerceAtLeast(0)
        spinner.setSelection(currentIndex)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLang = values[position]
                if (selectedLang != currentLang) {
                    LocaleHelper.setLocale(this@SettingsViewActivity, selectedLang)

                    val intent = Intent(this@SettingsViewActivity, MainScreenActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}