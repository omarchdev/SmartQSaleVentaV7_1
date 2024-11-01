package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.actmodestadoentregaenpedido

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.FlujoEntregaEnPedido
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActModEstadoEntregaEnPedidoViewModel(codePedido:Int) : ViewModel() {
    private val codePedido=codePedido
    private val idPedido=codePedido
    private val showDialog=MutableLiveData<HashMap<String,Any>>(HashMap())
    private val flujoPedido: MutableLiveData<FlujoEntregaEnPedido> by lazy {
        MutableLiveData<FlujoEntregaEnPedido>().also{
            GetEstadoFlujoPedido(codePedido)
        }
    }
    fun cargaDataInit(){
        GetEstadoFlujoPedido(codePedido)
    }

    val mostrarCarga=MutableLiveData<Boolean>().apply {
       postValue(false)
    }
    fun CierraComplemento(){

        val map=HashMap<String,Any>()
        map.put("A",false)
        map.put("B",0)
        showDialog.postValue(map)
    }
    fun MarcaFlujo(pos:Int,marcado:Boolean,mensaje: String){

        var idEstadoEntrega=0
        flujoPedido.value!!.listEstadoEntregaPedido.forEachIndexed { index, estadoEntregaPedidoEnUso ->
           if (index == pos) {
                estadoEntregaPedidoEnUso.bMarcado = marcado
                idEstadoEntrega=estadoEntregaPedidoEnUso.idEstadoEntrega
            }
        }
        if(idEstadoEntrega!=0){
            mostrarCarga.apply {
                postValue(true)
            }
            GlobalScope.launch {
                val fecha=BdConnectionSql.getSinglentonInstance().FechaServer()
                BdConnectionSql.getSinglentonInstance().GuardaAccionFlujoPedido(1,idEstadoEntrega,fecha,idPedido,mensaje,marcado)
                mostrarCarga.apply {
                    postValue(false)
              //      GetEstadoFlujoPedido(codePedido)
                }

            }
        }

    }

    fun GetFlujo(): LiveData<FlujoEntregaEnPedido> {
        return flujoPedido
    }

    fun GetEstadoDialogComplemento(): MutableLiveData<HashMap<String,Any>>{
        return showDialog
    }

    //Carga inicial
    fun GetEstadoFlujoPedido(codePedido:Int){

        GlobalScope.launch {
            //Conexion directa a base de datos
            val res= BdConnectionSql.getSinglentonInstance().GetEstadoFlujoPedido(codePedido)
            val flujo=FlujoEntregaEnPedido()
            Log.i("carga","carga")
            flujo.listEstadoEntregaPedido.addAll(res)
            flujoPedido.apply {
                postValue(flujo)
            }
        }
    }

    /*val flujoR=FlujoEntregaEnPedido()
    flujoR.listEstadoEntregaPedido=ArrayList(BdConnectionSql.getSinglentonInstance().GetEstadoFlujoPedido(codePedido))
    val flujoLiveData= MutableLiveData<FlujoEntregaEnPedido>()
    flujoLiveData.value= flujoR
    return@lazy flujoLiveData*/
}

class ActModEstadoEntregaEnPedidoFactory(val code:Int): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return ActModEstadoEntregaEnPedidoViewModel(code) as T
    }

}