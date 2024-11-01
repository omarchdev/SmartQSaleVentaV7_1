package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.confignumphoneweb

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.ConfigWhatsappTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICompanyRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigNumPhoneWebViewModel : ViewModel() {

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iCompanyRepository = retro.create(ICompanyRepository::class.java)


    val configWebTienda: MutableLiveData<ConfigWhatsappTienda> by lazy {
        MutableLiveData<ConfigWhatsappTienda>().also {
            GetConfiguracionCelularWeb()
        }
    }

    val visibleCarga = MutableLiveData<Boolean>().apply {
        value = false
    }

    val result: MutableLiveData<Map<String, Any>> by lazy {
        MutableLiveData<Map<String, Any>>().also {
            //       InitResult()
        }
    }

    private fun InitResult() {
        try {
            val map = HashMap<String, Any>()
            map.put("", false)
            map.put("", false)
            map.put("", "")
            result.value = map
        } catch (ex: Exception) {
            ex.toString()
        }

    }

    fun CloseDialog() {
        val r = HashMap<String, Any>()
        r.put("A", false)
        r.put("B", false)
        r.put("C", "")
        result.value = r
    }


    fun GuardarConfiguracionCelularWeb(
        bActivo: Boolean,
        cNumeroCelular: String,
        cMensajeInicial: String
    ) {


        GlobalScope.launch {
            visibleCarga.postValue(true)
            var permite = true
            var mensaje = ""
            if (cNumeroCelular.trim().length != 9) {
                permite = false
                mensaje = "El número de celular debe contar con 9 dígitos"
            }

            if (permite) {
                val configWhatsappTienda = ConfigWhatsappTienda()
                configWhatsappTienda.bActivo = bActivo
                configWhatsappTienda.cNumero = cNumeroCelular
                configWhatsappTienda.cMensajeInicial = cMensajeInicial
                val sol = SolicitudEnvio(codeCia, TIPO_CONSULTA, configWhatsappTienda)
                val resultGuardar =
                    iCompanyRepository.GuardarConfiguracionCelularWeb(sol).execute().body()
                val map = HashMap<String, Any>()
                map.put("A", true)
                if (resultGuardar != null) {
                    if (resultGuardar.codeResult == 200) {
                        map.put("B", true)
                    } else {
                        map.put("B", false)
                    }
                    map.put("C", resultGuardar.messageResult)
                }

                result.postValue(map)
                visibleCarga.postValue(true)
            } else {
                val map = HashMap<String, Any>()
                map.put("A", false)
                map.put("B", true)
                map.put("C", mensaje)
                result.postValue(map)
                visibleCarga.postValue(true)
            }

        }
    }

    fun GetConfiguracionCelularWeb() {
        GlobalScope.launch {
           // var temp=iCompanyRepository.GetConfiguracionWhatsappWeb(codeCia, TIPO_CONSULTA)

            val result =iCompanyRepository.GetConfiguracionWhatsappWeb(codeCia, TIPO_CONSULTA).execute().body()
            configWebTienda.apply {
                if(result!=null){
                    postValue(result)
                }

            }
        }
    }
}
