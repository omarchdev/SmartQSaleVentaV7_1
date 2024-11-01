package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class Pedido {

    @SerializedName("cabecera_pedido")
    var cabeceraPedido=mCabeceraPedido()
    var listProducto=ArrayList<ProductoEnVenta>()
    var pagosEnPedido=ArrayList<mPagosEnVenta>()
    var bPedidoEntrega=false
    var entregaPedidoInfo=EntregaPedidoInfo()
    var idEntregaPedido=0
    var flujoPagoPedido=FlujoPagoPedido()
    var flujoEntrega=FlujoEntregaEnPedido()
}