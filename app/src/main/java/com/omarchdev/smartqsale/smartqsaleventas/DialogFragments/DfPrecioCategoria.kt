package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.R

import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack
import kotlin.jvm.internal.Intrinsics
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_df_precio_categoria.*

import androidx.recyclerview.widget.LinearLayoutManager

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.DivisaPorDefecto

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.bg
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPrecioCategoriaV2


class DfPrecioCategoria : DialogFragment(), AsyncProducto.ListenerPreciosCategorias {
    override fun ErrorBusquedaPrecioCategoria() {
        dismiss()
        AlertDialog.Builder(requireContext()).setTitle("Advertencia").
                setMessage("Verifique su conexión a internet y reinicie la aplicación").
                setPositiveButton("Salir", null).create().show();

    }

    override fun ListadoCategoriasPrecio(list: MutableList<CategoriaPack>?) {
        pbCarga.visibility = View.GONE
        listPrecioCategoria?.addAll(ArrayList(list!!))
        if(list == null) {
            Intrinsics.throwNpe()
        }
        if (list!!.size > 0) {
            try {
                this.adapter.AgregarElementos(ArrayList(list))
            } catch (e: Exception) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }


    var  product: mProduct?=null
    var listPrecioCategoria:ArrayList<CategoriaPack>?=ArrayList<CategoriaPack>()
    var listener: ListenerGuardadoPrecio? = null
    var adapter = RvAdapterPrecioCategoriaV2()
    var asyncProduct = AsyncProducto()


    interface ListenerGuardadoPrecio {
        fun GuardarInfoPrecioCategoria(arrayList: ArrayList<CategoriaPack>)
    }

    fun newInstance(product: mProduct): DfPrecioCategoria {
        var a = DfPrecioCategoria()
        a.product = product
        return a
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_df_precio_categoria, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ListenerGuardadoPrecio){
            listener=context
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asyncProduct.setListenerPreciosCategorias(this)
        val mproduct = this.product
        if (mproduct != null) {
            if (mproduct == null) {
                Intrinsics.throwNpe()
            }
            mproduct.idProduct
            val asyncProducto = this.asyncProduct
            val mproduct2 = this.product
            if (mproduct2 == null) {
                Intrinsics.throwNpe()
            }
            asyncProducto.ObtenerPreciosCategorias(mproduct2!!.idProduct)
            val stringBuilder = StringBuilder()
            var mproduct3 = this.product
            if (mproduct3 == null) {
                Intrinsics.throwNpe()
            }
            stringBuilder.append(mproduct3!!.getcProductName())
            stringBuilder.append(" ")
            stringBuilder.append(DivisaPorDefecto.SimboloDivisa)
            mproduct3 = this.product
            if (mproduct3 == null) {
                Intrinsics.throwNpe()
            }
            val precioVenta = mproduct3!!.precioVenta
            txtPrecioPack.text =product!!.getcProductName()+" "+precioVenta.fortMoneda
            rvPrecioCat.adapter = this.adapter
            rvPrecioCat.layoutManager = LinearLayoutManager(activity)
            pbCarga.visibility = View.VISIBLE
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
        btnAceptar.setOnClickListener {

            var monto=0.bg

            listPrecioCategoria!!.forEach {
                monto=monto.add(it.precio)
            }
          if(monto.compareTo(product!!.precioVenta)!=0){
              Toast.makeText(activity, "No", Toast.LENGTH_SHORT).show()
          }else{
                 listener?.GuardarInfoPrecioCategoria(ArrayList(listPrecioCategoria!!))
                  dismiss()
            }
        }

    }
}
