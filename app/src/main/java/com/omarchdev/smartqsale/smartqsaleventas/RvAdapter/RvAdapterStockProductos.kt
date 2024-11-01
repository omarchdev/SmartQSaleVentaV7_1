package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fUnidades
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_producto_stock.view.*

class RvAdapterStockProductos(lista:ArrayList<mProduct>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var lista=lista

    var clickListener: ClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var v=LayoutInflater.from(parent.context).inflate(
            R.layout.cv_item_producto_stock,parent,false)
        return ItemVh(v)
    }


    inner class ItemVh(item: View):RecyclerView.ViewHolder(item){

        val txtCodProducto=item.txtCodProduct
        val txtNombreProducto=item.txtNameProduct
        val txtStockProduct=item.txtStockProduct
        val btnOption=item.btnOption

    }

    override fun getItemCount(): Int=lista.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {

        var h=holder as ItemVh

        h.txtCodProducto.text=lista.get(i).getcKey()
        h.txtNombreProducto.text=lista.get(i).descripcionCategoria+" "+lista.get(i).getcProductName()+" "+lista.get(i).descripcionVariante
        h.txtStockProduct.text="Stock disponible :"+lista.get(i).getdQuantity().fUnidades
        h.btnOption.setOnClickListener {
            clickListener?.clickPositionOption(h.adapterPosition,100.toByte())
        }
    }
}