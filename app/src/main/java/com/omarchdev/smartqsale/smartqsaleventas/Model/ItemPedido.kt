package com.omarchdev.smartqsale.smartqsaleventas.Model






class ItemPedido{

    var descripcion=""
    var cantidad=""


    private fun generarTexto():String{
        val linea = ""
        val stringBuilder = StringBuilder()
        stringBuilder.append(this.cantidad)
        stringBuilder.append(" ")
        val str = this.descripcion
        if (str != null) {
            stringBuilder.append(str.trim())
            stringBuilder.append("\n")

        }
        return stringBuilder.toString()
/*
        var linea=""
        var d=""
        var d1=""
        var d2=""
        var d3=""
        var d4=""
        var d5=""
        var q=""
        if(descripcion.length<=ConfigPedido.Pedido80MM.lenCNombre-4){
            d=completarEspaciosFinal(ConfigPedido.Pedido80MM.lenCNombre,descripcion.trim())
            q=completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,cantidad)
            linea=d+q+"\n"
            linea.length
        }else if(descripcion.length<=(ConfigPedido.Pedido80MM.lenCNombre*2)-4){

            d=completarEspacios(ConfigPedido.Pedido80MM.lenCNombre*2,descripcion)
            q=completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,cantidad)
            d1=d.substring(0,(ConfigPedido.Pedido80MM.lenCNombre)-4)+"    "
            d2=d.substring((ConfigPedido.Pedido80MM.lenCNombre)-4,d.length-1)
            linea=d1+q+"\n"+d2+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"


        }else if(descripcion.length<=(ConfigPedido.Pedido80MM.lenCNombre*3)-4){
            d=completarEspacios(ConfigPedido.Pedido80MM.lenCNombre*3,descripcion)
            q=completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,cantidad)
            d1=d.substring(0,(ConfigPedido.Pedido80MM.lenCNombre)-4)+"    "
            d2=d.substring((ConfigPedido.Pedido80MM.lenCNombre)-4,(ConfigPedido.Pedido80MM.lenCNombre*2)-4)+"    "
            d3=d.substring((ConfigPedido.Pedido80MM.lenCNombre*2)-4,d.length-1)

            linea=d1+q+"\n"+d2+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"+
                    d3+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"
        }else if(descripcion.length<=(ConfigPedido.Pedido80MM.lenCNombre*4)-4){
            d=completarEspacios(ConfigPedido.Pedido80MM.lenCNombre*3,descripcion)
            q=completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,cantidad)
            d1=d.substring(0,(ConfigPedido.Pedido80MM.lenCNombre)-4)+"    "
            d2=d.substring((ConfigPedido.Pedido80MM.lenCNombre)-4,(ConfigPedido.Pedido80MM.lenCNombre*2)-4)+"    "
            d3=d.substring((ConfigPedido.Pedido80MM.lenCNombre*2)-4,(ConfigPedido.Pedido80MM.lenCNombre*3)-4)+"    "
            d4=d.substring((ConfigPedido.Pedido80MM.lenCNombre*3)-4,d.length-1)
            linea=d1+q+"\n"+d2+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"+
                    d3+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"+
                    d4+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"
        }else if(descripcion.length<=(ConfigPedido.Pedido80MM.lenCNombre*5)-4){
            d=completarEspacios(ConfigPedido.Pedido80MM.lenCNombre*3,descripcion)
            q=completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,cantidad)
            d1=d.substring(0,(ConfigPedido.Pedido80MM.lenCNombre)-4)+"    "
            d2=d.substring((ConfigPedido.Pedido80MM.lenCNombre)-4,(ConfigPedido.Pedido80MM.lenCNombre*2)-4)+"    "
            d3=d.substring((ConfigPedido.Pedido80MM.lenCNombre*2)-4,(ConfigPedido.Pedido80MM.lenCNombre*3)-4)+"    "
            d4=d.substring((ConfigPedido.Pedido80MM.lenCNombre*3)-4,(ConfigPedido.Pedido80MM.lenCNombre*4)-4)+"    "
            d5=d.substring((ConfigPedido.Pedido80MM.lenCNombre*5)-4,d.length-1)
            linea=d1+q+"\n"+d2+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"+
                    d3+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"+
                    d4+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"+
                    d5+completarEspacios(ConfigPedido.Pedido80MM.lenCCantidad,"")+"\n"
        }*/
    }

    fun cadenaPedido():String{
        return generarTexto()
    }

    private fun completarEspaciosFinal(len:Int,descripcion:String):String{
        var d=descripcion
        var count=len-descripcion.length
        var i=0
        var e=" "
        while(i<count){
            e=e+" "
            i++
        }
        d=descripcion+e
        return d
    }

    private fun completarEspacios(len:Int,descripcion:String):String{
        var d=" "
        var count=len-descripcion.length
        var i=0
        var e=" "
        while(i<count){
            e=e+" "
            i++
        }
        d=e+descripcion
        return d
    }


}

abstract class ConfigPedido {


    object Pedido80MM {

        val lenCTotal=40
        val lenCNombre=28
        val lenCCantidad=8

    }
}