package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fUnidades
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.AlmacenProducto
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_stock_almacen.view.*


class RvAdapterStockAlmacenes(lista:ArrayList<AlmacenProducto>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val lista=lista

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v=LayoutInflater.from(parent.context).inflate(R.layout.cv_stock_almacen,parent,false)
        return ItemVh(v)
    }

    inner class ItemVh(item:View):RecyclerView.ViewHolder(item){

        val txtAlmacen=item.txtAlmacen
        val txtStock=item.txtStock

    }

    override fun getItemCount(): Int =lista.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {

        val h=holder as ItemVh

        h.txtAlmacen.text="${lista.get(i).almacen.descripcionAlmacen} " +
                "(${ObtenerNombreTienda(lista.get(i).almacen.idTienda)})"
        h.txtStock.text=lista.get(i).producto.getdQuantity().fUnidades

    }

}