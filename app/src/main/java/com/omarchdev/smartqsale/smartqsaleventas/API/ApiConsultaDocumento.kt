package com.omarchdev.smartqsale.smartqsaleventas.API

import android.util.Log
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.PersonaDoc
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class ApiConsultaDocumento{

    private val retro= Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build()


    var irConsultaDocumento:IRConsultaDocumento?=null
    interface IRConsultaDocumento{
        fun ResultadoConsultaDocumento(customer: mCustomer)
    }

    fun ConsultaPersonaPorTipoDocumento(numDoc:String,tipo:String):mCustomer{
        var cliente=mCustomer()
        var iConsultaDocumento:IConsultaDocumento=retro.create(IConsultaDocumento::class.java)
        var result: Call<PersonaDoc>?=null
        when(tipo){

            "RUC_1"->result=iConsultaDocumento.ConsultaRuc(numDoc)
            "DNI_1"->result=iConsultaDocumento.ConsultaDNI(numDoc)
        }
        result?.enqueue(object: Callback<PersonaDoc> {
            override fun onFailure(call: Call<PersonaDoc>, t: Throwable) {
                Log.d("da" ,"demooo")
                cliente.setiId(-99)
             }
            override fun onResponse(call: Call<PersonaDoc>, response: Response<PersonaDoc>) {
                if(response.isSuccessful){
                    var data=response.body()
                    cliente.setiId(0)
                    cliente.estadoDomicilio=data?.estadoDomicilio
                    cliente.estadoContribuyente=data?.estado
                    cliente.razonSocial=data?.denominacion
                    cliente.setcDireccion(data?.direccion)
                    irConsultaDocumento?.ResultadoConsultaDocumento(cliente)
                }
            }

        })

        return cliente
    }

}