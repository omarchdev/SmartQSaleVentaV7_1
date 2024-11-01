package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IZonaServicioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface IConfigZonaServicio {
    fun cargaExito()
}

class ConfigZonaServicioMesaVM() : ViewModel(), AsyncZonaServicio.ListenerActualizarZonaServicio {
    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val mesaData = MutableLiveData<mZonaServicio>()
    var iConfigZonaServicio: IConfigZonaServicio? = null
    var iZonaServicioRepository = retro.create(
        IZonaServicioRepository::class.java
    )
    val asyncZonaServcio = AsyncZonaServicio()

    fun getZonaServicio(code: Int) {
        GlobalScope.launch {

            try {
                var mesa = iZonaServicioRepository.DatosZonaServicioMesa(
                    codeCia,
                    Constantes.BASECONN.TIPO_CONSULTA, code
                ).execute().body()

                if (mesa != null) {
                    mesaData.postValue(mesa!!)
                }
            } catch (ex: Exception) {
                ex.toString()
            }


        }
    }

    fun guardarZonaServicio(descripcion: String, delivery: Boolean) {
        var zonaServicio = mZonaServicio()
        if(mesaData.value!=null){
            zonaServicio.idZona=mesaData.value!!.idZona

        }
        zonaServicio.descripcion = descripcion
        if (delivery) {
            zonaServicio.idTipoZona = 400
        } else {

            zonaServicio.idTipoZona = Constantes.ConfigTienda.idTipoZonaServicio

        }

        asyncZonaServcio.GuardarZonaServicio(zonaServicio, this)
    }


    fun setZonaServicio() {
        GlobalScope.launch {

        }
    }

    override fun ActualizarExito() {
        iConfigZonaServicio?.cargaExito()
    }

    override fun ErrorActualizar() {
    }
}
