package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.*

interface IZonaServicioRepository {
    @GET("api/ZonaServicio/UltimasVentasZonaServicio")
    fun UltimasVentasZonaServicio(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idZonaServicio") idZonaServicio: Int
    ): Call<List<mCabeceraVenta>>

    @GET("api/ZonaServicio/DatosZonaServicio")
    fun DatosZonaServicio(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idZonaServicio") idZonaServicio: Int
    ): Call<mZonaServicio>

    @POST("api/ZonaServicio/GuardarZonaServicio")
    fun GuardarZonaServicio(
        @Body sol:SolicitudEnvio<mZonaServicio>
    ):Call<ResultProcces>

    @GET("api/ZonaServicio/DatosZonaServicioMesa")
    fun DatosZonaServicioMesa(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idZonaServicio") idZonaServicio: Int
    ): Call<mZonaServicio>
    @GET("api/ZonaServicio/ObtenerMarcasAutos")
    fun ObtenerMarcasAutos(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<List<mMarca>>

    @GET("api/ZonaServicio/ObtenerModelosCW")
    fun ObtenerModelosCW(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("TipoMarca") TipoMarca: String
    ): Call<List<cModelo>>

    @GET("api/ZonaServicio/GetTipoZonaServicio")
    fun GetTipoZonaServicio(@Header("idCia")idCia:String,
                            @Header("tipoMovimiento")tipoMovimiento:String,
                            @Query("idZonaServicio")idZonaServicio:Int):Call<TipoZonaServicio>

    @PUT("api/ZonaServicio/ActualizarZonaServicioCw")
    fun ActualizarZonaServicioCw(@Body sol: SolicitudEnvio<mZonaServicio>): Call<Int>

    @GET("api/ZonaServicio/GetZonaServiciosM")
    fun ObtenerZonasServiciosM(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<List<mZonaServicio>>

    @DELETE("api/ZonaServicio/EliminarZonaServicioPedido")
    fun EliminarZonaServicioPedido(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("") idCabeceraPedido: Int
    ):Call<Int>

    @PUT("api/ZonaServicio/GuardarSeleccionZonaServicio")
    fun GuardarSeleccionZonaServicio(@Body sol:SolicitudEnvio<ZonaServicioPedido>):Call<ResZonaServicio>

}