package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ICompanyRepository {

    @POST("api/Compania/GuardarConfigDiaSemana")
    fun GuardarConfigDiaSemana(@Body sol: SolicitudEnvio<ConfigDiaSemana>): Call<ResultProcces>

    @GET("api/Compania/GetSemanaConfig")
    fun GetSemanaConfig(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<List<DiaSemanaConfig>>

    @GET("api/Compania/GetConfiguracionWhatsappWeb")
    fun GetConfiguracionWhatsappWeb(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<ConfigWhatsappTienda>

    @POST("api/Compania/GuardarConfiguracionCelularWeb")
    fun GuardarConfiguracionCelularWeb(@Body sol: SolicitudEnvio<ConfigWhatsappTienda>): Call<ResultProcces>


}