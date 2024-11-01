package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta
import retrofit2.Call
import retrofit2.http.GET
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
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<List<mPagosEnVenta>>



}