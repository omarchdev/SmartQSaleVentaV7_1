package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraVenta

import java.math.BigDecimal
import kotlin.jvm.internal.Intrinsics
import kotlin.TypeCastException





class ConstructorFactura(){

     final var lNombre="16"

    fun ConstructorCabecera(cabeceraVenta: mCabeceraVenta, tipoDoc:Int):String{
        var textoCabeceraVenta=""
        when(tipoDoc){

            Constantes.TipoDocumentoPago.NOTAVENTA->{}
            Constantes.TipoDocumentoPago.FACTURA->{
                textoCabeceraVenta= cabeceraVenta.emisor+"\n"+cabeceraVenta.nombreCalle+"\n"+cabeceraVenta.distrito+"-"+cabeceraVenta.nombreCiudad+"-"+cabeceraVenta.countrySubEntity+"\n"+"\n"+"FACTURA ELECTRONICA"+"\n"+cabeceraVenta.numSerie+"-"+ formatoCorrelativo(cabeceraVenta.numCorrelativo)
            }
            Constantes.TipoDocumentoPago.BOLETA->{
                textoCabeceraVenta= cabeceraVenta.emisor+"\n"+cabeceraVenta.nombreCalle+"\n"+cabeceraVenta.distrito+"-"+cabeceraVenta.nombreCiudad+"-"+cabeceraVenta.countrySubEntity+"\n"+"\n"+""+"BOLETA DE VENTA ELECTRONICA"+"\n"+cabeceraVenta.numSerie+ formatoCorrelativo(cabeceraVenta.numCorrelativo)
            }
        }
        return textoCabeceraVenta
    }

    fun generarListadoItems53mmPedido(p:List<ProductoEnVenta>):String{
        var t=""
        p.forEach {
            t=t+itemPNombre55mmPedido(it)+"\n"
        }
        return t
    }
    private fun itemPNombre55mmPedido(p:ProductoEnVenta):String{

        var n=(p.productName+" "+p.descripcionVariante+" "+p.observacionProducto).trim()
        var linea=""
        val colNombre = 32
        
        val chunks = mutableListOf<String>()
        var tempN = n
        while (tempN.isNotEmpty()) {
            if (tempN.length <= colNombre) {
                chunks.add(completarEspacios(colNombre, tempN))
                tempN = ""
            } else {
                chunks.add(tempN.substring(0, colNombre))
                tempN = tempN.substring(colNombre)
            }
        }

        for (chunk in chunks) {
            linea += chunk + "\n"
        }

        linea=linea+ completarEspaciosI(14,p.cantidad.fUnid)+
                completarEspaciosI(9,p.precioOriginal.fortMoneda)+
                completarEspaciosI(9,p.precioVentaFinal.fortMoneda)

        return linea

    }


    fun generarListadoItems53mm(p:List<ProductoEnVenta>):String{
        var t=""
        p.forEach {
            t=t+itemPNombre55mm(it)+"\n"
        }
        return t
    }
    private fun itemPNombre55mm(p:ProductoEnVenta):String{

        var n=(p.productName+" "+p.descripcionVariante).trim()

        var linea=""
        val colNombre = 32
        
        val chunks = mutableListOf<String>()
        var tempN = n
        while (tempN.isNotEmpty()) {
            if (tempN.length <= colNombre) {
                chunks.add(completarEspacios(colNombre, tempN))
                tempN = ""
            } else {
                chunks.add(tempN.substring(0, colNombre))
                tempN = tempN.substring(colNombre)
            }
        }

        for (chunk in chunks) {
            linea += chunk + "\n"
        }

        if(p.isControlTiempo){
            linea=linea+p.informacionAdicionalTiempo
        }
        linea=linea+ completarEspaciosI(5,p.unidad_medida_impresion)+
                completarEspaciosI(9,p.cantidad.fUnid)+
                completarEspaciosI(9,p.precioOriginal.fortMoneda)+
                completarEspaciosI(9,p.precioVentaFinal.fortMoneda)

        return linea

    }

