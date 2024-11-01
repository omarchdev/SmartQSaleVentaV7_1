package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.EnvioCpeW
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultNumTelefono
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVenta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IVentaRepository {

    @GET("api/venta/GetCabeceraVentasHistorial")
    fun GetCabeceraVenta(
        @Query("ciaCode") codeCia: String,
        @Query("tipo") tipo: String,
        @Query("fechaInicio") fechaInicio: Int,
        @Query("fechaFinal") FechaFinal: Int,
        @Query("idCliente") idCliente: Int
    ): Call<List<mVenta>>

    @GET("api/venta/GetEstadoVenta")
    fun GetEstadoVenta(
        @Query("ciaCode") codeCia: String,
        @Query("tipo") tipo: String,
        @Query("idCabeceraVenta") idCabeceraVenta: Int
    ): Call<Int>


    @GET("api/venta/GetNumeroCelularVentaCliente")
    fun GetNumeroCelularVentaCliente(
        @Query("ciaCode") codeCia: String,
        @Query("tipo") tipo: String,
        @Query("idCabeceraVenta") idCabeceraVenta: Int
    ): Call<ResultNumTelefono>

    @GET("api/venta/CrearMensajeEnvioWhatsappCpe")
    fun CrearMensajeEnvioWhatsappCpe(
        @Query("ciaCode") codeCia: String,
        @Query("tipo") tipo: String,
        @Query("idCabeceraVenta") idCabeceraVenta: Int,
        @Query("numeroTelefono") numeroTelefono: String
    ): Call<EnvioCpeW>


}