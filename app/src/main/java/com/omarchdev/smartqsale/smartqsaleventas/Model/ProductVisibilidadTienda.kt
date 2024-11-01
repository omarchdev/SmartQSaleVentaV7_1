package com.omarchdev.smartqsale.smartqsaleventas.Model

data class ProductVisibilidadTienda( val idProductConfigTienda:Int,  var tienda: Tienda,var visible:Boolean, var idProducto:Int)

class ProductVisibilidadEnTiendas{

    var idProduct:Int=0
    var listadoVisibilidad=ArrayList<ProductVisibilidadTienda>()

}