package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncStockProductos
import com.omarchdev.smartqsale.smartqsaleventas.Model.AlmacenProducto
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterStockAlmacenes
import kotlinx.android.synthetic.main.df_stock_almacenes.*

class DfStockAlmacenes: DialogFragment(), AsyncStockProductos.ListenerStockAlmacenes {

    private var lista:ArrayList<AlmacenProducto>?=null
    private var idProducto=0
    private var nombre=""
    private var asyncStockAlmacenes= AsyncStockProductos()
    private var rvAdapterStockAlmacenes: RvAdapterStockAlmacenes?=null
    fun newInstance(idProducto:Int,nombre:String):DfStockAlmacenes{

        val f=DfStockAlmacenes()
        f.idProducto=idProducto
        f.lista= ArrayList()
        f.nombre=nombre
        return f

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.df_stock_almacenes, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTitulo.text=nombre
        rvAdapterStockAlmacenes= RvAdapterStockAlmacenes(lista!!)
        rvStockAlmacen.adapter=rvAdapterStockAlmacenes
        rvStockAlmacen.layoutManager=LinearLayoutManager(activity)
        asyncStockAlmacenes.ObtenerStockProductoAlmacenes(idProducto)
        asyncStockAlmacenes.listenerStockAlmacenes=this

            btnSalir.setOnClickListener {
                dialog?.dismiss()
        }


    }

    override fun ListadoStockAlmacenes(lista: ArrayList<AlmacenProducto>) {

        this.lista!!.clear()
        this.lista!!.addAll(lista)
        rvAdapterStockAlmacenes?.notifyDataSetChanged()
    }

}