package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class MedioPagoEntrega(){

    var iid_Metodo_Pago_Entrega=0
    var cCodeMedioPago=0

    var cDescripcionMedioPago=""
    @SerializedName("descripcion_metodo_pago")
    var descripcion_metodo_pago=""
    override fun toString(): String {
        return "$descripcion_metodo_pago"
    }

}