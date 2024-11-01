package com.omarchdev.smartqsale.smartqsaleventas.Repository
import com.omarchdev.smartqsale.smartqsaleventas.Model.RetornoApertura
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import java.math.BigDecimal

interface ICajaRepository {


    @POST("api/caja/AperturarCaja")
    fun AperturarCaja(@Body solicitudEnvio: SolicitudEnvio<BigDecimal>): Call<RetornoApertura>

}