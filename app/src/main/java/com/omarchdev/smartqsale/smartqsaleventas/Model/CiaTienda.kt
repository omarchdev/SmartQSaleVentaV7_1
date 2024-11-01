package com.omarchdev.smartqsale.smartqsaleventas.Model

import  android.util.Base64
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes

data class CiaTienda(
        @SerializedName("tienda")
        val tienda: Tienda,
        @SerializedName("cia")
        val compania: Compania)


data class Compania(@SerializedName("id_company")
                    val idCompania: Int,
                    @SerializedName("cdescripcion")
                    val descripcion: String)

fun GetJsonCiaTiendaBase64x3(): String {
    val tienda = Tienda()
    tienda.idTienda=Constantes.Tienda.idTienda
    val compania = Compania(Constantes.Empresa.idEmpresa, "")
    val ciaTienda = CiaTienda(tienda, compania)
    val gson = Gson()
    var codeCia= gson.toJson(ciaTienda)
    var code=Constantes.Empresa.idEmpresa.toString()+"-"+Constantes.Tienda.idTienda.toString()
    for (i in 0..1){
        code=Base64.encodeToString(code.toByteArray(),0).replace("\n","")

    }

    return code
}



