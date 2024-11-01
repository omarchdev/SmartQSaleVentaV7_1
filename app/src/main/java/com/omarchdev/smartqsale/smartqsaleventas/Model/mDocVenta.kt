package com.omarchdev.smartqsale.smartqsaleventas.Model

class mDocVenta(cabeceraVenta:mCabeceraVenta,productoEnVenta:List<ProductoEnVenta>){


    val tipoOperacion="generar_comprobante"
    val cabeceraVenta=cabeceraVenta
    val productoEnVenta=productoEnVenta
    var esNota=false
    var fechaReferencia=""
    var serieReferencia=""
    var correlativoReferencia=0
    var tipoDocReferenciaChar=""
    var tipoDocReferenciaNum=0
    var tipoNotaCredito=0
    var codeResult=0
    var messageResult=""
    var idResult=0
}