package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioDetalle
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListasPreciosActivas
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface IListaPrecioRepository {

    @GET("api/ListaPrecio/GetListaPreciosSelect")
    fun GetListaPreciosSelect(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<ListasPreciosActivas>

    @PUT("api/ListaPrecio/UpdateListaPrecioDetalle")
    fun UpdateListaPrecioDetalle(@Body sol: SolicitudEnvio<ListaPrecioDetalle>): Call<ResultProcces>

    @GET("api/ListaPrecio/GetListPrecioDetalle")
    fun GetListPrecioDetalle(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idListaPrecio") idListaPrecio: Int,
        @Query("parametro") parametro: String
    ): Call<List<ListaPrecioDetalle>>


}