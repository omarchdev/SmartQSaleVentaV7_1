package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.wang.avi.AVLoadingIndicatorView
import java.math.BigDecimal

class DialogRegistroIngresoProductoCompra: DialogFragment(), AsyncProducto.ListenerDatosCompra {



    private lateinit var edtCantidad:EditText
    private lateinit var edtPrecioCompra:EditText
    private lateinit var txtTotal: TextView
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button
    private lateinit var listAlmacen:ListView
    private lateinit var avi:AVLoadingIndicatorView
   val listaAlmacen:ArrayList<mAlmacen> =ArrayList()
    var titulo:String=""
    var PrecioCompra=BigDecimal(0)
    var Cantidad:Float=0f
    var Total=BigDecimal(0)
    var permitir=true
    var idProductCompra:Int=0
    private lateinit var listenerProductCompra: ListenerProductCompra
    var product=mProduct()
    private lateinit var asyncProducto: AsyncProducto

    public interface ListenerProductCompra{
        public fun productoSeleccionadoParaCompra(product: mProduct)
    }

    fun setListenerProductCompra(listenerProductCompra: ListenerProductCompra){
        this.listenerProductCompra=listenerProductCompra
    }
    fun setInfoProducto(titulo:String,Cantidad:Float,idProducto:Int){
        this.titulo=titulo
        this.Cantidad=Cantidad
        this.idProductCompra=idProducto
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
        val view: View
        try{
            view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_product_compra, null)
            dialog.setView(view)
            product=mProduct()
            asyncProducto= AsyncProducto()
            listAlmacen=view.findViewById(R.id.listAlmacen)
            edtCantidad=view.findViewById(R.id.edtCantidad)
            edtPrecioCompra=view.findViewById(R.id.edtPrecioCompra)
            btnGuardar=view.findViewById(R.id.btnGuardar)
            btnCancelar=view.findViewById(R.id.btnCancelar)
            edtCantidad.setText(String.format("%.2f",BigDecimal(Cantidad.toString())))
            edtPrecioCompra.setText(String.format("%.2f",PrecioCompra))

            val numberTextWatcher= NumberTextWatcher(edtCantidad)
            edtCantidad.addTextChangedListener(numberTextWatcher)
            numberTextWatcher.iNumberTextWatcher=object: NumberTextWatcher.INumberTextWatcher {
                override fun cantidadBigDecimal(number: BigDecimal) {
                    Cantidad=number.toFloat()
                    calcularTotal()
                }
            }
            val numberTextWatcherV2= NumberTextWatcher(edtPrecioCompra)
            edtPrecioCompra.addTextChangedListener(numberTextWatcherV2)
            numberTextWatcherV2.iNumberTextWatcher=object: NumberTextWatcher.INumberTextWatcher {
                override fun cantidadBigDecimal(number: BigDecimal) {
                    PrecioCompra=number
                    calcularTotal()
                }
            }
            txtTotal=view.findViewById(R.id.txtTotal)
            avi=view.findViewById(R.id.avi)
            listenerButton()
            dialog.setTitle(titulo)
            asyncProducto.setListenerDatosCompra(this)
            asyncProducto.ObtenerDatosCompraProducto(idProductCompra)
            avi.show()


        }catch (f:Exception){
            Log.e("Error",f.toString())
        }

        return dialog.create()
    }
    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
    fun listenerButton(){

        btnGuardar.setOnClickListener {

            permitir=true
            if(Total.compareTo(BigDecimal(0))==0){

                permitir=false
            }
            if(permitir){
                product.idProduct=idProductCompra
                product.precioCompra=PrecioCompra
                product.setcProductName(titulo)
                product.setdQuantity(Cantidad)

                listenerProductCompra.productoSeleccionadoParaCompra(product)
                this.dismiss()
            }
        }
        btnCancelar.setOnClickListener {
            this.dismiss()
        }

    }

    private val edtPCompraListener=object:TextWatcher{
        override fun afterTextChanged(s: Editable?) {
         }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
         }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s.toString().equals(".")){
                PrecioCompra= BigDecimal(0)
            }
            else if(s.toString().equals("")){
                PrecioCompra=BigDecimal(0)
            }else {
                PrecioCompra= BigDecimal(s.toString())
            }
            calcularTotal()
        }

    }
    private fun calcularTotal(){
        Cantidad.toBigDecimal()
        Total= PrecioCompra.multiply( Cantidad.toBigDecimal())
        txtTotal.setText(Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",Total))
    }
    private val edtCantidadListener=object:TextWatcher{
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s.toString().equals(".")){
                Cantidad= 0f
            }
            else if(s.toString().equals("")){
                Cantidad=0f
            }else {
                Cantidad= s.toString().toFloat()
            }
            calcularTotal()
        }
    }

    override fun ObtenerDatosProductoEnAlmacen(precioCompra: BigDecimal?, almacenList: MutableList<mAlmacen>?) {


        var precioCompraTemp=BigDecimal(0)
        PrecioCompra=BigDecimal(0)
        if(precioCompra!=null){
            precioCompraTemp= precioCompra
            PrecioCompra=precioCompraTemp
        }

        edtPrecioCompra.setText(String.format("%.2f",precioCompraTemp))
        val listString= arrayOfNulls<String>(almacenList!!.size)
        for(i in 0 until almacenList!!.size){
            listString[i]=almacenList.get(i).descripcionAlmacen+" ("+String.format("%.2f",almacenList!!.get(i).cantidad)+")"+"\n"+ ObtenerNombreTienda(almacenList.get(i).idTienda)
        }
        try {
            avi.hide()
             listAlmacen.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, listString)
        }catch (e:Exception){
            e.toString()
        }
    }
}
