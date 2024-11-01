package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IVendedorRepository {

    @GET("api/vendedor/GetVendedores")
    fun GetVendedores(@Query("codecia")codeCia:String,
                      @Query("tipoconsulta")tipoConsulta:String,
                      @Query("param")param:String): Call<List<mVendedor>>

}