package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraVenta
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_pedido_venta.view.*

class RvAdapterVentasZonaServicio(lista:ArrayList<mCabeceraVenta>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val lista=lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v=LayoutInflater.from(parent.context).
                inflate(R.layout.cv_item_pedido_venta,parent,false)
        return ItemVh(v)
    }


    inner class ItemVh(item: View):RecyclerView.ViewHolder(item){
        val txtFecha=item.txtFecha
        val txtDescServicio=item.txtDescServicio
        val txtPrecio=item.txtPrecio
    }

    override fun getItemCount(): Int=lista.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val h=holder as ItemVh
        h.txtFecha.setText(lista.get(position).fechaVenta)
        h.txtDescServicio.setText(lista.get(position).
                descripcionVenta.replace(Constantes.Etiquetas.SaltoLinea,"\n"))
        h.txtPrecio.setText(Constantes.SimboloMoneda.moneda+lista[position].totalPagado.fortMoneda)

    }

}