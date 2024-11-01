package com.omarchdev.smartqsale.smartqsaleventas.Model

class FlujoEntregaEnPedido {

    var listEstadoEntregaPedido=ArrayList<EstadoEntregaPedidoEnUso>()

    fun EstadoEntrega():String{
        if(listEstadoEntregaPedido.size>0){
            return "(${listEstadoEntregaPedido.findLast { it.bMarcado }!!.cDescripcionEstado})"
        }
        return ""
    }
}