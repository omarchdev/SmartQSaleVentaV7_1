package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasVendedor
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_ventas_vendedor.view.*

class RvAdapterVentasVendedor(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val lista:ArrayList<mVentasVendedor>
    init {
        lista= ArrayList()
    }
    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cv_ventas_vendedor, parent, false)
        return ItemVH(v)
    }
    inner class ItemVH(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtNombre=itemView.txtNombre
        val txtMonto=itemView.txtMonto
        val txtNumVentas=itemView.txtNumVentas
    }
    override fun getItemCount(): Int =lista.size
    fun AgregarLista(lista:ArrayList<mVentasVendedor>){
        this.lista.clear()
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ItemVH
        h.txtNombre.text = "${lista.get(position).vendedor.primerNombre} ${lista.get(position).vendedor.apellidoPaterno} ${lista.get(position).vendedor.apellidoMaterno}"
        h.txtMonto.text = "Total Ventas:${Constantes.DivisaPorDefecto.SimboloDivisa} ${String.format("%.2f", lista.get(position).montoVentas)}"
        h.txtNumVentas.text="Num. Ventas:${lista.get(position).numeroVentas}"
    }

}