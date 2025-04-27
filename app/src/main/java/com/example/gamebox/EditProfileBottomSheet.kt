package com.example.gamebox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditProfileBottomSheet : BottomSheetDialogFragment() { //Es un fragmento que aparece en la pantalla desde abajo

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

        // Cerrar cuando toquen la X
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        // Guardar cambios cuando toquen "Guardar"
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_save -> {
                    // Aquí haces la lógica para guardar los datos
                    val newUsername = view.findViewById<EditText>(R.id.editTextUsername).text.toString()
                    // Puedes enviar esto de vuelta a la actividad o fragment
                    dismiss()
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

}