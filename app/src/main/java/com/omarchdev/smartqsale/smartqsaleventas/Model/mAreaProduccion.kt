package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class mAreaProduccion(){

    @SerializedName("idArea")
    var idArea:Int=0
    @SerializedName("descripcion")
    var cDescripcionArea:String=""
    @SerializedName("cEstadoEliminado")
    var cEstadoEliminado:String=""
    var impresora=mImpresora()

}