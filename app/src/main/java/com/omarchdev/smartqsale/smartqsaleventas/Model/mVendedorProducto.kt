package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class mVendedorProducto {

    @SerializedName("product")
    var product = mProduct()

    @SerializedName("vendedor")
    var vendedor = mVendedor()

    @SerializedName("idCabeceraVenta")
    var idCabeceraVenta = 0

    @SerializedName("fechaProceso")
    var fechaProceso = ""

    @SerializedName("idTienda")
    var idTienda = 0
}