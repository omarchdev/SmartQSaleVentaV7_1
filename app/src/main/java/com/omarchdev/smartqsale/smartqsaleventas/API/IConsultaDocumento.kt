package com.omarchdev.smartqsale.smartqsaleventas.API

import com.omarchdev.smartqsale.smartqsaleventas.Model.PersonaDoc
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IConsultaDocumento {

    @GET("api/ConsultaRuc/DNI/{num_dni}")
    fun ConsultaDNI(@Path(value="num_dni")numDni:String): Call<PersonaDoc>

    @GET("api/ConsultaRuc/{num_ruc}")
    fun ConsultaRuc(@Path(value="num_ruc")numRuc:String): Call<PersonaDoc>
}