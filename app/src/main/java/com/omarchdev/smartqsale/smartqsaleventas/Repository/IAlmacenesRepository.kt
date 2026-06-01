package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IAlmacenesRepository {

    @GET("api/Almacen/GetAlmacenes")
    fun GetAlmacenes(
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String,
        @Query("nombreCia") nombreCia: String
    ): Call<List<mAlmacen>>

    @GET("api/Almacen/ObtenerMovimientosAlmacen")
    fun ObtenerMovimientosAlmacen(
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String,
        @Query("nombreCia") nombreCia: String,
        @Query("desde") desde: String,
        @Query("hasta") hasta: String
    ): Call<List<mMovAlmacen>>

    @GET("api/Almacen/VerificarTipoAlmacen")
    fun VerificarTipoAlmacen(
        @Query("idAlmacen") idAlmacen: Int,
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<Byte>

    @POST("api/Almacen/VerificarStockAlmacen")
    fun VerificarStockAlmacen(
        @Body request: SolicitudEnvio<VerificarStockRequest>
    ): Call<List<mProduct>>

    @POST("api/Almacen/ConsultarProductosDisponibles")
    fun ConsultarProductosDisponibles(
        @Body request: SolicitudEnvio<VerificarStockRequest>
    ): Call<List<mProduct>>

    @POST("api/Almacen/RegistrarMovimientoAlmacen")
    fun RegistrarMovimientoAlmacen(
        @Body request: SolicitudEnvio<MovimientoAlmacenRegistroRequest>
    ): Call<Byte>

    @GET("api/Almacen/ObtenerCabeceraMovimiento")
    fun ObtenerCabeceraMovimiento(
        @Query("idMov") idMov: Int,
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<mMovAlmacen>

    @GET("api/Almacen/ObtenerProductosMovimiento")
    fun ObtenerProductosMovimiento(
        @Query("idMov") idMov: Int,
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<List<mProduct>>

    @GET("api/Almacen/ObtenerTransaccionesAlmacen")
    fun ObtenerTransaccionesAlmacen(
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<List<mTransacciones_Almacen>>

    @POST("api/Almacen/CompletarTransferenciaAlmacen")
    fun CompletarTransferenciaAlmacen(
        @Body request: SolicitudEnvio<CompletarTransferenciaRequest>
    ): Call<Byte>

    @GET("api/Almacen/ObtenerTransferenciasAlmacen")
    fun ObtenerTransferenciasAlmacen(
        @Query("idAlmacen") idAlmacen: Int,
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<List<mMovAlmacen>>

    @GET("api/Almacen/ObtenerTipoTransaccionesAlmacen")
    fun ObtenerTipoTransaccionesAlmacen(
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<List<mTransaccionAlmacen>>

    @POST("api/Almacen/GuardarAlmacen")
    fun GuardarAlmacen(
        @Body request: SolicitudEnvio<mAlmacen>
    ): Call<Byte>

    @POST("api/Almacen/EditarAlmacen")
    fun EditarAlmacen(
        @Body request: SolicitudEnvio<mAlmacen>
    ): Call<Byte>

    @GET("api/Almacen/ObtenerAlmacenId")
    fun ObtenerAlmacenId(
        @Query("idAlmacen") idAlmacen: Int,
        @Query("nombreCia") nombreCia: String,
        @Query("TipoMovimientoPedido") TipoMovimientoPedido: String
    ): Call<mAlmacen>

    @POST("api/Almacen/EliminarAlmacen")
    fun EliminarAlmacen(
        @Body request: SolicitudEnvio<EliminarAlmacenRequest>
    ): Call<Byte>

    @POST("api/Almacen/AnularMovimientoAlmacen")
    fun AnularMovimientoAlmacen(
        @Body request: SolicitudEnvio<Int>
    ): Call<Byte>
}