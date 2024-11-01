package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

data class ResultPack(
    @SerializedName("preciosCategoria")
    val precioCategoria:List<CategoriaPack>,
    @SerializedName("detallePack")
    val detallePack:List<PackElemento>
)
