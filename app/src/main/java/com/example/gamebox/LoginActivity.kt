package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //Declaración de elementos
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var actionButton: Button
    private lateinit var toggleModeText: TextView
    private lateinit var titleText: TextView
    private var isLogin = true; //Esta variable controla si esta logueando o registrando un usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Inicialización de variables
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        actionButton = findViewById(R.id.actionButton)
        toggleModeText = findViewById(R.id.toggleModeText)
        titleText = findViewById(R.id.titleText)

        setup();
    }

    private fun setup() {

        //Detecta el click en el boton
        actionButton.setOnClickListener {
            //Comprueba que no esten vacios los editText
            if (emailEditText.text.isEmpty() && passwordEditText.text.isEmpty()) {
                showAlertDialog(
                    title = "Campos vacios",
                    message = "Asegurate de que los campos no esten vacios!"
                );
            }
            //Si es correcto, pasa a crear el usuario
            else {
                if (isLogin) logUser() else registerUser();
            }
        }

        //Detecta el click en el texto para cambiar de inicio de sesion a registro y viceversa
        toggleModeText.setOnClickListener {
            if (isLogin) {
                titleText.setText("Registrar usuario")
                actionButton.setText("Registrar usuario")
                toggleModeText.setText("¿Ya tienes cuenta? Inicia sesión")
                isLogin = false;
            } else {
                titleText.setText("Iniciar sesión")
                actionButton.setText("Iniciar sesión")
                toggleModeText.setText("¿No tienes cuenta? Regístrate")
                isLogin = true;
            }

        }

    }

    private fun registerUser() {
        //Variable que verifica si el mail es correcto
        var isMailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()

        if(!isMailValid){
            showAlertDialog(
                title = "El mail introducido no es válido",
                message = "Asegurate de poner un formato correcto en el mail"
            )
        }
        //Comprueba la contraseña tenga minimo 6 caracteres
        else if (passwordEditText.text.length < 6) {
            showAlertDialog(
                title = "Contraseña muy corta",
                message = "La contraseña tiene que tener minimo 6 caracteres"
            )
        } else {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        showHome() //Mostrar la pantalla de inicio
                    } else {
                        //Muestra un mensaje de error
                        showAlertDialog(
                            title = "Error al registrar el usuario",
                            message = "Ha ocurrido un error al registrar el usuario"
                        );
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
                    Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
                    showHome() //Mostrar la pantalla de inicio
                } else {
                    //Muestra un mensaje de error
                    showAlertDialog(
                        title = "Error al iniciar sesion",
                        message = "No se ha encontrado el usuario"
                    );
                }
            }
    }

    //Funcion que muestra un dialog con los campos indicados
    //(Para no escribir cuatrocientos dialogs distintos)
    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }
}
