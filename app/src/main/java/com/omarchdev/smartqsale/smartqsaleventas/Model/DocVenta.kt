package com.omarchdev.smartqsale.smartqsaleventas.Model

import android.graphics.Bitmap
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Numero_a_Letra

class DocVenta{



    var imageQrZonaServicio: Bitmap?=null
    var nroPedido=""
    var nombreEmisor=""
    var nombreTienda=""
    var direccionEmisor=""
    var direccion=""
    var docEmisor=""
    var tipoDoc=""
    var rucEmisor=""
    var mensajeAutorizacion=""
    var identificador=""
    var serie=""
    var mensajeRepre=""
    var enlaceNf=""
    var codigoHash=""
    var correlativo:String=""
        set(value){
            field= formatoCorrelativo(value)
        }
    var tituloReceptor="ADQUIRIENTE"
    var docReceptor=""
    var nombreReceptor=""
    var nombreVendedor=""
    var fechaEmision=""
    var fechaEntrega=""
    var datosEntrega=""
    var fechaVencimiento=""
        set(value) {
            if(value.length>0) {
                field = "Fecha Vencimiento:" + value
            }else{
                field=""
            }
        }
    var moneda=""
    var IGV=""
    var cabecerasTicket=""
    var productos=""
    var totalDescuento=""
    var totalGravada=""
    var totalIgv=""
    var total=""
    var totalCambio=""
    var observacion=""
    var bEstadoEntrega=false
    var cEstadoEntrega=""
        get() {
            if(bEstadoEntrega){
                return "Estado :  ENTREGADO"
            }else{
                return "Estado : SIN ENTREGAR"
            }

        }
    var importeLetra=""
        set(value) {
            var numero_a_Letra=Numero_a_Letra()
            var c= numero_a_Letra.Convertir(value,true)
            field="Total "+c+" SOLES\n"
        }
    var pieDoc=""
    var pagosVenta=""

}

private fun String.reemplazarTildes(): String {

    return this.replace("á","a")
            .replace("é","e")
            .replace("í","i")
            .replace("ó","o")
            .replace("ú","u")
}

fun formatoCorrelativo(numCorrelativo:String):String{
    var r=""
    r=String.format("%8s", numCorrelativo)
    return r.replace(" ","0")
}