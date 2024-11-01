package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioPagoEntrega
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IMediosPago {

    @GET("api/MedioPago/GetMedioPagos")
    fun GetMediosPago(@Query("codeCia")codeCia:String,@Query("codeTipo")codeTipo:String ): Call<List<mMedioPago>>


    @GET("api/MedioPago/GetMedioPagosDeliveryAPP")
    fun GetMedioPagosDeliveryAPP(@Query("codeCia")codeCia:String,@Query("codeTipo")codeTipo:String ): Call<List<mMedioPago>>

    @GET("api/MedioPago/GetMetodosPagosCompanyDelivery")
    fun GetMetodosPagosCompanyDelivery(@Query("codeCia")codeCia:String,@Query("codeTipo")codeTipo:String ): Call<List<MedioPagoEntrega>>



}