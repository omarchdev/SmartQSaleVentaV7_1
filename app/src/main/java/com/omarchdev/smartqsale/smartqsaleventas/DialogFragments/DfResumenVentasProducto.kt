package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVentasProducto
import kotlinx.android.synthetic.main.fragment_resumen_ventas_producto.*

class DfResumenVentasProducto():DialogFragment(){



    val lista=ArrayList<mProduct>()
    val adapterReporteVentas= RvAdapterVentasProducto(lista)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvVentasProducto.layoutManager=LinearLayoutManager(activity)
        rvVentasProducto.adapter=adapterReporteVentas
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resumen_ventas_producto, container, false)
    }

    fun newInstance():DfResumenVentasProducto {
        val d=DfResumenVentasProducto()

        return d
    }

    fun AgregarProductosLista(productList: List<mProduct>){
            lista.clear()

            lista.addAll(productList)
            adapterReporteVentas.notifyDataSetChanged()

    }

    fun ErrorConsulta(){

    }
}