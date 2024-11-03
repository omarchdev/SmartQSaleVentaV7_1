package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class BienServicioDetraccion {
    @SerializedName("codigo")
    var Codigo: String = ""
    @SerializedName("descripcion")
    var Descripcion: String = ""
    @SerializedName("tasa_porcentaje")
    var Tasa_porcentaje: Int = 0
    override fun toString(): String {
        return Descripcion + " - " + Tasa_porcentaje.toString() + "%";
    }
}
