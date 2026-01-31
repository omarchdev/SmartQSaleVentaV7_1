package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.math.BigDecimal

data class ProductoListaPrecioSeleccion (
    var idProducto:Int,
    var cTipo_Unidad:String,
    var cantidad: BigDecimal,
    var cUnidad:String,
    var idListaPrecio:Int,
    var idPedido:Int
)