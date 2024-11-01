package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class VarianteBase {
    @SerializedName("valoresOpciones")
    var valoresOpciones = ArrayList<ValorOpcionVariante>()
    @SerializedName("opciones")
    var opciones = ArrayList<OpcionVariante>()
}
