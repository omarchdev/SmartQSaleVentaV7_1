package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

data class ProductoTiempoSol(
    @SerializedName("productoVenta")
    val productoEnVenta: ProductoEnVenta,
    @SerializedName("idCabeceraPedido")
    val idCabeceraPedido:Int,
    @SerializedName("horaFinal")
    val horaFinal:String)