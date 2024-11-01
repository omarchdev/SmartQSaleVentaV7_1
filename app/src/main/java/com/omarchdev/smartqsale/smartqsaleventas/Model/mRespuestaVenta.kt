package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class mRespuestaVenta{

    @SerializedName("valorRespuesta")
    var valorRespuesta:Int=0

    @SerializedName("cabeceraVenta")
    var cabeceraVenta=mCabeceraVenta()

    @SerializedName("mensaje")
    var mensaje = ""

    @SerializedName("codeRespuesta")
    var codeRespuesta = 0

    @SerializedName("productosVenta")
    var list=ArrayList<ProductoEnVenta>()

    @SerializedName("cliente")
    var cliente=mCustomer();

    @SerializedName("fechaEntregaTemp")
    var fechaEntregaTemp=""
}