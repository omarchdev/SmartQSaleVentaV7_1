package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRol
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_cliente.view.*

class RvAdapterRoles(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val rolUsuarios=ArrayList<mRol>()
    val imagenesController=ImagenesController()
     private var interfaceListaRoles: InterfaceListaRoles?=null

    fun setInterfaceListaRoles(interfaceListaRoles: InterfaceListaRoles){
        this.interfaceListaRoles=interfaceListaRoles
    }

    public interface InterfaceListaRoles{

        fun ObtenerPosicion(position: Int)

    }

    internal inner class ItemVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtNombreCliente=itemView.txtNombreCliente
        val imgCliente=itemView.imgCliente
        val btnSetting=itemView.btnSetting
        val btnEdit=itemView.btnEdit
        val txtNumDocumento=itemView.txtNumDocumento
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_cliente,parent,false)
        return ItemVH(v)
    }

    override fun getItemCount(): Int =rolUsuarios.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val h=holder as ItemVH
        h.txtNumDocumento.visibility=View.GONE
        h.btnEdit.visibility=View.GONE
        h.btnSetting.setOnClickListener{
            if(interfaceListaRoles!=null) {
                interfaceListaRoles?.ObtenerPosicion(h.adapterPosition)
            }
        }

            h.txtNombreCliente.text=rolUsuarios.get(position).getcDescripcion()
            h.imgCliente.setImageBitmap(imagenesController.textAsBitmap(rolUsuarios.get(position).getcDescripcion()))

    }

    fun agregarElementos(mRols:ArrayList<mRol>){
        this.rolUsuarios.clear()
        this.rolUsuarios.addAll(mRols)
        notifyDataSetChanged()
    }
}