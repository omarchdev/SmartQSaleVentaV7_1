package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class VentaDocumento {
    @SerializedName("descripcionDocumento")
    var descripcionDocumento: String = ""

    @SerializedName("monto")
    var monto: BigDecimal = BigDecimal(0)

    @SerializedName("cantidad")
    var cantidad: Int = 0

    constructor()

    constructor(descripcionDocumento: String, monto: BigDecimal, cantidad: Int) {
        this.descripcionDocumento = descripcionDocumento
        this.monto = monto
        this.cantidad = cantidad
    }
}