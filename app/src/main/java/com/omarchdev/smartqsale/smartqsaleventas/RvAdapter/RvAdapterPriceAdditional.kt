package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_precio_adicional.view.*

class RvAdapterPriceAdditional(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val lista:ArrayList<String>
    var posPrevious:Int=-1
    var posActual:Int=-1
    var heigth=1
    init{
          lista= ArrayList()
        posPrevious=-1
        posActual=0
    }



    inner class itemVh(item: View) :RecyclerView.ViewHolder(item){
        val txtPrecio=item.txtPrecio
        val rlContent=item.rlContent
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_precio_adicional,parent,false)
        return itemVh(v)
    }

    override fun getItemCount(): Int =lista.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as itemVh
        h.txtPrecio.text="${lista.get(position)}"
        h.itemView.setOnClickListener {
            posPrevious=posActual
            posActual=h.adapterPosition
            pintar()
        }

        if(position==posActual){
            h.rlContent.setBackgroundResource(R.drawable.fondo_rectangle_border_radius)
            h.txtPrecio.setTextColor(Color.WHITE)
        }
        else{
            h.rlContent.setBackgroundResource(R.drawable.fondo_img)
            h.txtPrecio.setTextColor(Color.GRAY)
        }

    }



    fun pintar(){
        notifyDataSetChanged()
    }

    fun AgregarListaPrecios(lista:List<String>){
        this.lista.clear()
        this.lista.addAll(lista)
        notifyDataSetChanged()
        posActual=0
    }

}