package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R

class DialogSeleccionarCantidad: DialogFragment() {


    private lateinit var edtCantidad:EditText
    private lateinit var btnCancelar:Button
    private lateinit var btnAceptar:Button
    private lateinit var txtMensajeAlerta: TextView
    private lateinit var obtenerCantidad:ObtenerCantidadSeleccionada
    var NombreProducto=""
    var idProducto=0
    var cantidad:Float=0f
    var cantidadDisponible:Float=0f



    public interface ObtenerCantidadSeleccionada{

        public fun ObtenerDatosProducto(product: mProduct)

    }


    public fun ObtenerInfoProduct(nombreProducto:String,idProducto:Int,cantidadDisponible:Float){
        this.NombreProducto=nombreProducto
        this.idProducto=idProducto
        this.cantidadDisponible=cantidadDisponible
    }


    public fun setObtenerCantidadSeleccionada(obtenerCantidadSeleccionada: ObtenerCantidadSeleccionada){
        this.obtenerCantidad=obtenerCantidadSeleccionada

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var view:View
        view=LayoutInflater.from(activity).inflate(R.layout.dialog_select_cantidad,null,false)
        val dialog=AlertDialog.Builder(activity)
        try{

        dialog.setTitle("$NombreProducto")
        dialog.setView(view)
        edtCantidad=view.findViewById(R.id.edtCantidad)
        btnAceptar=view.findViewById(R.id.btnAceptar)
        btnCancelar=view.findViewById(R.id.btnCancelar)
        txtMensajeAlerta=view.findViewById(R.id.txtMensajeAlerta)
        val txtCantidadDisponible=view.findViewById<TextView>(R.id.txtCantidadDisponible)
        txtCantidadDisponible.setText("Cantidad disponible "+String.format("%.2f",cantidadDisponible))
        ClickListener()
        }catch (e:Exception){
            Toast.makeText(activity,e.toString(),Toast.LENGTH_SHORT).show()
        }

       return dialog.create()

    }

    fun ClickListener(){

        btnAceptar.setOnClickListener {
            edtCantidad.clearFocus()
            if (edtCantidad.text.toString().equals(".")) {
                cantidad = 0f
            } else if (edtCantidad.text.toString().equals(".0")) {
                cantidad = 0f
            } else {
                cantidad = edtCantidad.text.toString().toBigDecimal().toFloat()
            }
            if (cantidad == 0f) {
                txtMensajeAlerta.visibility = View.VISIBLE
            } else if (cantidad > 0f)
            {
                if(cantidad<=cantidadDisponible) {
                    var product = mProduct()
                    product.idProduct = idProducto
                    product.setcProductName(NombreProducto)
                    product.setdQuantity(cantidad)
                    obtenerCantidad.ObtenerDatosProducto(product)
                    this.dismiss()
                }else{
                    edtCantidad.setError("La cantidad ingresada es mayor a la disponible")
                }
            }
        }
        btnCancelar.setOnClickListener {

            this.dismiss()

        }

    }


}










