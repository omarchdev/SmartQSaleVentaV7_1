package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.mOperario
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IOperarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsyncOperarios(){


    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iOperarioRepository = retro.create(
        IOperarioRepository::class.java
    )
    interface ListenerObtenerOperarios{
        fun OperariosObtenidos(listaOperarios:ArrayList<mOperario>)
    }

    fun ObtenerOperarios(listenerObtenerOperarios: ListenerObtenerOperarios,parametro:String){
         GlobalScope.launch {
            val callable=iOperarioRepository.GetOperarios(codeCia,TIPO_CONSULTA,parametro)
            var response=callable.execute()
            val result=response.body()
           launch(Dispatchers.Main){
                if(result!=null)
                listenerObtenerOperarios.OperariosObtenidos(result)
            }
        }
    }

}