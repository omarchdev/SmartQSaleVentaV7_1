package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IPagoRepository {

    @GET("api/Pedido/GetPagosRealizadosPedido")
    fun GetPagosPedido(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<List<mPagosEnVenta>>


    @GET("api/Pedido/GetPagosRealizadosPedidoV2")
    fun GetPagosPedidoV2(
        @Header("codeCia") codeCia: String,
        @Header("tipoBusqueda") tipoBusqueda: String,
        @Header("idPedido") idPedido: Int
    ): Call<List<mPagosEnVenta>>

    @GET("api/Pedido/GetPagosRealizadosPedidoV3")
    fun GetPagosPedidoV3(
        @Header("codeCia") codeCia: String,
        @Header("tipoBusqueda") tipoBusqueda: String,
        @Header("idPedido") idPedido: Int
    ): Call<List<mPagosEnVenta>>

}