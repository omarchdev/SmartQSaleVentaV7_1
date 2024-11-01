package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.df_edit_product_tiempo_pedido.*
import kotlinx.android.synthetic.main.df_edit_product_tiempo_pedido.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class DfEditProductTiempoPedido():DialogFragment(){

    private var product=ProductoEnVenta()
    private var listenerEditarProductoTiempoPedido:ListenerEditarProductoTiempoPedido?=null
    private var permitir=false
    interface ListenerEditarProductoTiempoPedido{

        fun EditarProductoTiempoPedido(product:ProductoEnVenta,horaFinal:String)

    }
    fun newInstance(product:ProductoEnVenta):DfEditProductTiempoPedido{

        var f=DfEditProductTiempoPedido()
        f.product=product
        return f
    }

    fun listener(listenerEditarProductoTiempoPedido:ListenerEditarProductoTiempoPedido):DfEditProductTiempoPedido{
        this.listenerEditarProductoTiempoPedido=listenerEditarProductoTiempoPedido
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_edit_product_tiempo_pedido,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtNombreProducto.setText((product.descripcionCategoria.trim()+" "
                +product.descripcionSubCategoria.trim()+" " +
                ""+product.productName.trim()).trim())
        permitir=true
        txtHoras.setText("${String.format("%.0f",product.cantidad)}")
        if(!product.horaInicio.equals("NN")){

            edtHoraInicial.setText(product.horaInicio)

        }else{
            edtHoraInicial.setText("")

        }

        if(!product.horaFinal.equals("NN")){
            edtHoraFinal.setText(product.horaFinal)
            btnClock2.visibility=View.GONE
            btnGuardar.visibility=View.GONE
        }else{
            edtHoraFinal.setText("")
        }

        btnClock2.setOnClickListener {
            GlobalScope.launch {
                var t= BdConnectionSql.getSinglentonInstance().ObtenerFechaFinalProducto(product.horaInicio)
               launch(Dispatchers.Main){
                    edtHoraFinal.setText(t.horaFinal)
                    txtHoras.setText("${String.format("%.0f",t.cantidad)}")
                    if(t.cantidad<1){
                        permitir=false
                    }else{
                        permitir= true
                    }
                }
            }

        }
        btnGuardar.setOnClickListener {

            if(!edtHoraFinal.text.toString().isEmpty()) {
                if(permitir) {
                    AlertDialog.Builder(context).setTitle("Advertencia")
                            .setMessage("¿Está seguro de registrar como hora final a las "
                                    + edtHoraFinal.edtHoraFinal.text.toString() + " ?")
                            .setPositiveButton("Aceptar", { dialog, which ->
                                listenerEditarProductoTiempoPedido?.EditarProductoTiempoPedido(product, edtHoraFinal.edtHoraFinal.text.toString())
                                this.dismiss()
                            }).setNegativeButton("Cancelar", null).create().show()
                }
            }
        }
        btnSalir.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}