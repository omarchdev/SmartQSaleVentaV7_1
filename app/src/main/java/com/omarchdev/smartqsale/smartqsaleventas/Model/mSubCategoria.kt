package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class mSubCategoria : mCategoriaProductos {

    @SerializedName("idSubCategoria")
    var idSubCategoria: Int = 0

    @SerializedName("descripcionSubCategoria")
    var descripcionSubCategoria: String = ""

    constructor() : super()

    constructor(id: Int, descripcion: String) : super() {
        this.idSubCategoria = id
        this.descripcionSubCategoria = descripcion
    }
}