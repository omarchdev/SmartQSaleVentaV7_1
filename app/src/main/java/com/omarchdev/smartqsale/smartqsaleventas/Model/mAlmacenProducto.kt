package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.math.BigDecimal

class  mAlmacenProducto(){


     var idProducto: Int = 0
     var codigoBarra: String?=null
     var nombreProducto: String?=null
    var descripcionVariante: String? = null
    var precioCompra: BigDecimal= BigDecimal(0)
     var precioVenta: BigDecimal? = null
     var cantidadDisponible: Float = 0.toFloat()
     var esVariante: Boolean = false
     var idAlmacen: Int = 0.toInt()
    var descripcionAlmacen: String?=null
     var idProductoAlmacen: Int = 0
    var esTienda: Boolean = false
    var idTienda=0


    fun getTotalCompra(): BigDecimal {
        return precioCompra!!.multiply(BigDecimal(cantidadDisponible.toDouble()))
    }

    fun getTotalVenta(): BigDecimal {
        return precioVenta!!.multiply(BigDecimal(cantidadDisponible.toDouble()))
    }

}