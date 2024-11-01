package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class mZonaServicio{

    @SerializedName("idZona")
    var idZona=0
    @SerializedName("descripcion")
    var descripcion=""
    @SerializedName("color")
    var color=""
    @SerializedName("idMarca")
    var idMarca=0
    @SerializedName("descripcionMarca")
    var descripcionMarca=""
    @SerializedName("numEspacios")
    var numEspacios=0
    @SerializedName("idModelo")
    var idModelo=0
    @SerializedName("cEstadoOcupado")
    var cEstadoOcupado=false
    @SerializedName("numReservas")
    var numReservas=0
    @SerializedName("bZonaLibre")
    var bZonaLibre=false

    @SerializedName("idTipoZona")
    var idTipoZona=0


}