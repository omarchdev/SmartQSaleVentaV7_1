package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listadozonaservicio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IZonaServicioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListadoZonaServicioVM : ViewModel() {

    val codeCia = GetJsonCiaTiendaBase64x3()
    val retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val iZonaServicioRepository = retro.create(
        IZonaServicioRepository::class.java
    )
    val listaZonaServicios=MutableLiveData<List<mZonaServicio>>().apply {
        MutableLiveData<List<mZonaServicio>>().also {
            getZonasServicio()
        }
    }

     fun getZonasServicio(){
         GlobalScope.launch {
             val lists =
                 iZonaServicioRepository.ObtenerZonasServiciosM(codeCia,
                     Constantes.BASECONN.TIPO_CONSULTA
                 ).execute().body()
             listaZonaServicios.postValue(lists)

         }

    }
}