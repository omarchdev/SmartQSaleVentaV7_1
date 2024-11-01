package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_producto_select_compra.view.*

class RvAdapterProductoCompra: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listProduct:ArrayList<mProduct>
    var visible:Boolean
    var esSalida:Boolean
    var esCVenta:Boolean



    init {
        listProduct= ArrayList()
        visible=true
        esSalida=false
        esCVenta=false
    }

    private  lateinit var listenerPositionProductoParaCompra:ListenerPositionProductoParaCompra

    fun setListenerPositionProductoParaCompra(listenerPositionProductoParaCompra:ListenerPositionProductoParaCompra){
        this.listenerPositionProductoParaCompra=listenerPositionProductoParaCompra
    }
    public interface ListenerPositionProductoParaCompra{
        public fun GetPositionProPos(position: Int,producto: mProduct)
    }

    fun getProductList():ArrayList<mProduct> = listProduct
    fun addProductListV2(productList:ArrayList<mProduct>){

        listProduct=productList

    }

    fun addProductList(productList:ArrayList<mProduct>){

        listProduct=productList
        notifyDataSetChanged()
    }

    fun actualizarLista(){

        notifyDataSetChanged()
    }
    inner class ItemVH(item: View):RecyclerView.ViewHolder(item){

        var txtNombreProducto=item.txtNombreProducto
        var txtTotal=item.txtTotal
        var btnEliminar=item.btnEliminar

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_producto_select_compra,parent,false)
        return ItemVH(v)
    }
    fun editableList(editable:Boolean){
        this.visible=editable
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int=listProduct.size

    fun AgregarProductosSeleccionados(product: mProduct){
        listProduct.add(product)
        notifyDataSetChanged()
    }

    fun LimpiarLista(){

        listProduct.clear()
        notifyDataSetChanged()

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as ItemVH
        h.txtNombreProducto.setText("${listProduct.get(position).getcProductName()}")
        if(!esSalida) {
            h.txtTotal.setText(String.format("%.2f", listProduct.get(position).getdQuantity()) +
                    "x" +
                    Constantes.DivisaPorDefecto.SimboloDivisa +
                    String.format("%.2f", listProduct.get(position).precioCompra) +
                    "=" +
                    Constantes.DivisaPorDefecto.SimboloDivisa +
                    String.format("%.2f", listProduct.get(position).precioCompra.multiply(listProduct.get(position).getdQuantity().toBigDecimal()))
            )
        }else{

            h.txtTotal.setText(String.format("%.2f", listProduct.get(position).getdQuantity().toBigDecimal()))
        }



        h.btnEliminar.setOnClickListener {
            listenerPositionProductoParaCompra.GetPositionProPos(h.adapterPosition,listProduct.get(h.adapterPosition))
        }
        when(visible){

            false->h.btnEliminar.visibility=View.INVISIBLE

        }
    }
    fun eliminarProductoPosition(position: Int){
        listProduct.removeAt(position)
        notifyItemRemoved(position)
    }
}










