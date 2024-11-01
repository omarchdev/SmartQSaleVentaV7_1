package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class ResumenPedido {

    @SerializedName("idPedido")
    var idPedido=0
    @SerializedName("identificador")
    var identificador=""
    @SerializedName("fecha")
    var fecha=""
    @SerializedName("productoResumen")
    var productoResumen=""
    @SerializedName("marcado")
    var marcado=false
}