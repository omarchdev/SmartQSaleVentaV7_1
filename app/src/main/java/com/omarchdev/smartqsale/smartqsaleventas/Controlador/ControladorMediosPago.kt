package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioPagoEntrega
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IMediosPago
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ControladorMediosPago {


    private val codeCia = GetJsonCiaTiendaBase64x3()
    private var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private var iMediosPago = retro.create(
        IMediosPago::class.java
    )


    fun GetMediosPago():List<mMedioPago>{

        try{

            val list=iMediosPago.GetMediosPago(codeCia,TIPO_CONSULTA).execute().body()!!
            return list

        }catch (ex:Exception){
            return ArrayList()
        }

    }


    fun GetMediosPagoDelivery():List<mMedioPago>{

        try{

            val list=iMediosPago.GetMedioPagosDeliveryAPP(codeCia,TIPO_CONSULTA).execute().body()!!
            return list

        }catch (ex:Exception){
            return ArrayList()
        }

    }




    fun GetMetodosPagosCompanyDelivery():List<MedioPagoEntrega>{

        try{

            val list=iMediosPago.GetMetodosPagosCompanyDelivery(codeCia,TIPO_CONSULTA).execute().body()!!
            return list

        }catch (ex:Exception){
            return ArrayList()
        }

    }
}