package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioDetalle
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_product_lista_precio.view.*

class RvAdapterDetalleListaPrecios : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = ArrayList<ListaPrecioDetalle>()
    var clickPositionRv: ClickPositionRv? = null
    fun setItems(list: ArrayList<ListaPrecioDetalle>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.cv_item_product_lista_precio, p0, false)
        return Item(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val h = p0 as Item
        h.txtNombre.text = list[p1].product.getNombreCompletoProducto()
        h.txtPrecio.setText("${Constantes.DivisaPorDefecto.SimboloDivisa} ${list[p1].precioUnitarioVenta.fortMoneda}")
        h.btnEdit.setOnClickListener { clickPositionRv?.clickPosition(p1, 1) }
        h.txtCodigo.text=list[p1].product.getcKey()

    }

    inner class Item(item: View) : RecyclerView.ViewHolder(item) {
        val txtCodigo=item.txtCodigo
        val txtNombre = item.txtNombre
        val txtPrecio = item.txtPrecio
        val btnEdit = item.btnEdit
    }
}