package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
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




}