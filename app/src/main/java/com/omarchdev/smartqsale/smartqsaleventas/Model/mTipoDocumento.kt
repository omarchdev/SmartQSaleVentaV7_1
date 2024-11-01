package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class mTipoDocumento(){

    var idTipoDocumento:Int=0
    var idDocSunat:Int=0
    @SerializedName("descripcionDocumento")
    var cDescripcionCorta:String=""
    var bVerificarDireccion:Boolean=false
    @SerializedName("color_tipo_documento")
    var cColorDescripcion:String=""
    var longitudNumeroDoc:Int=0
    var denominacionCliente:String=""
    var denominacionNumero:String=""
    var verificaRuc:Boolean=false

}