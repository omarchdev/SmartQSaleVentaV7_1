package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ActivityOperarioEdit
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncOperarios
import com.omarchdev.smartqsale.smartqsaleventas.Model.OperarioSelect
import com.omarchdev.smartqsale.smartqsaleventas.Model.mOperario
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterSelectOperario
import kotlinx.android.synthetic.main.df_select_operarios.*

class DfSelectOperarios():DialogFragment(), RvAdapterSelectOperario.ActionsOperarioList {

    private var listenerOperariosSeleccionados:ListenerOperariosSeleccionados?=null
    private var lista=ArrayList<OperarioSelect>()
    private val adapterOperariosSeleccionados= RvAdapterSelectOperario(lista)
    private val asyncOperarios=AsyncOperarios()
    private var Operarios:ArrayList<mOperario>?=null
    interface ListenerOperariosSeleccionados{

        fun OperariosSelect(operarios:ArrayList<mOperario>)
    }

    fun newInstance(listaOperarios:ArrayList<mOperario>,listenerOperariosSeleccionados: ListenerOperariosSeleccionados):DfSelectOperarios{

        var f=DfSelectOperarios()
        f.Operarios=listaOperarios
        f.listenerOperariosSeleccionados=listenerOperariosSeleccionados
        return f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_select_operarios, container, false)

    }

    override fun onResume() {
        super.onResume()
        obtenerOperarios(edtNombreOperario.text.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnGuardar.setOnClickListener {
            listenerOperariosSeleccionados?.OperariosSelect(adapterOperariosSeleccionados.ObtenerOperariosSeleccionados())
            dialog?.dismiss()
        }
        btnCancelar.setOnClickListener {
            dialog?.dismiss()
        }
        btnAgregarOperario.setOnClickListener {
            var intent=Intent(context,ActivityOperarioEdit::class.java)
            intent.putExtra("idOperario",0)
            startActivity(intent)
        }
        edtNombreOperario.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                obtenerOperarios(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        rvSelectOperario.layoutManager = LinearLayoutManager(context)
        rvSelectOperario.adapter = adapterOperariosSeleccionados
        adapterOperariosSeleccionados.actionsOperarioList=this
        obtenerOperarios("")

    }

    fun obtenerOperarios(parametro:String){
        asyncOperarios.ObtenerOperarios(object : AsyncOperarios.ListenerObtenerOperarios {
            override fun OperariosObtenidos(listaOperarios: ArrayList<mOperario>) {
                lista.clear()
                listaOperarios.forEach {
                    lista.add(OperarioSelect(false, it))
                }
                Operarios?.forEachIndexed { i, mOperario ->
                    lista.forEachIndexed { i, it ->
                        if (mOperario.idOperario == it.operario.idOperario) {
                            it.Select = true
                        }
                    }
                }
                adapterOperariosSeleccionados.count = Operarios!!.size
                adapterOperariosSeleccionados.notifyDataSetChanged()
            }
        },parametro)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun clickEditOperario(idOperario: Int) {

        val intent= Intent(context, ActivityOperarioEdit::class.java)
        intent.putExtra("idOperario",idOperario)
        startActivity(intent)


    }
}