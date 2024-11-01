package com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp

import com.omarchdev.smartqsale.smartqsaleventas.Model.MediosSolicitud
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProcessResult
import retrofit2.Call
import retrofit2.http.*

interface IMediosRetro {

    @GET("api/Medio/GetImagesBanner?")
    fun listRepos(@Query("codeCiaMov") codeCia: String, @Query("tipoMovimiento")tipo:String): Call<MediosSolicitud>

    @POST("api/Medio/UpdateImagenesBanner")
    fun saveMediosBanner(@Body medioSolicitud: MediosSolicitud):Call<ProcessResult<Int>>

}