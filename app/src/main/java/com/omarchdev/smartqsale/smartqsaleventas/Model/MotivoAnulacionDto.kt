package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class MotivoAnulacionDto(
    @SerializedName("idCabeceraVenta")
    val idCabeceraVenta: Int,
    @SerializedName("motivo")
    val motivo: String
)
