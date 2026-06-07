package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSubCategoria
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces
import retrofit2.Call
import retrofit2.http.*

interface ISubCategoriaRepository {

    @GET("api/SubCategoria/GetSubCategorias")
    fun GetSubCategorias(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idCategoria") idCategoria: Int
    ): Call<List<mSubCategoria>>

    @POST("api/SubCategoria/AgregarSubCategoria")
    fun AgregarSubCategoria(@Body sol: SolicitudEnvio<mSubCategoria>): Call<mSubCategoria>

    @POST("api/SubCategoria/EditarSubCategoria")
    fun EditarSubCategoria(@Body sol: SolicitudEnvio<mSubCategoria>): Call<mSubCategoria>

    @POST("api/SubCategoria/EliminarSubCategoria")
    fun EliminarSubCategoria(@Body sol: SolicitudEnvio<mSubCategoria>): Call<ResultProcces>
}
