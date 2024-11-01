package com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MediosRetro {

    val retrofit = Retrofit.Builder()
            .baseUrl("http://192.99.154.9:8062/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val iMediosRetro:IMediosRetro=retrofit.create(IMediosRetro::class.java)


}