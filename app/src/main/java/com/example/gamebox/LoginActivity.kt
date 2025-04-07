package com.example.gamebox

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val actionButton = findViewById<Button>(R.id.actionButton)
        val toggleModeText = findViewById<TextView>(R.id.toggleModeText)
        val titleText = findViewById<TextView>(R.id.titleText)
        val errorText = findViewById<TextView>(R.id.errorText)

        actionButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isLogin) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // TODO: ir a pantalla principal
                        } else {
                            errorText.text = task.exception?.message
                            errorText.visibility = View.VISIBLE
                        }
                    }
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // TODO: ir a pantalla principal
                        } else {
                            errorText.text = task.exception?.message
                            errorText.visibility = View.VISIBLE
                        }
                    }
            }
        }

        toggleModeText.setOnClickListener {
            isLogin = !isLogin
            titleText.text = if (isLogin) "Iniciar sesión" else "Registro"
            actionButton.text = if (isLogin) "Iniciar sesión" else "Registrarse"
            toggleModeText.text = if (isLogin)
                "¿No tienes cuenta? Regístrate"
            else
                "¿Ya tienes cuenta? Inicia sesión"
        }
    }
}
