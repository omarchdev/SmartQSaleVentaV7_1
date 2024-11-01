package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_mesa_config.view.*

interface ListZonaServicio{
    fun clickZonaServicio(id:Int)
}

class RvAdapterSelectZonaServicioV2(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val lista:ArrayList<mZonaServicio> = ArrayList()
    lateinit var listZonaServicio:ListZonaServicio

    inner class itemVh(item: View) : RecyclerView.ViewHolder(item){
        val content=item.content
        val txtPrecio=item.txtPrecio
        val imgEdit=item.imgEdit
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_mesa_config,parent,false)
        return itemVh(v)
    }


    override fun getItemCount(): Int =lista.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as itemVh
        h.imgEdit.setOnClickListener {
            listZonaServicio.clickZonaServicio(lista.get(position).idZona)
        }
        h.txtPrecio.text="${lista.get(position).descripcion}"
        h.content.setBackgroundResource(R.drawable.fondo_rectangle_border_radius)
        h.txtPrecio.setTextColor(Color.WHITE)
    }
    fun pintar(){
        notifyDataSetChanged()
    }
    fun AgregarListaPrecios(lista:List<mZonaServicio>){
        this.lista.clear()
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }
}