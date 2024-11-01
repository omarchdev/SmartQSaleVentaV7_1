package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

class ListaPrecios : Serializable {

    var idLista: Int = 0
    var descripcionLista = ""

}

class ListasPreciosActivas {

    var listas = ArrayList<ListaPrecios>()
}

class ListaPrecioDetalle {
    @SerializedName("idLista")
    var idLista: Int = 0
    @SerializedName("idDestalleLista")
    var idDetalleLista: Int = 0
    @SerializedName("producto")
    var product = mProduct()
    @SerializedName("precioUnitarioVenta")
    var precioUnitarioVenta = BigDecimal(0)
}