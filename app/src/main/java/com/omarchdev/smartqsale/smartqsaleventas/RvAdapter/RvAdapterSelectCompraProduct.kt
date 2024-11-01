package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes

import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_producto.view.*

class RvAdapterSelectCompraProduct: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listaProducto:ArrayList<mProduct>
    var visible:Boolean
    var Salida:Boolean
   init{
        listaProducto=ArrayList()
       visible=true
       Salida=false

    }
    private lateinit var listenerPosition:ListenerPosition
    fun setListenerPosition(listenerPosition:ListenerPosition){
        this.listenerPosition=listenerPosition
    }
    public interface ListenerPosition{
        fun positionSelectProductAlmacen(position:Int)
    }
    fun setEsSalida(esSalida:Boolean){
        this.Salida=esSalida
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder{
       var v=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_producto,parent,false)
       return ItemVH(v)
    }
    internal inner class ItemVH(itemView:View): RecyclerView.ViewHolder(itemView) {

        val txtNombre=itemView.txtNombreProducto
        val txtPrecioCompra=itemView.txtPrecioCompra

    }

    fun AgregarProductos(listaProducto:ArrayList<mProduct>){

        this.listaProducto.clear()
        this.listaProducto.addAll(listaProducto)
        notifyDataSetChanged()
    }

    fun EliminarProductos(){

    }

    override fun getItemCount(): Int =listaProducto.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as ItemVH
        h.txtNombre.setText("${listaProducto.get(position).getcKey()} \n${listaProducto.get(position).descripcionCategoria}\n" +
                "${listaProducto.get(position).getcProductName()}")

        if(!Salida) {
            h.txtPrecioCompra.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", listaProducto.get(position).precioCompra))
        }
        else{
            h.txtPrecioCompra.setText(String.format("%.2f",listaProducto.get(position).getdQuantity()))
        }
        h.itemView.setOnClickListener {
            listenerPosition.positionSelectProductAlmacen(h.adapterPosition)

        }
    }
}