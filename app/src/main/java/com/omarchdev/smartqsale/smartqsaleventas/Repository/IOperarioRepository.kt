package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mOperario
import retrofit2.Call
import retrofit2.http.*

interface IOperarioRepository {

    @GET("api/operario")
    fun GetOperarios(
        @Query("codecia") codecia: String,
        @Query("tipoconsulta") tipoconsulta: String,
        @Query("parametro") parametro: String
    ): Call<ArrayList<mOperario>>


    @POST("api/operario")
    fun InsertaOperario(@Body solData: SolicitudEnvio<mOperario>): Call<ResultProcces>

    @PUT("api/operario")
    fun ActualizaOperario(@Body solData: SolicitudEnvio<mOperario>): Call<ResultProcces>

    @DELETE("api/operario")
    fun EliminaOperario(@Query("codeCia")codeCia:String,
                        @Query("tipoConsulta")tipoConsulta:String,
                        @Query("codeOperario")idOperario:Int):Call<ResultProcces>

    @GET("api/operario/GetOperarioId")
    fun GetOperarioId(
        @Query("codeCia") codeCia: String,
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeOp") codeOp: Int
    ):Call<mOperario>
}