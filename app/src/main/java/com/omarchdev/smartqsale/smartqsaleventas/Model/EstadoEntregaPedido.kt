package com.omarchdev.smartqsale.smartqsaleventas.Model

open class EstadoEntregaPedido {

    var idEstadoEntrega=0
    var cCodigoEstadoEntrega=""
    var cDescripcionEstado=""
    var cDescripcionAdicionalEstado=""

}

class EstadoEntregaPedidoEnUso:EstadoEntregaPedido(){

    var bMarcado=false
    var bDisponibleMarcado=true
}