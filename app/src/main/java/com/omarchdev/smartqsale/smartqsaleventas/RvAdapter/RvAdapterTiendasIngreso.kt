package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_cliente.view.*
import kotlinx.android.synthetic.main.cv_tienda.view.*

class RvAdapterTiendasIngreso(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface PositionClick1{
        fun ObtenerTienda(id:Int)
    }
    var tipoVista=1
    var positionClick:PositionClick1?=null
    val imagenesController=ImagenesController()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(tipoVista==1) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.cv_tienda, parent, false)
            return ItemVh(v)
        }else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_cliente, parent, false)
            return ItemVh2(v)
        }

   }


    private class ItemVh(item:View):RecyclerView.ViewHolder(item){

        val cv_item=item.cv_item
        val txtNombreTienda=item.txtNombreTienda
    }

    private class ItemVh2(item:View):RecyclerView.ViewHolder(item){

        val imgCliente=item.imgCliente
        val txtNombreCliente=item.txtNombreCliente
        val btnSetting=item.btnSetting
        val btnEdit=item.btnEdit
        val txtEmailCliente=item.txtEmailCliente
        val txtNumDocumento=item.txtNumDocumento
    }
    override fun getItemCount(): Int= Constantes.Tiendas.tiendaList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(tipoVista==1){
        val h=holder as ItemVh
        h.cv_item.setOnClickListener {
            positionClick?.ObtenerTienda(Constantes.Tiendas.tiendaList.get(position).idTienda)
        }
        h.txtNombreTienda.text="${Constantes.Tiendas.tiendaList.get(position).nombreTienda}"
        }else if(tipoVista==2){

            val h=holder as ItemVh2
            h.imgCliente.setImageBitmap(imagenesController.textAsBitmap(Constantes.Tiendas.tiendaList.get(position).nombreTienda))
            h.txtNombreCliente.text=Constantes.Tiendas.tiendaList.get(position).nombreTienda
            h.txtNumDocumento.visibility=View.GONE
            h.txtEmailCliente.visibility=View.GONE
            h.btnEdit.setOnClickListener {
                positionClick?.ObtenerTienda(Constantes.Tiendas.tiendaList.get(position).idTienda)
            }
            h.btnSetting.visibility=View.GONE
        }
    }

}