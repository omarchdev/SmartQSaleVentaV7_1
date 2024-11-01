package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class RespuestaProductoVenta {

    @SerializedName("codeRespuesta")
    var codeRespuesta: Int = 0
    @SerializedName("mensaje")
    var mensaje = ""
    @SerializedName("productoVenta")
    var productoEnVenta = ProductoEnVenta()


}