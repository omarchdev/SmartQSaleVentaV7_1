package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.VarianteBase
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IVarianteRepo {
    @GET("api/Variantes/GetVariantesBase")
    fun GetVariantesBase(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") tipoConsulta: String,
        @Query("idProducto") idProducto: Int
    ): Call<VarianteBase>

    @GET("api/Variantes/ObtenerCantidadDisponibleVariante")
    fun ObtenerCantidadDisponibleVariante(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") tipoConsulta: String,
        @Query("idProducto") idProducto: Int,
        @Query("descripcion1") descripcion1: String,
        @Query("descripcion2") descripcion2: String,
        @Query("descripcion3") descripcion3: String

    ): Call<mProduct>
}
