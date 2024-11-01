package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import org.apache.commons.codec.binary.Base64;

fun MaximoPermitidoValor(maxValue: Int, valorVerificar: String): Boolean {

    if (maxValue <= valorVerificar.toInt()) {
        return true
    } else {
        return false
    }

}

fun String.DecodeBase64(a: Int): String {
    var text = this
    for (i in 0..a - 1) {

        var byteArray: ByteArray = Base64.decodeBase64(text.toByteArray())
        text = String(byteArray)
    }
    return text
}


fun GetValueCodeCia(): String {

    var text = Constantes.Empresa.idEmpresa.toString() + "-" + Constantes.Tienda.idTienda.toString()
    for (i in 0..1) {
        var byteArray = Base64.encodeBase64(text.toByteArray())
        text = String(byteArray)
    }

    return text
}