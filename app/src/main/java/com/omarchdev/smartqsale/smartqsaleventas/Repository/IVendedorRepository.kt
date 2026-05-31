package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IVendedorRepository {

    @GET("api/vendedor/GetVendedores")
    fun GetVendedores(@Query("codecia")codeCia:String,
                      @Query("tipoconsulta")tipoConsulta:String,
                      @Query("param")param:String): Call<List<mVendedor>>

    @GET("api/Vendedor/ObtenerVendedorPorId")
    fun ObtenerVendedorPorId(
        @Header("idVendedor") idVendedor: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<mVendedor>

    @POST("api/Vendedor/RegistroVendedor")
    fun RegistroVendedor(@Body solicitudEnvio: SolicitudEnvio<mVendedor>): Call<Byte>

}