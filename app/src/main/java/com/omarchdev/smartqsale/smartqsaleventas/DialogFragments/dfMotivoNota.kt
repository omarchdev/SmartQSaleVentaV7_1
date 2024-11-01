package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.df_motivo_nota.*


class dfMotivoNota : DialogFragment() {

    private var listener: IMotivoNota? = null
    private var motivodesc=""
    private var titulo=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_motivo_nota, container, false)
    }


    fun newInstance(titulo:String,motivodes:String):dfMotivoNota{

        val f=dfMotivoNota()
        f.motivodesc=motivodes
        f.titulo=titulo
        return f

    }

    fun onButtonPressed(motivo:String) {
        listener?.RecibirMotivoNota(motivo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtMotivoNota.hint=motivodesc
        txtTituloMotivo.text=titulo
        btnAceptar.setOnClickListener {
            if(edtMotivoNota.editText?.text.toString().trim().length>2) {
                listener?.RecibirMotivoNota(edtMotivoNota.editText?.text.toString())
                dismiss()
            }else{
                if(edtMotivoNota.editText?.text.toString().trim().isEmpty()){
                    edtMotivoNota.error="Debe ingresar un motivo para generar la nota"
                }else{
                    edtMotivoNota.error="El motivo es muy corto."
                }
            }
        }
        btnCancelar.setOnClickListener {
            dismiss()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IMotivoNota) {
            listener = context
        } else {
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface IMotivoNota {
        fun RecibirMotivoNota(motivo: String)
    }

}
