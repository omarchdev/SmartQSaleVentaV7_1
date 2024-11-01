package com.omarchdev.smartqsale.smartqsaleventas.Model

class EntregaPedidoInfo {

    var tiempoEntregaPedido=TiempoEntregaPedido()
    var tipoEntregaPedido=TipoEntregaPedido()
    var clienteEntrega=mCustomer()
    var medioPagoEntrega=MedioPagoEntrega()
    var infoAdicionalEntregaPedido=InfoAdicionalEntregaPedido()
    var cNumeroPedido=""
    var cFechaCreacion=""
    var usaEntregaPedido=false;

}