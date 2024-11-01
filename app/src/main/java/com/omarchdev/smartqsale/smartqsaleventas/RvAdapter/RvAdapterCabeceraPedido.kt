package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_cabecera_pedido.view.*

class RvAdapterCabeceraPedido(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listaPedidos:ArrayList<mCabeceraPedido>?=null
    var clickListener: ClickListener?=null

    inner class ItemView(item: View):RecyclerView.ViewHolder(item){
        val txtNombrePedido=item.txtNombrePedido;
        val txtCliente=item.txtCliente
        val txtVendedor=item.txtVendedor
        val txtfechaPedido=item.txtfechaPedido
        val btnGetPedido=item.btnGetPedido
        val btnDelete=item.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_cabecera_pedido,parent,false)
        return ItemView(v)
    }

    override fun getItemCount(): Int =listaPedidos!!.size


    fun addElements(){
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        val h=holder as ItemView


        h.txtCliente.text="Cliente: ${listaPedidos!!.get(pos).cliente.getcName()} ${listaPedidos!!.get(pos).cliente.getcApellidoPaterno()} ${listaPedidos!!.get(pos).cliente.getcApellidoMaterno()}"
        h.txtfechaPedido.text="${listaPedidos!!.get(pos).fecha}"
        h.txtNombrePedido.text="${listaPedidos!!.get(pos).identificadorPedido}"
        h.txtVendedor.text="Vendedor: ${listaPedidos!!.get(pos).vendedor.primerNombre} ${listaPedidos!!.get(pos).vendedor.apellidoPaterno} ${listaPedidos!!.get(pos).vendedor.apellidoMaterno}"
        h.btnDelete.setOnClickListener {
            clickListener!!.clickPositionOption(h.adapterPosition,50)
        }
        h.btnGetPedido.setOnClickListener {
            clickListener!!.clickPositionOption(h.adapterPosition,60)

        }

    }

}