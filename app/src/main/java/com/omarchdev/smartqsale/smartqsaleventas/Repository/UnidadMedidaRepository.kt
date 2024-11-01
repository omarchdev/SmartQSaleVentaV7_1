package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnidadMedidaRepository {


    @GET("api/UnidadMedida/ObtenerUnidadesMedida")
    fun ObtenerUnidadesMedida( @Query("codeCia") nombreCia: String,@Query("tipoConsulta") tipoMov: String
                          ): Call<List<mUnidadMedida>>

}