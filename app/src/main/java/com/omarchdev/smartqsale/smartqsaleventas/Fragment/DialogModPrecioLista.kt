package com.omarchdev.smartqsale.smartqsaleventas.Fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioDetalle
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_dialog_mod_precio_lista.*
import java.math.BigDecimal


class DialogModPrecioLista : DialogFragment(), NumberTextWatcher.INumberTextWatcher {

    interface IDialogModPrecioLista{
        fun saveDetalleLista(listaPrecioDetalle: ListaPrecioDetalle)
    }


    var listaPrecioDetalle=ListaPrecioDetalle()
    var iDialogModPrecioLista:IDialogModPrecioLista?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_mod_precio_lista, container, false)
    }

    fun newInstance(listaPrecioDetalle:ListaPrecioDetalle):DialogModPrecioLista{
        val dialog=DialogModPrecioLista()
        dialog.listaPrecioDetalle=listaPrecioDetalle
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtWatcher=NumberTextWatcher(edtMonto)
        edtWatcher.iNumberTextWatcher=this
        edtMonto.setText(listaPrecioDetalle.precioUnitarioVenta.fortMoneda)
        edtMonto.addTextChangedListener(edtWatcher)
        edtMonto.clearFocus()
        edtMonto.setOnClickListener {
            edtMonto.setSelection(edtMonto.text.toString().length)
        }
        edtMonto.setOnFocusChangeListener { v, hasFocus ->
           if(hasFocus){
                edtMonto.setSelection(edtMonto.text.toString().length)
            }
        }
        txtMoneda.text=Constantes.DivisaPorDefecto.SimboloDivisa
        txtTitulo.text=listaPrecioDetalle.product.nombreCompletoProducto
        btnOk.setOnClickListener {
            val listaPrecioDetalleTemp=ListaPrecioDetalle().apply {

                if(BigDecimal(edtMonto.text.toString()).compareTo(BigDecimal(0))==1){
                    Log.i("info1","mayor")
                    idLista=listaPrecioDetalle.idLista
                    idDetalleLista=listaPrecioDetalle.idDetalleLista
                    precioUnitarioVenta=BigDecimal(edtMonto.text.toString())
                    product=listaPrecioDetalle.product
                }else{
                    Log.i("info2","igual menor")
                }

            }
            iDialogModPrecioLista?.saveDetalleLista(listaPrecioDetalleTemp)
        }
        btnCancel.setOnClickListener {
            this.dismiss()
        }


    }

    override fun cantidadBigDecimal(number: BigDecimal) {

    }

}



