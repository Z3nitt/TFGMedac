package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var actionButton: Button
    private lateinit var titleText: TextView
    private lateinit var backButton: ImageButton
    private var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isLogin = intent.getBooleanExtra("isLogin", true)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        actionButton = findViewById(R.id.actionButton)
        titleText = findViewById(R.id.titleText)
        backButton = findViewById(R.id.backButton)

        // Cambia el texto según el modo
        if (!isLogin) {
            titleText.text = "Registrar usuario"
            actionButton.text = "Registrar usuario"
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }

        setup()
    }

    private fun setup() {
        actionButton.setOnClickListener {
            if (emailEditText.text.isEmpty() || passwordEditText.text.isEmpty()) {
                showAlertDialog("Campos vacíos", "Asegúrate de que los campos no estén vacíos!")
            } else {
                if (isLogin) logUser() else registerUser()
            }
        }
    }

    private fun registerUser() {
        val isMailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()

        if (!isMailValid) {
            showAlertDialog("El mail introducido no es válido", "Asegúrate de poner un formato correcto en el mail")
        } else if (passwordEditText.text.length < 6) {
            showAlertDialog("Contraseña muy corta", "La contraseña tiene que tener mínimo 6 caracteres")
        } else {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        showHome()
                    } else {
                        showAlertDialog("Error al registrar el usuario", "Ha ocurrido un error al registrar el usuario")
                    }
                }
        }
    }

    private fun logUser() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    showHome()
                } else {
                    showAlertDialog("Error al iniciar sesión", "No se ha encontrado el usuario")
                }
            }
    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        builder.create().show()
    }

    private fun showHome() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }
}
