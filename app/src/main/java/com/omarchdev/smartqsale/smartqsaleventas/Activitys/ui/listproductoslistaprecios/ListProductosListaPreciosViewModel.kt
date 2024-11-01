package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listproductoslistaprecios

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioDetalle
import com.omarchdev.smartqsale.smartqsaleventas.Model.LoadingProcessState
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IListaPrecioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListProductosListaPreciosViewModel(idLista: Int) : ViewModel() {

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iListaPrecioRepository = retro.create(IListaPrecioRepository::class.java)

    val idLista = idLista
    val listaPrecioDetalle: MutableLiveData<ArrayList<ListaPrecioDetalle>> by lazy {
        MutableLiveData<ArrayList<ListaPrecioDetalle>>().also {
            GetListaDetalle("")
        }
    }

    val loadingInit=MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }

    val loadingProcessSave = MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }

    fun GetItemListaPos(pos:Int):ListaPrecioDetalle=listaPrecioDetalle.value!![pos]

    fun GetListaDetalle(parametro: String) {
        GetDetalleListaPrecios(idLista, parametro)
    }

    fun UpdateDetalleListaPrecio(item: ListaPrecioDetalle) {
        GlobalScope.launch {
            loadingProcessSave.postValue(LoadingProcessState().apply { loading = true })
            item.idLista=idLista
            val sol=SolicitudEnvio(codeCia,TIPO_CONSULTA,
                item,
                Constantes.Terminal.idTerminal,
                Constantes.Usuario.idUsuario)
            val cadena= Gson().toJson(sol)
            val result = iListaPrecioRepository.UpdateListaPrecioDetalle(sol).execute().body()
            val sState = LoadingProcessState()
            if(result!=null){
                if(result.codeResult==200){
                    listaPrecioDetalle.value!!.find { x->x.idDetalleLista==item.idDetalleLista }!!.precioUnitarioVenta=item.precioUnitarioVenta
                }
                sState.resultOk = result.codeResult == 200
                sState.loading = false
                sState.terminate = true
                sState.message = result.messageResult
                loadingProcessSave.postValue(sState)
            }
        }
    }

    private fun GetDetalleListaPrecios(idLista: Int, parametro: String) {

        GlobalScope.launch {
            loadingInit.postValue(LoadingProcessState().apply { loading=true })
            val result = iListaPrecioRepository.GetListPrecioDetalle(codeCia,TIPO_CONSULTA,idLista, parametro).execute().body()
            // BdConnectionSql.getSinglentonInstance().GetListPrecioDetalle(idLista, parametro)
            if(result!=null)
            listaPrecioDetalle.postValue(ArrayList(result))
            loadingInit.postValue(LoadingProcessState().apply { loading=false })
        }

    }
}

class ListProductosListaPreciosViewModelFact(val idLista: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListProductosListaPreciosViewModel(idLista) as T
    }
}