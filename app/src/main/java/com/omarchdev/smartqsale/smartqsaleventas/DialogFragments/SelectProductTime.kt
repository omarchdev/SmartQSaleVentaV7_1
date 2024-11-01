package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProductKt
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cardviewproductsinsale.view.*
import kotlinx.android.synthetic.main.fragment_select_product_time_list_dialog.*
import java.io.Serializable


class SelectProductTime : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_product_time_list_dialog, container, false)
    }

    interface ISelectProductTime : Serializable {
        fun GetIdProductTime(product: mProduct)
    }

    var iSelectProductTime:ISelectProductTime?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list_product.layoutManager = LinearLayoutManager(context)
        pbProductTime.visibility=View.VISIBLE
        val asyncProductKt = AsyncProductKt()
        asyncProductKt.GetProductosTiempo()
        iSelectProductTime = arguments?.getSerializable("interface") as ISelectProductTime

        asyncProductKt.iProductosTiempoConsulta = object : AsyncProductKt.IProductosTiempoConsulta {
            override fun ResultProductosTiempo(productos: List<mProduct>) {

                val itemAdapter = ItemAdapter(productos)
                list_product.adapter = itemAdapter
                pbProductTime.visibility=View.GONE
            }

        }
    }


    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.cardviewproductsinsale, parent, false)) {
        internal val imageViewProduct = itemView.ImageProductPhoto
        internal val productName = itemView.txtNombreProducto
        internal val productPrice = itemView.txtPrecioProducto

    }

    private inner class ItemAdapter internal constructor(private val lista: List<mProduct>) : RecyclerView.Adapter<ViewHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            try {
                holder.productName.text = lista[position].getcProductName()
                holder.productPrice.text = lista[position].precioVentaTexto

                if (lista[position].tipoRepresentacionImagen == 1.toByte()) {
                    holder.imageViewProduct.setImageResource(
                            holder.itemView.context.resources.getIdentifier(
                                    "@drawable/" + lista.get(position).getCodigoForma().trim(), null,
                                    holder.itemView.context.packageName))
                } else if (lista[position].tipoRepresentacionImagen == 2.toByte()) {
                    holder.imageViewProduct.clearColorFilter()
                    if (lista[position].getbImage() != null) {
                        holder.imageViewProduct.setImageBitmap(BitmapFactory.decodeByteArray(lista[position].getbImage(), 0, lista[position].getbImage().size))
                    } else {
                        holder.imageViewProduct.setImageResource(
                                holder.itemView.context.resources.getIdentifier(
                                        "@drawable/" + lista.get(position).getCodigoForma().trim(), null,
                                        holder.itemView.context.packageName))
                    }

                }

                holder.itemView.setOnClickListener {
                    iSelectProductTime?.GetIdProductTime(lista[position])
                    dismiss()
                }
            } catch (ex: Exception) {
                Log.e("exlist",ex.toString())
            }


        }

        override fun getItemCount(): Int = lista.count()
    }

    companion object {

        fun newInstance(tiSelectProductTime: ISelectProductTime): SelectProductTime =
                SelectProductTime().apply {
                    arguments = Bundle().apply {
                        this.putSerializable("interface", tiSelectProductTime)
                    }
                }

    }
}
