package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_tranferencia_almacen.view.*

class RvAdapterTransferencia: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listaMov:ArrayList<mMovAlmacen>
    private lateinit var listenerTrasferenciasAlmacen:ListenerTrasferenciasAlmacen
    init {
        listaMov= ArrayList()
    }

    public fun setListenerTrasferenciasAlmacen(listenerTrasferenciasAlmacen:ListenerTrasferenciasAlmacen){
        this.listenerTrasferenciasAlmacen=listenerTrasferenciasAlmacen
    }

    public interface ListenerTrasferenciasAlmacen{

        fun obtenerTransferenciaAlmacen(mMovAlmacen: mMovAlmacen)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_tranferencia_almacen,parent,false)
        return ItemVH(v)
    }
        internal inner class ItemVH(itemView:View): RecyclerView.ViewHolder(itemView) {
        val txtNumTransferencia=itemView.txtNumTransferencia
        val txtAlmacenOrigen=itemView.txtAlmacenOrigen
        val txtDescripcion=itemView.txtDescripcion
        val txtFechaMov=itemView.txtFechaMov
    }

    override fun getItemCount(): Int =listaMov.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as ItemVH
        h.txtNumTransferencia.setText("${listaMov.get(position).idMovAlmacen}")
        h.txtAlmacenOrigen.setText("Almacen Origen: ${listaMov.get(position).descAlmacenI}")
        h.txtFechaMov.setText("Fecha Transferencia: ${listaMov.get(position).fechaMov}")
        h.txtDescripcion.setText("${listaMov.get(position).descripcionMov}")
        h.itemView.setOnClickListener{
            listenerTrasferenciasAlmacen.obtenerTransferenciaAlmacen(listaMov.get(h.adapterPosition))
        }
    }

    fun setTransferencias(listaMov:ArrayList<mMovAlmacen>){
        this.listaMov=listaMov
        notifyDataSetChanged()
    }

}












