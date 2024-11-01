package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.dialog_elegir_guardar.view.*

class DialogGuardarProcesoAlmacen : DialogFragment() {

    var titulo = ""

    private lateinit var listenerAccionGuardarCompra: ListenerAccionGuardarCompra

    public interface ListenerAccionGuardarCompra {

        fun GuardarSinProcesar()
        fun ProcesarGuardar()

    }

    fun setListenerAccionGuardarCompra(listenerAccionGuardarCompra: ListenerAccionGuardarCompra) {
        this.listenerAccionGuardarCompra = listenerAccionGuardarCompra
    }


    fun ObtenerTitulo(titulo: String) {
        this.titulo = titulo
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View
        view = LayoutInflater.from(activity).inflate(R.layout.dialog_elegir_guardar, null)
        var dialog = AlertDialog.Builder(activity)

        dialog.setTitle("$titulo").setView(view)

        view?.btnGuardarNProcesar?.setOnClickListener {

            listenerAccionGuardarCompra.GuardarSinProcesar()

        }
        view?.btnGuardarSProcesar?.setOnClickListener {
            listenerAccionGuardarCompra.ProcesarGuardar()
        }
        view?.btnCancelar?.setOnClickListener {

            this.dismiss()

        }
        return dialog.create()
    }

}