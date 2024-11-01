package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_pedido.view.*

class RvAdapterPedidoskt(lista:ArrayList<mCabeceraPedido>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var lista=lista
    var listenerClickListener: ClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_pedido,parent,false)
        return ItemVh(view)

    }

    inner class ItemVh(item: View):RecyclerView.ViewHolder(item){
        val txtIdentificador=item.txtIdentificador
        val txtFecha=item.txtFecha
        val txtCliente=item.txtCliente
        val txtVendedor=item.txtVendedor
        val txtValor=item.txtValor
        val txtPagado=item.txtPagado
        val txtZonaServicio=item.txtZonaServicio
    }

    override fun getItemCount(): Int =lista.size

    fun Agregar(lista1:ArrayList<mCabeceraPedido>){
        lista=lista1
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as ItemVh
        h.txtIdentificador.text=lista.get(position).identificadorPedido
        if(lista.get(position).cliente.getcName().length>0){
            h.txtCliente.text=lista.get(position).cliente.getcName()
        }else{
            h.txtCliente.text="Cliente no especificado"
        }
        h.txtFecha.text=lista.get(position).fechaReserva
        if(lista.get(position).vendedor.primerNombre.length>0){
            h.txtVendedor.text=lista.get(position).vendedor.primerNombre
        }else{
            h.txtVendedor.text="Vendedor no especificado"
        }
        h.txtValor.text=Constantes.DivisaPorDefecto.SimboloDivisa+" "+lista.get(position).totalNeto.fortMoneda
        h.itemView.setOnClickListener {
            listenerClickListener?.clickPositionOption(h.adapterPosition,100.toByte())
        }
        if(lista.get(position).isbEstadoPagado()){
            h.txtPagado.setTextColor(Color.parseColor("#00c853"))

        }else{
            h.txtPagado.setTextColor(Color.parseColor("#e53935"))

        }
        h.txtPagado.text=lista.get(position).observacionReserva
        h.txtZonaServicio.setText(lista.get(position).observacionPedido.replace("\\n","\n"))
    }


}