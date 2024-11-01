package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleModificador
import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IModificadorRepository {

    @GET("api/Modificador/ObtenerModificadoresProductoVenta")
    fun ObtenerModificadoresProductoVenta(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoConsulta: String,
        @Query("idproducto") idProducto: Int
    ): Call<List<Modificador>>

    @POST("api/Modificador/InsertarModificador")
    fun InsertarModificador(
        @Body sol:SolicitudEnvio<String>
    ): Call<Modificador>

    @POST("api/Modificador/GuardarDetalleModificador")
    fun GuardarDetalleModificador(
        @Body sol:SolicitudEnvio<DetalleModificador>
    ): Call<DetalleModificador>

}