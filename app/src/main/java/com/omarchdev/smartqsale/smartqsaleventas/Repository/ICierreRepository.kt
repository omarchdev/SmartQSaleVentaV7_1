package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import java.math.BigDecimal
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ICierreRepository {

    @GET("api/Cierre/ObtenerCierrePorId")
    fun ObtenerCierrePorId(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<mCierre>

    @GET("api/Cierre/getCabeceraCierreCaja")
    fun getCabeceraCierreCaja(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<mCierre>

    @GET("api/Cierre/ObtenerCabeceraResumen")
    fun ObtenerCabeceraResumen(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<mResumenTotalVentas>

    @GET("api/Cierre/ObtenerAcumuladoVentasPorCierreMontoTop10")
    fun ObtenerAcumuladoVentasPorCierreMontoTop10(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<List<ProductoEnVenta>>

    @GET("api/Cierre/ComparativoCierresUltimos10")
    fun ComparativoCierresUltimos10(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<List<mCierre>>



    @GET("api/Cierre/getVentasPorHora")
    fun getVentasPorHora(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<List<mVentasPorHora>>

    @GET("api/Cierre/getResumenFlujoCaja")
    fun getResumenFlujoCaja(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<List<mResumenFlujoCaja>>

    @GET("api/Cierre/getResumenMP")
    fun getResumenMP(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<List<mResumenMedioPago>>



    @GET("api/Cierre/getMovimientoCaja")
    fun getMovimientoCaja(@Header("idCierre") idCierre:Int,@Query("tipoConsulta") tipoConsulta:String,@Query("codeCia") codeCia:String): Call<List<mDetalleMovCaja>>

    @GET("api/Cierre/ObtenerIdCierre")
    fun ObtenerIdCierre(
        @Query("codeCia") codeCia: String,
        @Query("tipoConsulta") tipoConsulta: String,
        @Header("idCompany") idCompany: Int,
        @Header("idTienda") idTienda: Int,
        @Header("idUsuario") idUsuario: Int
    ): Call<mCierre>

    @POST("api/Cierre/CerrarCaja")
    fun CerrarCaja(@Body solicitudEnvio: SolicitudEnvio<Int>): Call<Byte>

    @GET("api/Cierre/GetCierresHistorial")
    fun GetCierresHistorial(
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFinal") fechaFinal: String,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String,
        @Query("idUsuario") idUsuario: Int
    ): Call<List<mCierre>>

    @GET("api/Cierre/ObtenerMontoAperturaCierre")
    fun ObtenerMontoAperturaCierre(
        @Header("idCierre") idCierre: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<BigDecimal>

    @GET("api/Cierre/ObtenerAcumuladoVentasPorCierreMonto")
    fun ObtenerAcumuladoVentasPorCierreMonto(
        @Header("idCierre") idCierre: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<List<ProductoEnVenta>>

    @GET("api/Cierre/VentasPorDocumentoCierre")
    fun VentasPorDocumentoCierre(
        @Header("idCierre") idCierre: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<List<VentaDocumento>>

    @GET("api/Cierre/RetirosCajaPorCierre")
    fun RetirosCajaPorCierre(
        @Header("idCierre") idCierre: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<List<mDetalleMovCaja>>

    @GET("api/Cierre/TotalDescuentoCierre")
    fun TotalDescuentoCierre(
        @Header("idCierre") idCierre: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<BigDecimal>

    @GET("api/Cierre/CabeceraCierre")
    fun CabeceraCierre(
        @Header("idCierre") idCierre: Int,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<mCierre>

    @GET("api/Cierre/MovimientosCajaPorPeriodoFecha")
    fun MovimientosCajaPorPeriodoFecha(
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFinal") fechaFinal: String,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<List<mDetalleMovCaja>>
}