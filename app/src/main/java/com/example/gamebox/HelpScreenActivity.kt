package com.example.gamebox

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.toLowerCase
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

class HelpScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_screen)
        setupToggle(R.id.p1, R.id.r1)
        setupToggle(R.id.p2, R.id.r2)
        setupToggle(R.id.p3, R.id.r3)
        setupToggle(R.id.p4, R.id.r4)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.appbar_ayuda)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupToggle(questionId: Int, answerId: Int) {
        val question = findViewById<TextView>(questionId)
        val answer = findViewById<TextView>(answerId)
        question.setOnClickListener {
            answer.visibility = if (answer.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }
}
