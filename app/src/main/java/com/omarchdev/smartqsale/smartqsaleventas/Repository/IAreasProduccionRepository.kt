package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IAreasProduccionRepository {


    @GET("api/AreaProduccion/GetAreasProduccion")
    fun GetAreasProduccion(@Query("TipoMovimientoPedido") tipoMov: String,
                              @Query("nombreCia") nombreCia: String): Call<List<mAreaProduccion>>


}