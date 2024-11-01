package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResumenPedido
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_pedido_resumen.view.*

class RvAdaterResumenPedido: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val list=ArrayList<ResumenPedido>()
    var changeStateItem:ChangeStateItem?=null
    interface ChangeStateItem{

        fun itemChange(position:Int,state:Boolean)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_pedido_resumen,parent,false)
        return Item(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val h=holder as Item

        h.cbItem.isChecked=list[pos].marcado
        h.txtFecha.text=list[pos].fecha
        h.txtPlaca.text=list[pos].identificador
        h.txtProducto.text=list[pos].productoResumen

        h.cbItem.setOnCheckedChangeListener { buttonView, isChecked ->
            changeStateItem?.itemChange(pos,isChecked)
        }
    }

    fun AddItems(list:ArrayList<ResumenPedido>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =list.size

    inner class Item(item: View):RecyclerView.ViewHolder(item){
        val cbItem=item.cbItem
        val txtPlaca=item.txtPlaca
        val txtProducto=item.txtProducto
        val txtFecha=item.txtFecha
    }

}