package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ProfileScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileScreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val userEmailText = findViewById<TextView>(R.id.userEmailText)
        val usernameText = findViewById<TextView>(R.id.usernameText)
        val editProfileBottomSheet = EditProfileBottomSheet()
        val editProfileBtn = findViewById<Button>(R.id.editProfileBtn)

        val usuario = FirebaseAuth.getInstance().currentUser
        var email = usuario?.email //Comprueba si el usuario es nulo por las dudas
        var username = usuario?.displayName

        userEmailText.setText(email)
        usernameText.setText(username)

        //Listener para volver atras
        backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //Vuelve a la activity anterior
        }

        //Listener para el boton de editar el perfil
        editProfileBtn.setOnClickListener{
            //Listener para cuando actualiza el perfil, para actualizar la ventana actual
            editProfileBottomSheet.profileUpdatedListener = object : OnProfileUpdatedListener {
                override fun onProfileUpdated(newUsername: String) {
                    // Actualiza el nombre en la UI del perfil
                    usernameText.text = newUsername
                }
            }
            //Muestra el bottomSheetFragment
            editProfileBottomSheet.show(supportFragmentManager, editProfileBottomSheet.tag)
        }

        //Listener para cerrar sesion
        logoutBtn.setOnClickListener {
            //Desloguea la sesion actual
            FirebaseAuth.getInstance().signOut()
            // Luego de desloguearse, vuelve al login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK //Limpia la pila de actividades
            Toast.makeText(this, "Deslogueo exitoso", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish() // Esto cierra la activity actual para que no pueda volver con el botón atrás


        }
    }


}