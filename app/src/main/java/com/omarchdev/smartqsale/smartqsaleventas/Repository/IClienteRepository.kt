package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSaldoCliente
import com.omarchdev.smartqsale.smartqsaleventas.Model.CancelarPagoCtaCteRequest
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProcesarPagoCtaCteRequest
import com.omarchdev.smartqsale.smartqsaleventas.Model.CtaCteCliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IClienteRepository {
    @GET("api/cliente/GetClientes")
    fun GetClientePedido(@Query("tipoconsulta") tipoMov: String,
                           @Query("codecia") nombreCia: String,
                           @Query("param") param: String): Call<List<mCustomer>>

    @GET("api/Cliente/GetClienteEdiccion")
    fun GetCliente(@Query("tipoconsulta") tipoMov: String,
                           @Query("codecia") nombreCia: String,
                           @Query("idCliente") idCliente: Int): Call<mCustomer>

    @GET("api/Cliente/GetCustomerById")
    fun GetCustomerById(@Query("tipoconsulta") tipoMov: String,
                        @Query("codecia") nombreCia: String,
                        @Query("idCliente") idCliente: Int): Call<mCustomer>

    @GET("api/Cliente/GetSaldosClientes")
    fun GetSaldosClientes(@Query("tipoconsulta") tipoMov: String,
                          @Query("codecia") nombreCia: String,
                          @Query("saldoCero") saldoCero: Byte,
                          @Query("nombreCliente") nombreCliente: String): Call<List<mSaldoCliente>>

    @GET("api/Cliente/GetClienteNumTelefono")
    fun GetClienteNumTelefono(@Query("tipoconsulta") tipoMov: String,
                   @Query("codecia") nombreCia: String,
                   @Query("numtelefono") numtelefono: String): Call<mCustomer>



    @POST("api/Cliente/EditaCliente")
    fun EditaCliente(@Body solicitud: SolicitudEnvio<mCustomer>):Call<Int>

    @POST("api/Cliente/CancelarPagoCtaCte")
    fun CancelarPagoCtaCte(@Body solicitud: SolicitudEnvio<CancelarPagoCtaCteRequest>): Call<Byte>

    @POST("api/Cliente/ProcesarPagoCtaCte")
    fun ProcesarPagoCtaCte(@Body solicitud: SolicitudEnvio<ProcesarPagoCtaCteRequest>): Call<Byte>

    @GET("api/Cliente/ObtenerCtaCteCorriente")
    fun ObtenerCtaCteCorriente(
        @Query("tipoconsulta") tipoMov: String,
        @Query("codecia") nombreCia: String,
        @Query("idCliente") idCliente: Int
    ): Call<CtaCteCliente>
}