package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioPagoEntrega
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipo_Pago
import retrofit2.Call
import retrofit2.http.*

interface IMediosPago {

    @GET("api/MedioPago/GetMedioPagos")
    fun GetMediosPago(@Query("codeCia")codeCia:String,@Query("codeTipo")codeTipo:String ): Call<List<mMedioPago>>


    @GET("api/MedioPago/GetMedioPagosDeliveryAPP")
    fun GetMedioPagosDeliveryAPP(@Query("codeCia")codeCia:String,@Query("codeTipo")codeTipo:String ): Call<List<mMedioPago>>

    @GET("api/MedioPago/GetMetodosPagosCompanyDelivery")
    fun GetMetodosPagosCompanyDelivery(@Query("codeCia")codeCia:String,@Query("codeTipo")codeTipo:String ): Call<List<MedioPagoEntrega>>

    @GET("api/MedioPago/GetTiposPago")
    fun GetTiposPago(@Query("codeCia") codeCia: String, @Query("codeTipo") codeTipo: String): Call<List<mTipo_Pago>>

    @POST("api/MedioPago/GuardarMedioPago")
    fun GuardarMedioPago(@Body medioPago: SolicitudEnvio<mMedioPago>): Call<Byte>

    @POST("api/MedioPago/EditarMedioPago")
    fun EditarMedioPago(@Body medioPago: SolicitudEnvio<mMedioPago>): Call<Byte>

    @POST("api/MedioPago/EliminarMedioPago")
    fun EliminarMedioPago(@Body idMedioPago: SolicitudEnvio<Int>): Call<Byte>
}