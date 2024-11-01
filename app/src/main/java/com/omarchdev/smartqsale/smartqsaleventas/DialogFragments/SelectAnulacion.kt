package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoAnulacion

import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_select_anulacion.*


class SelectAnulacion : DialogFragment() {

    private var listener: CodeAnulacion? = null
    lateinit var list:List<TipoAnulacion>

    fun newInstance(listAnulaciones:List<TipoAnulacion>):SelectAnulacion{

        val f=SelectAnulacion()
        f.list=listAnulaciones
        return f

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_anulacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSalir.setOnClickListener {
            this.dismiss()
        }
        var adapter=ArrayAdapter<TipoAnulacion>(requireActivity(),android.R.layout.simple_list_item_1,list)

        listAnulaciones.adapter=adapter
        listAnulaciones.setOnItemClickListener { adapterView, view, i, l ->
            listener?.codigoSeleccionado(list[i].codeAnulacion)
            dismiss()
        }

    }

    fun onButtonPressed(code:Int) {
        listener?.codigoSeleccionado(code)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CodeAnulacion) {
            listener = context
        } else {
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface CodeAnulacion {
        fun codigoSeleccionado(code:Int)
    }

}
