package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class  mAlmacenProducto(){


     @SerializedName("idProducto")
     var idProducto: Int = 0

     @SerializedName("codigoBarra")
     var codigoBarra: String?=null

     @SerializedName("nombreProducto")
     var nombreProducto: String?=null

    @SerializedName("descripcionVariante")
    var descripcionVariante: String? = null

    @SerializedName("precioCompra")
    var precioCompra: BigDecimal= BigDecimal(0)

     @SerializedName("precioVenta")
     var precioVenta: BigDecimal? = null

     @SerializedName("cantidadDisponible")
     var cantidadDisponible: Float = 0.toFloat()

     @SerializedName("esVariante")
     var esVariante: Boolean = false

     @SerializedName("idAlmacen")
     var idAlmacen: Int = 0.toInt()

    @SerializedName("descripcionAlmacen")
    var descripcionAlmacen: String?=null

     @SerializedName("idProductoAlmacen")
     var idProductoAlmacen: Int = 0

    @SerializedName("esTienda")
    var esTienda: Boolean = false

    @SerializedName("idTienda")
    var idTienda=0


    fun getTotalCompra(): BigDecimal {
        return precioCompra!!.multiply(BigDecimal(cantidadDisponible.toDouble()))
    }

    fun getTotalVenta(): BigDecimal {
        return precioVenta!!.multiply(BigDecimal(cantidadDisponible.toDouble()))
    }

}