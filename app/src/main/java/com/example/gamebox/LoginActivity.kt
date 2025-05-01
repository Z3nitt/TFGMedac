package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
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
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        actionButton = findViewById(R.id.actionButton)
        titleText = findViewById(R.id.titleText)
        backButton = findViewById(R.id.backButton)

        usernameEditText.visibility = View.INVISIBLE

        // Cambia el texto según el modo
        if (!isLogin) {
            titleText.text = "Registrar usuario"
            actionButton.text = "Registrar usuario"
            usernameEditText.visibility = View.VISIBLE
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }

        setup()
    }

    private fun setup() {
        actionButton.setOnClickListener {
            if (emailEditText.text.trim().isEmpty() || passwordEditText.text.trim().isEmpty()) {
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
        } else if (passwordEditText.text.trim().length < 6) {
            showAlertDialog("Contraseña muy corta", "La contraseña tiene que tener mínimo 6 caracteres")
        }
        else if(usernameEditText.text.trim().isEmpty()){
            showAlertDialog("Debes poner un nombre de usuario", "Indica un nombre de usuario")
        }
        else {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        addUserName()
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        showHome()
                    } else {
                        showAlertDialog("Error al registrar el usuario", "Ha ocurrido un error al registrar el usuario")
                    }
                }
        }
    }

    private fun addUserName() {
        val user = FirebaseAuth.getInstance().currentUser //Guardo el usuario ya creado

        //Actualizo el displayName del usuario
        val profileUpdates = userProfileChangeRequest {
            displayName = usernameEditText.text.toString().trim()
        }
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
            if (!updateTask.isSuccessful) { //Controlo un posible error
                Log.e("Firebase", "Error actualizando nombre de usuario", updateTask.exception)
                Toast.makeText(this, "Error actualizando nombre de usuario", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun logUser() {
        Log.e("Firebase",  emailEditText.text.toString() + " " +passwordEditText.text.toString() )
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
        val homeIntent = Intent(this, MainScreenActivity::class.java)
        startActivity(homeIntent)
    }
}
