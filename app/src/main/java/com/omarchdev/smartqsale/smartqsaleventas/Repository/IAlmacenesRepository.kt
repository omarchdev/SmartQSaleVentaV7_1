package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen
import retrofit2.Call
import retrofit2.http.GET
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
}