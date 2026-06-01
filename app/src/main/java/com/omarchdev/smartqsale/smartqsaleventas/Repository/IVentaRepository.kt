package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("api/venta/CancelarVenta")
    fun CancelarVenta(@Body solicitudEnvio: SolicitudEnvio<Int>): Call<BdConnectionSql.RetornoCancelar>

    @GET("api/venta/ObtenerTiposAnulacionDocumento")
    fun ObtenerTiposAnulacionDocumento(
        @Query("ciaCode") ciaCode: String,
        @Query("tipo") tipo: String,
        @Query("idCabeceraVenta") idCabeceraVenta: Int
    ): Call<List<TipoAnulacion>>

    @GET("api/venta/VerificarConfigCorrelativosNota")
    fun VerificarConfigCorrelativosNota(
        @Query("ciaCode") ciaCode: String,
        @Query("tipo") tipo: String,
        @Query("idTerminal") idTerminal: Int
    ): Call<ResultProcces>

    @POST("api/venta/GenerarNota")
    fun GenerarNota(@Body solicitudEnvio: SolicitudEnvio<MotivoAnulacionDto>): Call<mDocVenta>

    @POST("api/venta/ActualizarEstadoNotaGenerada")
    fun ActualizarEstadoNotaGenerada(@Body solicitudEnvio: SolicitudEnvio<ActualizarNotaDto>): Call<Void>

    @POST("api/venta/AnularDocumento")
    fun AnularDocumento(@Body solicitudEnvio: SolicitudEnvio<MotivoAnulacionDto>): Call<mDocVenta>

    @GET("api/venta/ObtenerVentaId")
    fun ObtenerVentaId(
        @Query("ciaCode") codeCia: String,
        @Query("tipo") tipo: String,
        @Query("idCabeceraVenta") idCabeceraVenta: Int
    ): Call<mRespuestaVenta>

    @GET("api/venta/GetPagosVenta")
    fun GetPagosVenta(
        @Query("ciaCode") codeCia: String,
        @Query("tipo") tipo: String,
        @Query("idCabeceraVenta") idCabeceraVenta: Int
    ): Call<List<mPagosEnVenta>>
}