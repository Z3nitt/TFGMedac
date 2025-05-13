package com.example.gamebox

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

//Esto sirve para actualizar el perfil en la ventana anterior
interface OnProfileUpdatedListener {
    fun onProfileUpdated(newUsername: String)
}


class EditProfileBottomSheet : BottomSheetDialogFragment() { //Es un fragment que aparece en la pantalla desde abajo

    var profileUpdatedListener: OnProfileUpdatedListener? = null

    private lateinit var editTextEmail: EditText
    private lateinit var editTextUsername: EditText
    private var usuario: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.edit_profile_menu)
       editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextUsername = view.findViewById(R.id.editTextUsername)

        usuario = FirebaseAuth.getInstance().currentUser
        //Comprueba si el usuario es nulo por las dudas
        var email = usuario?.email
        var username = usuario?.displayName

        editTextEmail.setText(email)
        editTextUsername.setText(username)

        // Cerrar cuando toquen la flecha.
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        // Guardar cambios cuando toquen "Guardar"
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_save -> { //Detecta que se tocó el boton de "guardar"
                    editUser()
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        //Cambia la altura del BottomSheet a MATCH_PARENT, o sea, ocupa toda la pantalla.
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun editUser() {
        val newUsername = editTextUsername.text.toString().trim()
       // val newEmail = editTextEmail.text.toString().trim()

       // val isMailValid = Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()

        //Verifica si los campos estan vacios
        if(newUsername.isNotEmpty()){
            //Verifica si el mail es valido
           // if(isMailValid){
                updateUserValues(newUsername)
            //}
            //else{
               // Toast.makeText(context, "El mail introducido no es válido", Toast.LENGTH_SHORT).show()
            //}
        }else{
            Toast.makeText(context, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserValues(newUsername: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return //Verifica que no sea nulo

        val profileUpdates = userProfileChangeRequest {
            displayName = newUsername
        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show()

                    //Esto sirve para actualizar el perfil en la ventana anterior
                    profileUpdatedListener?.onProfileUpdated(newUsername)
                    dismiss()

                } else {
                    Toast.makeText(context, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show()
                    Log.e("Firebase", "Error al actualizar nombre: ${task.exception?.message}")
                }
            }
        //SE PUEDE CAMBIAR EL MAIL PERO ES MANDANDO VERIFCIACION AL CORREO
    }


}