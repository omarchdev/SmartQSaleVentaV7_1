package com.omarchdev.smartqsale.smartqsaleventas.Model

open class EstadoPagoPedido {

    var idEstadoPago=0
    var cDescripcionEstadoPago=""
    var cDescripcionAdicionalPago=""
}
class EstadoPagoEnPedido:EstadoPagoPedido(){


    var bMarcado:Boolean=false
}