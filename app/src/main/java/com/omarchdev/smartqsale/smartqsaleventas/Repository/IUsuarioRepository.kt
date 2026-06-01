package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IUsuarioRepository {

    @GET("api/Usuario/ObtenerUsuariosRegistrados")
    fun ObtenerUsuariosRegistrados(
        @Query("idCompany") idCompany: Int,
        @Query("idTienda") idTienda: Int
    ): Call<List<mUsuario>>

    @GET("api/Usuario/ObtenerUsuarioPorId")
    fun ObtenerUsuarioPorId(
        @Query("idCompany") idCompany: Int,
        @Query("idTienda") idTienda: Int,
        @Query("idUsuario") idUsuario: Int
    ): Call<mUsuario>

    @POST("api/Usuario/EditarUsuarioRegistrado")
    fun EditarUsuarioRegistrado(@Body solicitudEnvio: SolicitudEnvio<mUsuario>): Call<Byte>

    @POST("api/Usuario/EliminarUsuario")
    fun EliminarUsuario(@Body solicitudEnvio: SolicitudEnvio<Int>): Call<Byte>
}
