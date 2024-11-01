package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.detallepedido

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVentas
import com.omarchdev.smartqsale.smartqsaleventas.Model.Pedido
import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetallePedidoViewModel : ViewModel() {


    val pedido=MutableLiveData<Pedido>().apply { value=Pedido() }
    val listPagos=MutableLiveData<List<mPagosEnVenta>>().apply { value=ArrayList() }
    val mostrarData=MutableLiveData<Boolean>().apply{ value=false }
    val estadoEntregado=MutableLiveData<Boolean>().apply{value=false}


    fun ActualizarEstadoEntregado(){

    }

    fun GetPedido(id:Int,estadoPagado:Boolean){
        GlobalScope.launch {
            val controladorVentas=ControladorVentas()

            val listPagosTemp=controladorVentas.getPagosRealizadosDetallePedido(id);// BdConnectionSql.getSinglentonInstance().getPagosRealizadosDetallePedido(id)
            val pedidoTemp= controladorVentas.ObtenerPedidoId(id,estadoPagado)

            pedido.postValue(pedidoTemp)
            listPagos.postValue(listPagosTemp)
            estadoEntregado.postValue(pedidoTemp.cabeceraPedido.isbEntregado())
        }
    }
    fun ActualizaEstadoEntregadoPedido(){
        GlobalScope.launch {
            var result=BdConnectionSql.getSinglentonInstance().ActualizarEstadoEntrega(pedido.value!!.cabeceraPedido.idCabecera)

            estadoEntregado.postValue(result.bestado)
        }
     }
}
