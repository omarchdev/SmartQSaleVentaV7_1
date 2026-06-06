package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.AlmacenProducto
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IStockProductosRepository {
    @GET("api/Producto/ObtenerStockProductos")
    fun ObtenerStockProductos(
        @Query("codeCia") codeCia: String,
        @Query("tipoConsulta") tipoConsulta: String
    ): Call<List<mProduct>>

    @GET("api/Producto/ObtenerStockProductosFiltroTexto")
    fun ObtenerStockProductosFiltroTexto(
        @Query("codeCia") codeCia: String,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("texto") texto: String
    ): Call<List<mProduct>>

    @GET("api/Almacen/ObtenerCantidadPorAlmacen")
    fun ObtenerCantidadPorAlmacen(
        @Query("codeCia") codeCia: String,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("idProducto") idProducto: Int
    ): Call<List<AlmacenProducto>>
}
