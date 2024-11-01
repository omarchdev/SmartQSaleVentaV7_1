package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.estadopagoflujopedido


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.FlujoPagoPedido
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EstadoPagoFlujoPedidoViewModel(codePedido:Int) : ViewModel() {

    private val codePedido=codePedido
    private val flujoPago: MutableLiveData<FlujoPagoPedido> by lazy{
        MutableLiveData<FlujoPagoPedido>().also{
            GetFlujoPagoPedido(codePedido)
        }
    }

    fun MarcarEstadoPago(codeEstado:Int,marcado:Boolean){

        GlobalScope.launch {
            val fecha=BdConnectionSql.getSinglentonInstance().FechaServer()
            val resultOk=BdConnectionSql.getSinglentonInstance().GuardaAccionFlujoPagoPedido(1,codeEstado,fecha,codePedido)
            if(resultOk){
                flujoPago?.value?.listEstadoPago?.forEachIndexed { index, estadoPagoEnPedido ->

                    if(estadoPagoEnPedido.idEstadoPago==codeEstado){
                        estadoPagoEnPedido.bMarcado=true
                    }else{
                        estadoPagoEnPedido.bMarcado=false
                    }

                }
            }
        }

    }

    fun getw():Int=2

    fun GetFlujoPago():MutableLiveData<FlujoPagoPedido>{
        return flujoPago
    }
    fun GetFlujoPagoPedido(codePedido: Int){

        GlobalScope.launch {
            val flujoList=BdConnectionSql.getSinglentonInstance().GetEstadoFlujoPagoPedido(codePedido)
            val flujo:FlujoPagoPedido=FlujoPagoPedido()
            flujo.listEstadoPago=ArrayList(flujoList)
            flujoPago.apply {
                postValue(flujo)
            }
         }

    }


}
class EstadoPagoEnPedidoVMFactory(val code:Int): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EstadoPagoFlujoPedidoViewModel(code) as T
    }

}