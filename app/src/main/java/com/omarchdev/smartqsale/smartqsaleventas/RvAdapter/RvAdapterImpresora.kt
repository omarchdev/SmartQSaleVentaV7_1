package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.mImpresora
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_impresora.view.*

class RvAdapterImpresora(listaImpresora:List<mImpresora>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var lista=listaImpresora
    var clickPosition: ClickPosition?=null
    interface ClickPosition{

        fun positionClick(position:Int)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_impresora,parent,false)
        return Item(v)

    }

    inner class Item(item: View):RecyclerView.ViewHolder(item){
        val txtDescripcion=item.txtDescripcion
        val txtTienda=item.txtTienda
        val txtArea=item.txtArea
        val imgSetting=item.imgSetting
    }

    override fun getItemCount(): Int=lista.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as Item
        h.txtDescripcion.text=lista.get(position).nombreImpresora
        h.txtTienda.text= ObtenerNombreTienda(lista.get(position).idTienda)
        h.txtArea.text=lista.get(position).nombreArea
        h.itemView.setOnClickListener {
            clickPosition?.positionClick(h.adapterPosition)

        }
        h.imgSetting.visibility=View.GONE
      /*  h.imgSetting.setOnClickListener {
            clickPosition?.positionClick(h.adapterPosition)
        }
    */}

}