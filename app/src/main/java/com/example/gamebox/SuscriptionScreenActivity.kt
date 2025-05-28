package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SuscriptionScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_suscription_screen)

        val goback: ImageView = findViewById(R.id.back)

        goback.setOnClickListener {
            startActivity(Intent(this, MainScreenActivity::class.java))
        }

        val rbMonthly: RadioButton  = findViewById(R.id.monthly_plan)
        val rbYearly: RadioButton  = findViewById(R.id.yearly_plan)
        val rbLifetime: RadioButton  = findViewById(R.id.lifetime_plan)

        rbMonthly.setOnClickListener{
            rbMonthly.isChecked = true
            rbYearly.isChecked = false
            rbLifetime.isChecked = false
        }

        rbYearly.setOnClickListener{
            rbYearly.isChecked = true
            rbMonthly.isChecked = false
            rbLifetime.isChecked = false
        }

        rbLifetime.setOnClickListener{
            rbLifetime.isChecked = true
            rbYearly.isChecked = false
            rbMonthly.isChecked = false
        }

    }
}