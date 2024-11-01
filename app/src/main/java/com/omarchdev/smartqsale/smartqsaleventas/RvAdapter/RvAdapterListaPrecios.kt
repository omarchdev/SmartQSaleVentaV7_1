package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecios
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_lista_precio.view.*

interface ClickPositionRv{

    fun clickPosition(pos:Int,action:Int)

}

class RvAdapterListaPrecios : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listaPrecios = ArrayList<ListaPrecios>()
    var clickPositionRv:ClickPositionRv?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.cv_item_lista_precio, p0, false)
        return Item(v)
    }

    fun SetItemsLista(listaPrecios:ArrayList<ListaPrecios>){
        this.listaPrecios.clear()
        this.listaPrecios.addAll(listaPrecios)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listaPrecios.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

        val h=p0 as Item
        h.txtDescLista.text=listaPrecios[p1].descripcionLista
        h.itemView.setOnClickListener {
            clickPositionRv?.clickPosition(p1,0)
        }
    }

    inner class Item(item: View) : RecyclerView.ViewHolder(item) {

        val txtDescLista = item.txtDescLista

    }
}