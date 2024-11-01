package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

data class ComboRapidoSol (@SerializedName("idPedido") val idPedido:Int,@SerializedName("productoEnVenta") val productoEnVenta:ProductoEnVenta)