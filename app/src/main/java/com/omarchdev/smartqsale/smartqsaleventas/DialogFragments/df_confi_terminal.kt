package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes

import com.omarchdev.smartqsale.smartqsaleventas.R
import android.widget.RadioGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.SesionUsuario
import kotlinx.android.synthetic.main.df_confi_terminal.*


class df_confi_terminal : DialogFragment() {


    lateinit var sesion:SesionUsuario
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.df_confi_terminal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sesion=SesionUsuario()
        Constantes.HorariosTerminal.list.forEach {
            val nuevoRadio = RadioButton(activity)
            val params = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT)
            nuevoRadio.layoutParams = params
            nuevoRadio.setText(it.descripcion)
            nuevoRadio.tag = it.id
            nuevoRadio.id=it.id
            rgHorario.addView(nuevoRadio)
        }

        rgHorario.check(sesion!!.ObtenerIdConfigHora())


        btnSalir.setOnClickListener {
            dialog?.dismiss()
        }
        btnGuardar.setOnClickListener {

            sesion?.GuardarFrecuenciaValidacionPin(rgHorario.checkedRadioButtonId)
            dialog?.dismiss()

        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}
