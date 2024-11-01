package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MediosSolicitud {

    @SerializedName("medios")
    @Expose(serialize = true)
    var medios:ArrayList<MedioImagen>?=null

    @SerializedName("medios_delete_id")
    @Expose(serialize = true)
    var idMediosDelete:ArrayList<Int>?=null

    @SerializedName("code_cia")
    @Expose(serialize = true)
    var codeCiaTienda:String?=null

    @SerializedName("tipo_mov")
    @Expose(serialize = true)
    var tipoMov:String?=null

    @SerializedName("imagenes_visibles")
    @Expose(serialize = true)
    var imagenesVisible=false

}