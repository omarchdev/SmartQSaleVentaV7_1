package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Model.mOperario
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_operario_pedido.view.*

class RvAdapterOperarioPedido(listaOperario:ArrayList<mOperario>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val listaOperario=listaOperario
    var clickListener: ClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_operario_pedido,parent,false)
        return ItemOperarioPedido(view)
    }

    inner class ItemOperarioPedido(item: View):RecyclerView.ViewHolder(item){
        val txtOperario=item.txtOperario
        val imgDelete=item.imgDelete
    }
    override fun getItemCount(): Int =listaOperario.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val h=holder as ItemOperarioPedido
        h.txtOperario.setText(listaOperario.get(i).ApellidoPaterno
                +" "+listaOperario.get(i).ApellidoPaterno+" , "+
                listaOperario.get(i).PrimerNombre+" "+
        listaOperario.get(i).SegundoNombre)
        h.imgDelete.setOnClickListener {
            clickListener?.clickPositionOption(h.adapterPosition,100.toByte())
        }
    }
}