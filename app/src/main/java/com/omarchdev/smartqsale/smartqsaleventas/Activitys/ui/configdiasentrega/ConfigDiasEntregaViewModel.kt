package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configdiasentrega

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.ConfigDiaSemana
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.SemanaConfigWeb
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICompanyRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigDiasEntregaViewModel : ViewModel() {

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iCompanyRepository = retro.create(ICompanyRepository::class.java)

    val SemanaConfigWeb:MutableLiveData<SemanaConfigWeb>by lazy{
        MutableLiveData<SemanaConfigWeb>().also {
            getSemanaConfigWeb()
        }
    }

    val resultGuardar= MutableLiveData<HashMap<String,Any>>().apply {

        val map=HashMap<String,Any>()
        map.put("A",false)
        map.put("B",false)
        map.put("C","")
        value=map
    }
    val mostrarDialogCarga=MutableLiveData<Boolean>().apply { value=false }
    fun closeDialogMessage(){

        val map=HashMap<String,Any>()
        map.put("A",false)
        map.put("B",false)
        map.put("C","")
        resultGuardar.postValue(map)
    }
    private fun getSemanaConfigWeb(){
        GlobalScope.launch {
            mostrarDialogCarga.postValue(true)
            val semana=iCompanyRepository.GetSemanaConfig(codeCia, TIPO_CONSULTA).execute().body()
            mostrarDialogCarga.postValue(false)
            if(semana!=null){

                val semanaConfig=SemanaConfigWeb()
                semanaConfig.diasSemana=ArrayList(semana)

                SemanaConfigWeb.postValue(semanaConfig)
            }

        }
    }


    fun GuardarDiaSemana(idDiaSemana:Int,activo:Boolean){

        GlobalScope.launch {
            mostrarDialogCarga.postValue(true)
            val sol=SolicitudEnvio(codeCia,TIPO_CONSULTA, ConfigDiaSemana(idDiaSemana,activo))
            val result= iCompanyRepository.GuardarConfigDiaSemana(sol).execute().body()
            val map=HashMap<String,Any>()
            map.put("A",true)
            if(result!!.codeResult==200){
                val estados=SemanaConfigWeb.value

                SemanaConfigWeb.value!!.diasSemana.find { x->x.IdSemanaConfig==idDiaSemana }!!.Activo=activo
                map.put("B",true)
            }else{
                map.put("B",true)
            }
            map.put("C",result.messageResult)
            resultGuardar.postValue(map)
            mostrarDialogCarga.postValue(false)
        }

    }

    fun GuardarDiasSemanaConfig(semanaConfig:SemanaConfigWeb){

        GlobalScope.launch {

            val diasOriginales= SemanaConfigWeb.value!!.diasSemana
            semanaConfig.diasSemana.forEachIndexed { index, diaSemanaConfig ->

               var diaOriginal= diasOriginales.find { x->x.IdSemanaConfig==diaSemanaConfig.IdSemanaConfig }
                if(diaSemanaConfig.Activo!=diaOriginal!!.Activo){

                }
            }


        }


    }

}
