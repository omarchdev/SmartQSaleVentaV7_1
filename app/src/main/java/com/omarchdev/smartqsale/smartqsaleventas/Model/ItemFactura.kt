package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes

class ItemFactura(){

    var descripcionProducto=""
    var precioUnitario=""
    var descuento=""
    var precioVenta=""
    private var numLinea=0

    private val lineas=ArrayList<String>()

    private fun generarLineaItem():String{
        var nombre=""
        var precioU=""
        var precioT=""
        var precioD=""
        var l1=""
        var l2=""
        var l3=""
        var l4=""
        numLinea=0
        if(descripcionProducto.length<=Constantes.NumeroDigitosFactura80mm.Descripcion) {
            numLinea = 2
            nombre=completarEspaciosFinal(Constantes.NumeroDigitosFactura80mm.Descripcion*2, descripcionProducto)
            precioU=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,precioUnitario)
            precioD=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,"-($descuento)")
            precioU=precioU+precioD
            precioT=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU*2,precioVenta)

            l1=nombre.substring(0,Constantes.NumeroDigitosFactura80mm.Descripcion)+precioU.substring(0,Constantes.NumeroDigitosFactura80mm.PrecioU)+precioT.substring(0,Constantes.NumeroDigitosFactura80mm.PrecioT)

        }
        else if(descripcionProducto.length<=Constantes.NumeroDigitosFactura80mm.Descripcion*2){
            numLinea = 2
            nombre=completarEspaciosFinal(Constantes.NumeroDigitosFactura80mm.Descripcion*2, descripcionProducto)
            precioU=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,precioUnitario)
            precioD=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,"-($descuento)")
            precioU=precioU+precioD
            precioT=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU*2,precioVenta)

        } else if(descripcionProducto.length<=Constantes.NumeroDigitosFactura80mm.Descripcion*3){
            numLinea = 3
            nombre=completarEspaciosFinal(Constantes.NumeroDigitosFactura80mm.Descripcion*3, descripcionProducto)
            precioU=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,precioUnitario)
            precioD=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,"-($descuento)")
            precioU=precioU+precioD
            completarEspaciosFinal(Constantes.NumeroDigitosFactura80mm.PrecioU*3,precioU)
            precioT=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU*3,precioVenta)
        }
        else if(descripcionProducto.length<=Constantes.NumeroDigitosFactura80mm.Descripcion*4){
            numLinea = 4
            nombre=completarEspaciosFinal(Constantes.NumeroDigitosFactura80mm.Descripcion*4, descripcionProducto)
            precioU=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,precioUnitario)
            precioD=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU,"-($descuento)")
            precioU=precioU+precioD
            completarEspaciosFinal(Constantes.NumeroDigitosFactura80mm.PrecioU*4,precioU)
            precioT=completarEspacios(Constantes.NumeroDigitosFactura80mm.PrecioU*4,precioVenta)

        }
        return ""
    }

    private fun completarEspaciosFinal(len:Int,descripcion:String):String{
        var d=descripcion
        var count=len-descripcion.length
        var i=0
        while(i<count){
            d="$descripcion "
            i++
        }
        return d
    }

    private fun completarEspacios(len:Int,descripcion:String):String{
        var d=descripcion
        var count=len-descripcion.length
        var i=0
        while(i<count){
            d=" $descripcion"
            i++
        }
        return d
    }


    fun obtenerLinea():String{
        return generarLineaItem()
    }
}