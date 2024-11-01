package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class ProductoTiempo {

    @SerializedName("producto")
    var product= mProduct()

    @SerializedName("hora_inicio")
    var horaInicio=""

    @SerializedName("id_pedido")
    var idPedido=0
}