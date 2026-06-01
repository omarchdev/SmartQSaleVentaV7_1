package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class EliminarPagoTemporalDto(
    @SerializedName("idCabeceraPedido")
    val idCabeceraPedido: Int,
    @SerializedName("idTipoPago")
    val idTipoPago: Int,
    @SerializedName("idPago")
    val idPago: Int
)
