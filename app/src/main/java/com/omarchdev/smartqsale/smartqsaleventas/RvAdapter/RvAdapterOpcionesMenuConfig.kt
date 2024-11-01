package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.IMenuItem
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_menu.view.*

interface IRvAdapterMenu{
    fun clickListener(position:Int,action:Int)
}

class RvAdapterOpcionesMenuConfig: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   private val items=ArrayList<IMenuItem>()

    var iRvAdapter:IRvAdapterMenu?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v= LayoutInflater.from(p0.context).inflate(R.layout.cv_item_menu,p0,false)
        return Item(v)
    }

    fun SetItems(items:ArrayList<IMenuItem>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val h=p0 as Item
        h.txtDescripcion.text=items[p1].DescripcionItem
        h.txtTitulo.text=items[p1].TituloItem
        h.itemView.setOnClickListener {
            iRvAdapter?.clickListener(p1,0)
        }
        when(items[p1].Id){
            1->{  h.imgItemMenu.setImageResource(R.drawable.ic_link_variant_white_36dp)}
            2->{  h.imgItemMenu.setImageResource(R.drawable.ic_whatsapp_white_36dp)}
            3->{  h.imgItemMenu.setImageResource(R.drawable.ic_calendar_today_white_36dp)}
            4->{  h.imgItemMenu.setImageResource(R.drawable.ic_calendar_clock_white_36dp)}
            5->{  h.imgItemMenu.setImageResource(R.drawable.ic_image_white_36dp)}
            6->{  h.imgItemMenu.setImageResource(R.drawable.ic_image_white_36dp)}
        }

    }
    inner class Item(item: View):RecyclerView.ViewHolder(item){
        val txtTitulo=item.txtTitulo
        val txtDescripcion=item.txtDescripcion
        val imgItemMenu=item.imgItemMenu

    }

}