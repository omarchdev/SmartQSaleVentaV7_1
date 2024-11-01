package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.util.Log
import android.view.*
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.df_producto_control_tiempo_nuevo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DfProductoControlTiempoPedido():DialogFragment(){


    private lateinit var p: mProduct
    private var permitir=false
    private var listenerGuardarProduct:ListenerGuardarProductoTiempoPedido?=null
    var tiempoTemp=""
    interface ListenerGuardarProductoTiempoPedido{

        fun GuardarProductoTiempoPedido(p:mProduct,horaInicio:String)

    }
    fun newInstance(p:mProduct):DfProductoControlTiempoPedido{
        var f=DfProductoControlTiempoPedido()
        f.p=p
        return f
    }

    fun f(listenerGuardarProductoTiempoPedido: ListenerGuardarProductoTiempoPedido):DfProductoControlTiempoPedido{
        this.listenerGuardarProduct=listenerGuardarProductoTiempoPedido
        return this
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_producto_control_tiempo_nuevo,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permitir=false
        txtTitulo.setText((p.descripcionCategoria+" "+p.descripcionSubCategoria.trim()+" "+p.getcProductName()).trim())
         GlobalScope.launch  {
            val t=BdConnectionSql.getSinglentonInstance().ObtenerHoraActual()
           launch(Dispatchers.Main){
                edtHoraInicio.setText(t.fecha+" "+t.hora)
                permitir=true
            }
        }
        btnSalir.setOnClickListener {
            this.dismiss()
        }
        btnGuardar.setOnClickListener {
            if(permitir){
                 GlobalScope.launch {
                    val t=BdConnectionSql.getSinglentonInstance().ObtenerHoraActual()
                    tiempoTemp=t.fecha+" "+t.hora
                   launch(Dispatchers.Main){
                        try{
                            //edtHoraInicio.setText(t.fecha+" "+t.hora)
                            listenerGuardarProduct?.GuardarProductoTiempoPedido(p,tiempoTemp)
                        }catch (ex:Exception){
                            Log.d("d1-t",ex.toString())
                        }

                    }
                }

                this.dismiss()
            }
        }
    }

}