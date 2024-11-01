package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_ventas_vendedor.view.*

class RvAdapterVentasProducto(lista:List<mProduct>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val lista:List<mProduct>?=lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cv_ventas_vendedor, parent, false)
        return ItemVH(v)
    }
    inner class ItemVH(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtNombre=itemView.txtNombre
        val txtMonto=itemView.txtMonto
        val txtNumVentas=itemView.txtNumVentas
    }
    override fun getItemCount(): Int=lista!!.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p: Int) {
        val h = holder as ItemVH
        h.txtNombre.setText("${lista!!.get(p).descripcionCategoria.trim()}/${lista.get(p).descripcionSubCategoria.trim()}/${lista!!.get(p).getcProductName().trim()}/${lista!!.get(p).descripcionVariante.trim()}")
        h.txtMonto.setText("Total ventas:${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f", lista.get(p).precioVenta)}")
        h.txtNumVentas.setText("Unidades Vendidas:${String.format("%.2f",lista.get(p).getdQuantity())}")
    }
}