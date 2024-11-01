package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments


import android.app.Dialog
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCarSale
import android.media.MediaPlayer
import android.text.InputType
import android.view.KeyEvent
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedido
import kotlinx.android.synthetic.main.fragment_df_venta_rapida.*

import android.app.AlertDialog
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class df_venta_rapida : DialogFragment(), View.OnKeyListener, AsyncPedido.ResultadoBusquedaProductoCodigoBarra {


    override fun ErrorBusquedaProducto(str: String) {

        edtBarCode.isFocusable=true
        permitirBusqueda = true
        pbCargar.visibility=View.GONE
        ultimoCodigo=""
        AlertDialog.Builder(activity).setMessage(str).setPositiveButton("Salir",null).create().show()
    }


    override fun ExisteProducto(productoEnVenta: ProductoEnVenta) {

        pbCargar.visibility = View.GONE
        listenerAgregarProductoDetallePedido?.AgregarProductoDetallePedido(productoEnVenta)
        if (productoEnVenta.cantidad > 0.toFloat()) {
            if (productoEnVenta.metodoGuardar.equals("N")) {
                this.listProductos.add(productoEnVenta)
            } else if (productoEnVenta.metodoGuardar.equals("M")) {
                val arrayList = this.listProductos
                val obj = arrayList[arrayList.size - 1]
                obj.cantidad = productoEnVenta.cantidad
            }
        }
        adapterCarSale?.notifyDataSetChanged()
        this.permitirBusqueda = true

        edtBarCode.isFocusable = true
        edtBarCode.isFocusable=true
        edtBarCode.requestFocus()
        edtBarCode.setText("")

    }


    var adapterCarSale: RvAdapterCarSale? = null
    var idCabeceraPedido: Int = 0
    var listProductos = ArrayList<ProductoEnVenta>()
    var listenerAgregarProductoDetallePedido: ListenerAgregarProductoDetallePedido? = null
    var mp: MediaPlayer? = null
    var permitirBusqueda = true
    var ultimoCodigo = ""
    var asyncPedido: AsyncPedido?=null
    var code=""
    var i=0
    public interface ListenerAgregarProductoDetallePedido {
        fun AgregarProductoDetallePedido( productoEnVenta: ProductoEnVenta)
    }

    fun newInstance(idCabeceraPedido: Int): df_venta_rapida {
        val f = df_venta_rapida()
        f.idCabeceraPedido = idCabeceraPedido
        return f
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_df_venta_rapida, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setLayout(-1, -1)
        dialog.window!!.requestFeature(1)
        dialog.setCancelable(false)
        dialog.cancel()
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapterCarSale = RvAdapterCarSale(this.listProductos)
        adapterCarSale!!.PermitirOpciones(false)
        edtBarCode.setFocusable(true)
        edtBarCode.setInputType(InputType.TYPE_NULL)
        btnSalir.setFocusable(false)
        rvProductos.adapter=adapterCarSale
        asyncPedido= AsyncPedido(requireContext())
        pbCargar.setVisibility(View.GONE)
       asyncPedido!!.resultadoBusquedaProductoCodigoBarra=this
        btnSalir.setOnClickListener {
            dialog!!.dismiss()
        }
        edtBarCode.setOnKeyListener(this)
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 66) {
            i=0
             i++
            if (i % 2 == 1) {

                edtBarCode.isFocusable = true
                txtCodigoBarra.text =  edtBarCode.text.toString()
                txtCodigoBarra.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.abc_popup_enter))
                if ((if ((edtBarCode.text.toString() as CharSequence).length == 0) 1 else null) == null) {

                    if (!ultimoCodigo.equals(edtBarCode.text.toString())) {
                             pbCargar.visibility = View.VISIBLE
                        asyncPedido?.BuscarProductoCodigoBarra(edtBarCode.text.toString(),
                                idCabeceraPedido, 0,
                                0, "N")///VERIFICAR
                        permitirBusqueda = false
                    } else if (!listProductos.isEmpty()) {

                        pbCargar.visibility = View.VISIBLE
                        val str = edtBarCode.text.toString()
                        var obj = listProductos.get(listProductos.size - 1)
                        val idDetallePedido = (obj as ProductoEnVenta).idDetallePedido
                        obj = listProductos.get(listProductos.size - 1)
                        /*
                        *
                        * */
                        asyncPedido?.BuscarProductoCodigoBarra(edtBarCode.text.toString(),
                                idCabeceraPedido, 0,
                                0, "N")///VERIFICAR
                      /*  asyncPedido?.BuscarProductoCodigoBarra(str, idCabeceraPedido,
                                idDetallePedido, listProductos.get(listProductos.size - 1).idProducto,
                                "M"*/
                        /*
                        asyncPedido?.BuscarProductoCodigoBarra(edtBarCode.text.toString(),
                                idCabeceraPedido, 0,
                                0, "N")*/
                        ///VERIFICAR
                        permitirBusqueda = false
                    }
                   ultimoCodigo =edtBarCode.text.toString()
                    Toast.makeText(context,ultimoCodigo,Toast.LENGTH_SHORT).show()
                }
               edtBarCode.setText("")

            }
        }
         return false


    }
    fun SetListenerAgregarProductoDetallePedido(listenerAgregarProductoDetallePedido: ListenerAgregarProductoDetallePedido): df_venta_rapida {

        this.listenerAgregarProductoDetallePedido = listenerAgregarProductoDetallePedido
        return this
    }
}