    private fun itemPNombre55mmPedidoPrecuenta(p: ProductoEnVenta): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(p.productName)
        stringBuilder.append(" ")
        stringBuilder.append(p.descripcionVariante)
        stringBuilder.append(" ")
        stringBuilder.append(p.observacionProducto)
        var stringBuilder2: String? = stringBuilder.toString()
        if (stringBuilder2 != null) {
            stringBuilder2 =stringBuilder2.trim()
            var linea = ""
            val stringBuilder3: StringBuilder
            if (p.productName.length <= 32) {
                stringBuilder2 = completarEspacios(32, stringBuilder2!!)
                stringBuilder3 = StringBuilder()
                stringBuilder3.append(stringBuilder2)
                stringBuilder3.append("\n")
                linea = stringBuilder3.toString()
            } else if (p.productName.length <= 64) {
                stringBuilder2 = completarEspacios(64, stringBuilder2!!)
                stringBuilder3 = StringBuilder()
                if (stringBuilder2 != null) {
                    var r4 = stringBuilder2.substring(0, 32)
                    Intrinsics.checkExpressionValueIsNotNull(r4, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                    stringBuilder3.append(r4)
                    stringBuilder3.append("\n")
                    if (stringBuilder2 != null) {
                        var r3 = stringBuilder2.substring(32, 64)
                        Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                        stringBuilder3.append(r3)
                        stringBuilder3.append("\n")
                        linea = stringBuilder3.toString()
                    } else {
               //         throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                    }
                }
          //      throw TypeCastException("null cannot be cast to non-null type java.lang.String")
            } else if (p.productName.length <= 98) {
                stringBuilder2 = completarEspacios(98, stringBuilder2!!)
                stringBuilder3 = StringBuilder()
                if (stringBuilder2 != null) {
                    var r4 = stringBuilder2.substring(0, 32)
                    Intrinsics.checkExpressionValueIsNotNull(r4, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                    stringBuilder3.append(r4)
                    stringBuilder3.append("\n")
                    if (stringBuilder2 != null) {
                        var r3 = stringBuilder2.substring(32, 64)
                        Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                        stringBuilder3.append(r3)
                        stringBuilder3.append("\n")
                        if (stringBuilder2 != null) {
                            r3 = stringBuilder2.substring(64, 98)
                            Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                            stringBuilder3.append(r3)
                            stringBuilder3.append("\n")
                            linea = stringBuilder3.toString()
                        } else {
                         //   throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                        }
                    }
            //        throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                }
             //   throw TypeCastException("null cannot be cast to non-null type java.lang.String")
            } else if (p.productName.length <= 130) {
                stringBuilder2 = completarEspacios(130, stringBuilder2!!)
                stringBuilder3 = StringBuilder()
                if (stringBuilder2 != null) {
                    var r4 = stringBuilder2.substring(0, 32)
                    Intrinsics.checkExpressionValueIsNotNull(r4, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                    stringBuilder3.append(r4)
                    stringBuilder3.append("\n")
                    if (stringBuilder2 != null) {
                       var r3 = stringBuilder2.substring(32, 64)
                        Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                        stringBuilder3.append(r3)
                        stringBuilder3.append("\n")
                        if (stringBuilder2 != null) {
                            r3 = stringBuilder2.substring(64, 98)
                            Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                            stringBuilder3.append(r3)
                            if (stringBuilder2 != null) {
                                r3 = stringBuilder2.substring(98, 130)
                                Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                                stringBuilder3.append(r3)
                                stringBuilder3.append("\n")
                                stringBuilder3.append("\n")
                                linea = stringBuilder3.toString()
                            } else {
                   //             throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                            }
                        }
                 //       throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                    }
               //     throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                }
               // throw TypeCastException("null cannot be cast to non-null type java.lang.String")
            } else if (p.productName.length <= 161) {
                stringBuilder2 = completarEspacios(161, stringBuilder2!!)
                stringBuilder3 = StringBuilder()
                if (stringBuilder2 != null) {
                    var r4 = stringBuilder2.substring(0, 32)
                    Intrinsics.checkExpressionValueIsNotNull(r4, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                    stringBuilder3.append(r4)
                    stringBuilder3.append("\n")
                    if (stringBuilder2 != null) {
                        var r3 = stringBuilder2.substring(32, 64)
                        Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                        stringBuilder3.append(r3)
                        stringBuilder3.append("\n")
                        if (stringBuilder2 != null) {
                            r3 = stringBuilder2.substring(64, 98)
                            Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                            stringBuilder3.append(r3)
                            if (stringBuilder2 != null) {
                                r3 = stringBuilder2.substring(98, 130)
                                Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                                stringBuilder3.append(r3)
                                stringBuilder3.append("\n")
                                if (stringBuilder2 != null) {
                                    r3 = stringBuilder2.substring(130, 161)
                                    Intrinsics.checkExpressionValueIsNotNull(r3, "(this as java.lang.Strin…ing(startIndex, endIndex)")
                                    stringBuilder3.append(r3)
                                    stringBuilder3.append("\n")
                                    stringBuilder3.append("\n")
                                    linea = stringBuilder3.toString()
                                } else {
                       //             throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                                }
                            }
                     //       throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                        }
                   //     throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                    }
                 //   throw TypeCastException("null cannot be cast to non-null type java.lang.String")
                }
               // throw TypeCastException("null cannot be cast to non-null type java.lang.String")
            }
            return linea
        }
        throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
    }

    fun generarListadoItems53mmPedidoPreCuenta(p: List<ProductoEnVenta>): String {
        Intrinsics.checkParameterIsNotNull(p, "p")
        var t: String = ""
        for (it in p) {
            val stringBuilder = StringBuilder()
            stringBuilder.append(t)
            stringBuilder.append(itemPNombre55mm(it))
     //       stringBuilder.append(itemPNombre55mmPedidoPrecuenta(it))
            stringBuilder.append("\n")
            t = stringBuilder.toString()
        }
        return t
    }


    fun generarListadoItems80mm(p:List<ProductoEnVenta>):String{

        var t=""

        p.forEach {
            t=t+item(it)+"\n"
        }

        return t
    }

    private fun item(p: ProductoEnVenta): String {
        var n = "[" + p.cantidad.fUnid + "]" + p.productName
        val textoDescuento = if (p.montoDescuento.compareTo(BigDecimal.ZERO) == 0) "" else "(${p.montoDescuento.fortMoneda3})"

        val colNombre = 24
        val colPU = 12
        val colPT = 12

        var linea = ""
        val chunks = mutableListOf<String>()
        var tempN = n
        while (tempN.isNotEmpty()) {
            if (tempN.length <= colNombre) {
                chunks.add(completarEspacios(colNombre, tempN))
                tempN = ""
            } else {
                chunks.add(tempN.substring(0, colNombre))
                tempN = tempN.substring(colNombre)
            }
        }

        if (chunks.isEmpty()) chunks.add(completarEspacios(colNombre, ""))

        for (i in chunks.indices) {
            when (i) {
                0 -> {
                    linea += chunks[i] + completarEspaciosI(colPU, p.precioOriginal.fortMoneda3) +
                            completarEspaciosI(colPT, p.precioVentaFinal.fortMoneda3) + "\n"
                }
                1 -> {
                    linea += chunks[i] + completarEspaciosI(colPU, textoDescuento) + completarEspacios(colPT, "") + "\n"
                }
                else -> {
                    linea += chunks[i] + "\n"
                }
            }
        }

        if (chunks.size == 1 && textoDescuento.isNotEmpty()) {
            linea += completarEspacios(colNombre, "") + completarEspaciosI(colPU, textoDescuento) + completarEspacios(colPT, "") + "\n"
        }

        return linea
    }




}

fun formatoCorrelativo(serie:Int):String{
    var s=""
    s=String.format("%8s", serie)
    s=s.replace(" ","0")
    return s
}
private val BigDecimal.fortMoneda3: String
    get() {
        return String.format("%.3f",this)
    }


fun completarEspacios(espacios:Int,cadena:String):String{

    return String.format("%1$-${espacios}S", cadena)
}

fun completarEspaciosI(espacios:Int,cadena:String):String{

    return String.format("%${espacios}S", cadena)
}

fun generarCadenaCaracteres(caracter:String,cantidad:Int):String{

    var cadena=""

    for (i in 0..cantidad){
        cadena=cadena+caracter
    }
    return cadena
}

private val Float.fUnid: String
    get() {

        return String.format("%.2f",this)

    }
