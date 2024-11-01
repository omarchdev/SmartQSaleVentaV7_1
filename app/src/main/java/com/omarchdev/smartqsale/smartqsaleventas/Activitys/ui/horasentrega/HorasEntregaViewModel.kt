package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.horasentrega

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.LoadingProcessState
import com.omarchdev.smartqsale.smartqsaleventas.Model.TimeHour
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HorasEntregaViewModel : ViewModel() {

    val horaInicio:MutableLiveData<TimeHour>by lazy {
        MutableLiveData<TimeHour>().also{
            it.value= TimeHour()
        }
    }
    val horaFinal:MutableLiveData<TimeHour>by lazy {
        MutableLiveData<TimeHour>().also{
            it.value=TimeHour()
        }
    }
    val loadingStateGuardar=MutableLiveData<LoadingProcessState>().apply {
        value=LoadingProcessState()
    }
    val initCharge:MutableLiveData<Boolean>by lazy{
        MutableLiveData<Boolean>().also{
            Init()
        }
    }
    fun OcultarDialogResult(){
        loadingStateGuardar.value=LoadingProcessState()
    }
    fun GuardarHoras(){
        GlobalScope.launch {
            var initCarga=LoadingProcessState()
            initCarga.loading=true
            initCarga.terminate=false
            initCarga.resultOk=false
            initCarga.message=""
            loadingStateGuardar.postValue(initCarga)
            val result=BdConnectionSql.getSinglentonInstance().GuardarHorasIntervaloEntrega(horaInicio.value,horaFinal.value)
            var EndCarga=LoadingProcessState()
            EndCarga.loading=false
            EndCarga.terminate=true
            EndCarga.message=result.messageResult
            if(result.codeResult==200){
                EndCarga.resultOk=true
            }else{
                initCarga.resultOk=false
            }
            loadingStateGuardar.postValue(EndCarga)

        }
    }
    private fun Init(){
        GlobalScope.launch {
            initCharge.postValue(true)
            var list= BdConnectionSql.getSinglentonInstance().GetIntervaloEntrega()
            if(list.size==2){
                horaInicio.postValue(list[0])
                horaFinal.postValue(list[1])
            }
            initCharge.postValue(false)
        }
    }

    fun SetHora(hour:Int,minuto:Int,tipo:Int){
        val hora=TimeHour()
        hora.hour=hour
        hora.minute=minuto
        when(tipo){
            1->{
               horaInicio.apply {
                   value=hora
               }
            }
            2->{
                horaFinal.apply {
                    value=hora
                }
            }

        }

    }

}
