package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.EstadoEntregaPedidoEnUso
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_estado_pedido.view.*

class RvAdapterEstadoPedido: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var iEstadoPorPedidoList:IEstadoPorPedidoList?=null
    interface IEstadoPorPedidoList{
        fun GetPostionPedidoClick(position:Int,marcado:Boolean,descripcion:String)
    }
    private val list=ArrayList<EstadoEntregaPedidoEnUso>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        var v= LayoutInflater.from(p0.context).inflate(R.layout.cv_item_estado_pedido,p0,false)
        return EstadoPedidoVH(v)
    }

    fun Refresh(){
        notifyDataSetChanged()
    }

    fun setItems(listData:ArrayList<EstadoEntregaPedidoEnUso>){

        list.addAll(listData)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return  list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        val h=holder as EstadoPedidoVH
        try{

            h.txtTituloEstado.setText(list.get(p1).cDescripcionEstado)
            h.txtSubtituloEstado.setText(list.get(p1).cDescripcionAdicionalEstado)
            h.cbEstado.isChecked=list.get(p1).bMarcado

            h.cbEstado.setOnCheckedChangeListener { buttonView, isChecked ->
                iEstadoPorPedidoList?.GetPostionPedidoClick(p1,h.cbEstado.isChecked,list.get(p1).cDescripcionEstado)
            }
            /*h.cbEstado.setOnClickListener {
                h.cbEstado.isChecked=!h.cbEstado.isChecked
                iEstadoPorPedidoList?.GetPostionPedidoClick(p1,!h.cbEstado.isChecked)

            }*/
            h.itemView.setOnClickListener {
                h.cbEstado.isChecked=!h.cbEstado.isChecked
            }

        }catch (e:Exception){
            Log.d("Error",e.toString())
        }


    }
    inner class EstadoPedidoVH(item: View):RecyclerView.ViewHolder(item){
        val cbEstado=item.cbEstado
        val txtTituloEstado=item.txtTituloEstado
        val txtSubtituloEstado=item.txtSubtituloEstado
        val clEstadoPedido=item.clEstadoPedido
    }
}