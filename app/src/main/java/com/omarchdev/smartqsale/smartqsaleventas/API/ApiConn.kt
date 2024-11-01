package com.omarchdev.smartqsale.smartqsaleventas.API

import android.util.Log
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.DecodeBase64
import com.omarchdev.smartqsale.smartqsaleventas.Model.ConnectionString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConn{

    var interfaceApiConnStart:InterfaceApiConnStart?=null
    private val retro= Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    interface InterfaceApiConnStart{

        fun ReceivedStringConn(connString:String)
        fun ConnInitError()

    }

    fun GetConnInit(){


        var interfaceCallApi:InterfaceCallApi=retro.create(InterfaceCallApi::class.java)
        val result=  interfaceCallApi.ReceivedConnInit()
        result.enqueue(object: Callback<ConnectionString> {
            override fun onFailure(call: Call<ConnectionString>, t: Throwable) {
                Log.d("da" ,"demooo")
                interfaceApiConnStart?.ConnInitError()
            }
            override fun onResponse(call: Call<ConnectionString>, response: Response<ConnectionString>) {
                if(response.isSuccessful){

                    var conn:ConnectionString=response.body()!!;
                    var s=conn.connString.DecodeBase64(10)
                    interfaceApiConnStart?.ReceivedStringConn(s)
                }
            }
        })
    }

}

