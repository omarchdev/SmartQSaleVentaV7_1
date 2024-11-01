package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class ResZonaServicio{

    @SerializedName("respuesta")
    var respuesta=0.toByte()
    @SerializedName("zonaServicio")
    var zonaServicio=mZonaServicio()
    @SerializedName("observacion")
    var observacion=""
    @SerializedName("bTieneVentas")
    var bTieneVentas=false
    @SerializedName("cliente")
    var cliente=mCustomer()

}