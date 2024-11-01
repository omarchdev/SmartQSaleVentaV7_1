package com.omarchdev.smartqsale.smartqsaleventas.Model

class InfoGuardadoPedido(str1: String,str2: String,str3: String, fecha: String, bEsContrato: Boolean){

    var str1: String=str1
    var str2: String=str2
    var str3: String=str3
    var fecha: String=fecha
    var bEsContrato: Boolean=bEsContrato
    var imprimi:Boolean=true
    var idCabeceraPedido=0;
    var usaEntregaPedido=false;
    var entregaPedidoInfo=EntregaPedidoInfo()
    var reservaPedido=true;
}

