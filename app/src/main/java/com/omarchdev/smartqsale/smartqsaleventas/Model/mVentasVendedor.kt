package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class mVentasVendedor{

    @SerializedName("vendedor")
    var vendedor=mVendedor()

    @SerializedName("numeroVentas")
    var numeroVentas=0

    @SerializedName("montoVentas")
    var montoVentas=BigDecimal(0)


}