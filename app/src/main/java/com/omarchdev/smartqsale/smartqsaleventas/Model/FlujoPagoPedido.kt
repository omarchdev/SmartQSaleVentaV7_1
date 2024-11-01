package com.omarchdev.smartqsale.smartqsaleventas.Model

class FlujoPagoPedido {

    var listEstadoPago=ArrayList<EstadoPagoEnPedido>()

    fun EstadoPagoEntrega():String{
        var r=""
        if(listEstadoPago.size>0){
           r= "(${listEstadoPago.findLast{
               it.bMarcado
           }?.cDescripcionEstadoPago})"
        }
        return r
    }

}