package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments


import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante

import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_df_agregar_opcion_variante.*

class DfAgregarOpcionVariante : DialogFragment() {

    interface IValVariante{
        fun RecepcionaValor(valor:String,idItem:Int,numItem:Int,IdProduct:Int)
    }

    var ivalVariante:IValVariante?=null
    var opcion:OpcionVariante?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_df_agregar_opcion_variante, container, false)
    }

    fun Instance(ivalVariante:IValVariante,opc:OpcionVariante):DfAgregarOpcionVariante{
        var f=DfAgregarOpcionVariante()
        f.opcion=opc
        f.ivalVariante=ivalVariante
        return f

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
       // edtOpcion.setText()

        btnCancelar.setOnClickListener {
            dismiss()
        }

        btnGuardar.setOnClickListener {

            if (edtOpcion!!.text.toString().isNotEmpty()) {
                var f=true
               opcion!!.listValores.forEach {

                   if(it.descripcion==edtOpcion!!.text.toString()){
                       f=false
                   }

               }
                if(f) {
                    dismiss()
                    ivalVariante?.RecepcionaValor(edtOpcion!!.text.toString(), opcion!!.idOpcionVariante, opcion!!.getiNumIntem(), opcion!!.idProduct)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)

        }
    }

}
