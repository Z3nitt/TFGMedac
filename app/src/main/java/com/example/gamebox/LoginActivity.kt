package com.example.gamebox

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setup();
    }

    private fun setup() {
        //Obtengo todos los elementos
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val actionButton = findViewById<Button>(R.id.actionButton)
        val toggleModeText = findViewById<TextView>(R.id.toggleModeText)
        val titleText = findViewById<TextView>(R.id.titleText)
        val errorText = findViewById<TextView>(R.id.errorText);

        //Detecta el click en el boton
        actionButton.setOnClickListener {
            //Comprueba que no esten vacios los editText
            if (emailEditText.text.isEmpty() && passwordEditText.text.isEmpty()) {
                showAlertDialog(
                    title = "Campos vacios",
                    message = "Asegurate de que los campos no esten vacios!"
                );
            }
            //Comprueba la contraseña tenga minimo 6 caracteres
            else if (passwordEditText.text.length < 6) {
                showAlertDialog(
                    title = "Contraseña muy corta",
                    message = "La contraseña tiene que tener minimo 6 caracteres"
                )
            }
            //Si es correcto, pasa a crear el usuario
            else {
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
                                title = "Error al registrar",
                                message = "Ha ocurrido un error al registrar el usuario"
                            );
                        }
                    }
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
        //ACA PONER EL INTENT QUE DIRIGE A LA HOME
    }
}
