package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_area_produccion.view.*

class RvAdapterAreasProduccion(areasProduccion:ArrayList<mAreaProduccion>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val areasProduccion=areasProduccion
    val imageController=ImagenesController()
    var clickListener: ClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_area_produccion,parent,false)
        return ItemVH(v)
    }



    private class ItemVH(item: View): RecyclerView.ViewHolder(item){
        val txtDescripcion=item.txtDescripcion
        val imgArea=item.imgArea
        val imgSetting=item.imgSetting
    }
    override fun getItemCount(): Int =areasProduccion.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val h=holder as ItemVH
            h.txtDescripcion.text=areasProduccion.get(position).cDescripcionArea
            h.imgArea.setImageBitmap(imageController.textAsBitmap(areasProduccion.get(position).cDescripcionArea))
            h.imgSetting.setOnClickListener {
                clickListener?.clickPositionOption(h.adapterPosition,100)

            }
    }



}