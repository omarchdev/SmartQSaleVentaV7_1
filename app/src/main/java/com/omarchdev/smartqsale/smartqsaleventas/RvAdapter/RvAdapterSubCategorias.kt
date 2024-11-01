package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSubCategoria
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_sub_categoria.view.*


class RvAdapterSubCategorias(listaSubCategorias:ArrayList<mSubCategoria>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listaSubCategorias=listaSubCategorias

    var clickListener: ClickListener?=null

    private class ItemVhSubCategoria(item: View):RecyclerView.ViewHolder(item){
        val txtDescripcionSubCategoria=item.txtDescripcionSubCategoria
        val imgDelete=item.imgDelete
        val imgEdit=item.imgEdit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_sub_categoria,parent,false)
        return ItemVhSubCategoria(v)

    }

    override fun getItemCount(): Int=listaSubCategorias.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {

        var h=holder as ItemVhSubCategoria

        h.txtDescripcionSubCategoria.text=listaSubCategorias.get(i).descripcionSubCategoria.trim()
        h.imgDelete.setOnClickListener {
            clickListener?.clickPositionOption(h.adapterPosition,100.toByte())
        }
        h.imgEdit.setOnClickListener {
            clickListener?.clickPositionOption(h.adapterPosition,101.toByte())
        }

    }
}