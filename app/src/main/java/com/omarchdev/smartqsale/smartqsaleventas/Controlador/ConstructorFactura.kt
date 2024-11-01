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
        if(p.productName.length<=32){
            n= completarEspacios(32,n)
            linea=n+"\n"
        }else if(p.productName.length<=64){
            n= completarEspacios(64,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"
        }else if(p.productName.length<=98){
            n= completarEspacios(98,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"+n.substring(64,98)+"\n"
        }else if(p.productName.length<=130){

            n= completarEspacios(130,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"+n.substring(64,98)+n.substring(98,130)+"\n"+
                    "\n"
        }else if(p.productName.length<=161){

            n= completarEspacios(161,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"+
                    n.substring(64,98)+n.substring(98,130)+"\n"+
                    n.substring(130,161)+"\n"+
                    "\n"

        }

        linea=linea+ completarEspaciosI(10,p.cantidad.fUnid)+
                completarEspaciosI(10,p.precioOriginal.fortMoneda)+
                completarEspaciosI(10,p.precioVentaFinal.fortMoneda)

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
        if(p.productName.length<=32){
            n= completarEspacios(32,n)
            linea=n+"\n"
        }else if(p.productName.length<=64){
            n= completarEspacios(64,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"
        }else if(p.productName.length<=98){
            n= completarEspacios(98,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"+n.substring(64,98)+"\n"
        }else if(p.productName.length<=130){

            n= completarEspacios(130,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"+n.substring(64,98)+n.substring(98,130)+"\n"+
                    "\n"
        }else if(p.productName.length<=161){

            n= completarEspacios(161,n)
            linea=n.substring(0,32)+"\n"+n.substring(32,64)+"\n"+
                    n.substring(64,98)+n.substring(98,130)+"\n"+
                    n.substring(130,161)+"\n"+
                    "\n"

        }
        if(p.isControlTiempo){
            linea=linea+p.informacionAdicionalTiempo
        }
        linea=linea+ completarEspaciosI(10,p.cantidad.fUnid)+
        completarEspaciosI(10,p.precioOriginal.fortMoneda)+
                completarEspaciosI(10,p.precioVentaFinal.fortMoneda)

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

    private fun item(p:ProductoEnVenta):String{

        var linea=""

        var n="["+p.cantidad.fUnid+"]"+p.productName

        if(n.length<=16){
            linea=completarEspacios(16,n)+ completarEspaciosI(12, p.precioOriginal.fortMoneda3)+
                    completarEspaciosI(12,p.precioVentaFinal.fortMoneda3)+"\n"+
            completarEspaciosI(16,"")+ completarEspacios(12,
                    "(${p.montoDescuento.fortMoneda3})")+ completarEspacios(12,"")

        }else if(n.length<=32){
           n= completarEspacios(32,n)
           linea=n.substring(0,16)+completarEspaciosI(12, p.precioOriginal.fortMoneda3)+
                   completarEspaciosI(12,p.precioVentaFinal.fortMoneda3)+"\n"+
                   completarEspacios(16,n.substring(16,n.length))+
                   completarEspaciosI(12,"(${p.montoDescuento.fortMoneda3})")+
                   completarEspacios(12,"")

        }else if(n.length<=48){
            n=completarEspacios(48,n)
            linea=n.substring(0,16)+completarEspaciosI(12, p.precioOriginal.fortMoneda3)+
                    completarEspaciosI(12,p.precioVentaFinal.fortMoneda3)+"\n"+completarEspacios(16,n.substring(16,32))+
                    completarEspaciosI(12,"(${p.montoDescuento.fortMoneda3})")+
                    completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(32,n.length))+
                    completarEspacios(12,"")+ completarEspacios(12,"")
        }else if(n.length<=64){
            n=completarEspacios(64,n)
            linea=n.substring(0,16)+completarEspaciosI(12, p.precioOriginal.fortMoneda3)+
                    completarEspaciosI(12,p.precioVentaFinal.fortMoneda3)+"\n"+
                    completarEspacios(16,n.substring(16,32))+
                    completarEspaciosI(12,"(${p.montoDescuento.fortMoneda3})")+
                    completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(32,48))+
                    completarEspacios(12,"")+
                            completarEspacios(12,"")+"\n"+
                            completarEspacios(16,n.substring(48,n.length))+
                    completarEspacios(12,"")+ completarEspacios(12,"")


        }else if(n.length<=80){
            n=completarEspacios(80,n)
            linea=n.substring(0,16)+completarEspacios(12, p.precioOriginal.fortMoneda3)+
                    completarEspacios(12,p.precioVentaFinal.fortMoneda3)+"\n"+
                    completarEspacios(16,n.substring(16,32))+
                    completarEspacios(12,"(${p.montoDescuento.fortMoneda3})")+
                    completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(32,48))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+                                         completarEspacios(16,n.substring(48,64))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(64,n.length))+
                    completarEspacios(12,"")+ completarEspacios(12,"")


        }else if(n.length<=96){
            n=completarEspacios(96,n)
            linea=n.substring(0,16)+completarEspaciosI(12, p.precioOriginal.fortMoneda3)+
                    completarEspaciosI(12,p.precioVentaFinal.fortMoneda3) +"\n"+
                    completarEspacios(16,n.substring(16,32))+
                    completarEspaciosI(12,"(${p.montoDescuento.fortMoneda3})")+
                    completarEspacios(12,"")+"\n"+
            completarEspacios(16,n.substring(32,48))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+                                         completarEspacios(16,n.substring(48,64))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(64,80))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(80,n.length))+
                    completarEspacios(12,"")+ completarEspacios(12,"")


        }else if(n.length<=112){
            n=completarEspacios(112,n)
            linea=n.substring(0,16)+completarEspaciosI(12, p.precioOriginal.fortMoneda3)+
                    completarEspaciosI(12,p.precioVentaFinal.fortMoneda3) +"\n"+
                    completarEspacios(16,n.substring(16,32))+
                    completarEspaciosI(12,"(${p.montoDescuento.fortMoneda3})")+
                    completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(32,48))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+                                           completarEspacios(16,n.substring(48,64))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(64,80))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+
                   completarEspacios(16,n.substring(80,96))+
                    completarEspacios(12,"")+ completarEspacios(12,"")+"\n"+
                    completarEspacios(16,n.substring(96,n.length))+
                    completarEspacios(12,"")+ completarEspacios(12,"")

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
