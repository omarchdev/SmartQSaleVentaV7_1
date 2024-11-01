package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface

import android.os.Bundle
import androidx.fragment.app.DialogFragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProcesoVenta
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPriceAdditional
import pl.polak.clicknumberpicker.ClickNumberPickerView

class DialogSelectPrecioAdic():DialogFragment(){



    val adapter: RvAdapterPriceAdditional
    internal var clickNumberPickerView: ClickNumberPickerView?=null
    val asyncPVenta:AsyncProcesoVenta
    var idProducto:Int
    val listaPrecios:ArrayList<AdditionalPriceProduct>
    var titulo:String
    var permitir=true

    init {
        listaPrecios=ArrayList()
        asyncPVenta= AsyncProcesoVenta()
        adapter=RvAdapterPriceAdditional()
        idProducto=0
        titulo=""
    }

    var obtenerInfoProduct:ObtenerInfoProduct?=null


    public interface ObtenerInfoProduct{
        public fun ObtenerPrecioCantidad(idProducto:Int,cantidad:Float,idPventa:Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v=LayoutInflater.from(activity).inflate(R.layout.dialog_select_precio_venta,null)
        val dialog=AlertDialog.Builder(activity)
        val contentPV=v.findViewById<LinearLayout>(R.id.contentPV)
        val pb=v.findViewById<ProgressBar>(R.id.pb)
        listaPrecios.clear()
        pb.visibility=View.VISIBLE
        dialog.setTitle(titulo)
        val rvPVAddicional=v.findViewById<RecyclerView>(R.id.rvPVAddicional)
        rvPVAddicional.adapter=adapter
        rvPVAddicional.layoutManager= GridLayoutManager(activity,2)
        clickNumberPickerView=v.findViewById(R.id.clickNumberPickerView)
        clickNumberPickerView!!.setPickerValue(1f)
        contentPV.visibility= View.INVISIBLE
        if(idProducto>0) {
            asyncPVenta.ObtenerPreciosVenta(idProducto)
            asyncPVenta.setListenerPrecioVentaAdiccional(object:AsyncProcesoVenta.ListenerPrecioVentaAdiccional{
                override fun ObtenerPreciosVentaAd(additionalPriceProductList: MutableList<AdditionalPriceProduct>?) {
                    listaPrecios.clear()
                    listaPrecios.addAll(ArrayList(additionalPriceProductList!!))
                    if(additionalPriceProductList!!.size>0){
                        var listap=ArrayList<String>()
                        additionalPriceProductList!!.forEach {
                                 listap.add("${Constantes.DivisaPorDefecto.SimboloDivisa} ${String.format("%.2f",it.additionalPrice)}")
                        }
                        adapter.AgregarListaPrecios(listap)
                        pb.visibility=View.INVISIBLE
                        contentPV.visibility=View.VISIBLE
                    }else{
                            dismiss()
                        Toast.makeText(activity,"Error al conseguir los precios de venta adicionales",Toast.LENGTH_LONG).show()
                    }
                }
                override fun ErrorConsulta() {
                    dismiss()
                    Toast.makeText(activity,"Error al conseguir los precios de venta adicionales",Toast.LENGTH_LONG).show()

                }

            })
        }else{
            dismiss()
            Toast.makeText(activity,"Error al conseguir los precios de venta adicionales",Toast.LENGTH_LONG).show()

        }
        dialog.setView(v).setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->

            permitir=true
            if (listaPrecios.size > 0) {
                permitir=true
            } else if (listaPrecios.size == 0) {
                permitir= false
            }

            if(adapter.posActual>=0){
                permitir=true
            }else{
                Toast.makeText(activity,"Debe elegir un precio de venta",Toast.LENGTH_LONG).show()

                permitir=false
            }
            if(permitir){
                try {
                    obtenerInfoProduct?.ObtenerPrecioCantidad(idProducto, clickNumberPickerView!!.value, listaPrecios.get(adapter.posActual).id)
                }catch (e:Exception){
                    Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show()
                }
            }


        })
                .setNegativeButton("Cancelar",null)

        return dialog.create()
    }
}