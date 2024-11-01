package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

data class SolModificadorProductoDetallePedido(
    @SerializedName("idProducto") val idProducto: Int,
    @SerializedName("idCabeceraPedido") val idCabeceraPedido: Int,
    @SerializedName("cantidad") val cantidad: Float,
    @SerializedName("modificador") val Modificador: String,
    @SerializedName("idPventa") val idPventa: Int
)
