package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTransaccionAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_tipo_transaccion.view.*

class RvAdapterTiposTransaccion: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list:ArrayList<mTransaccionAlmacen>
    private lateinit var listenerTransaccion:ListenerTransaccion

    fun setListenerTransaccion(listenerTransaccion:ListenerTransaccion){
        this.listenerTransaccion=listenerTransaccion
    }

    public interface ListenerTransaccion{

        public fun ObtenerCodigoTransaccion(codigo:String)

    }

    init {
        list= ArrayList()
    }

    internal inner class ItemVh(itemView:View): RecyclerView.ViewHolder(itemView) {
        val txtTitulo=itemView.txtTitulo
        val txtSubtitulo=itemView.txtSubtitulo

    }

    fun AgregarTransacciones(list:ArrayList<mTransaccionAlmacen>){
        this.list=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_tipo_transaccion,parent,false)
        return ItemVh(v)

    }

    override fun getItemCount(): Int=list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val h=holder as ItemVh
        h.txtTitulo.setText("${list.get(position).nombre}")
        h.txtSubtitulo.setText("${list.get(position).descripcion}")
        h.itemView.setOnClickListener {
            if(listenerTransaccion!=null) {
                listenerTransaccion.ObtenerCodigoTransaccion(list.get(position).codigo)
            }
        }


    }
}