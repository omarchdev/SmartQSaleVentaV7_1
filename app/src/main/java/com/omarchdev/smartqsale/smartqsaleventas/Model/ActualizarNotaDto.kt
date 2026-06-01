package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class ActualizarNotaDto(
    @SerializedName("idCabeceraVenta")
    val idCabeceraVenta: Int,
    @SerializedName("resultadoComprobante")
    val resultadoComprobante: ResultadoComprobante
)
