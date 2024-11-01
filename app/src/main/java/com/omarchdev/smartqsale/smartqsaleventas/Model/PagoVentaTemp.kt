package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


data class PagoVentaTemp (
    @SerializedName("idTipoPago")
    var idTipoPago :Int=0,

    @SerializedName("cTipoPago")
    var cTipoPago: String="",

    @SerializedName("tipoPago")
    var tipoPago: String= "",

    @SerializedName("cantidadPagada")
    var cantidadPagada: BigDecimal ,

    @SerializedName("esEfectivo")
    var esEfectivo:Boolean,

    @SerializedName("activaPagoExterno")
    var activaPagoExterno :Boolean,

    @SerializedName("idCabeceraPedido")
    var IdCabeceraPedido:Int


)