package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

open class DiaSemana {

    var Descripcion=""


}

class DiaSemanaConfig{

    @SerializedName("idDiaSemanaConfig")
    var IdSemanaConfig=0
    @SerializedName("activo")
    var Activo=false
    @SerializedName("descripcion")
    var Descripcion=""
}

class SemanaConfigWeb{

    @SerializedName("diasSemana")
    var diasSemana=ArrayList<DiaSemanaConfig>()

}