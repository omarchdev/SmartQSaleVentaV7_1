package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer
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



    @GET("api/Cliente/GetClienteNumTelefono")
    fun GetClienteNumTelefono(@Query("tipoconsulta") tipoMov: String,
                   @Query("codecia") nombreCia: String,
                   @Query("numtelefono") numtelefono: String): Call<mCustomer>



    @POST("api/Cliente/EditaCliente")
    fun EditaCliente(@Body solicitud: SolicitudEnvio<mCustomer>):Call<Int>
}