package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.pedidoreservaresumen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.LoadingProcessState
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResumenPedido
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewModel : ViewModel() {
    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iPedidoRepository = retro.create(
        IPedidoRespository::class.java
    )

    val listResumePedido:MutableLiveData<List<ResumenPedido>> by lazy {
        MutableLiveData<List<ResumenPedido>>().also{
            GetLista()
        }
    }
    val loadingProcessSave = MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }

    val loadingInit=MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }

    fun ActualizaEstadoResumenPedido(bEstado:Boolean,position:Int){

        GlobalScope.launch {
            loadingProcessSave.postValue(LoadingProcessState().apply { loading = true })
            val sState = LoadingProcessState()
            val id=listResumePedido.value!![position].idPedido

            val result=BdConnectionSql.getSinglentonInstance().updateVerificadoPedido(id,bEstado)
            if(result.codeResult==200){
                listResumePedido.value!!.find { x->x.idPedido==id }!!.marcado=bEstado
            }
            sState.resultOk = result.codeResult == 200
            sState.loading = false
            sState.terminate = true
            sState.message = result.messageResult
            loadingProcessSave.postValue(sState)
        }

    }

    fun CierraMensajeConfirmacion(){
        val sState = LoadingProcessState()
        sState.resultOk =false
        sState.loading = false
        sState.terminate = false
        sState.message = ""
        loadingProcessSave.value=sState
    }
    fun GetLista(){iPedidoRepository

        GlobalScope.launch {
            loadingInit.postValue(LoadingProcessState().apply { loading=true })
            val list=iPedidoRepository.GetResumenPedidoReserva(codeCia, BASECONN.TIPO_CONSULTA).execute().body()
            if(list!=null){
                listResumePedido.postValue(list)
            }

            loadingInit.postValue(LoadingProcessState().apply { loading=false })

        }
    }
}
